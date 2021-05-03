package ru.rt.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import ru.rt.appcontainer.api.AppComponent;
import ru.rt.appcontainer.api.AppComponentsContainer;
import ru.rt.appcontainer.api.AppComponentsContainerConfig;
import ru.rt.exceptions.CreateContextException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) throws CreateContextException {
        processConfig(initialConfigClass);
    }
    public AppComponentsContainerImpl(String packageName) throws CreateContextException {
        var reflections = new Reflections(packageName,
                                        new TypeAnnotationsScanner(),
                                        new SubTypesScanner(false));
        var classes = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                                        .toArray(Class[]::new);
        processConfig(classes);
    }

    private void processConfig(Class<?>... configClasses) throws CreateContextException {
        checkConfigClass(configClasses);
        var classes = getSortedAppComponentsContainerConfig(configClasses);
        prepareAndSetAppComponents(classes);
    }

    private void prepareAndSetAppComponents(List<Class<?>> classes) throws CreateContextException {
        for (Class<?> configClass : classes){
            try {
                var methods = getSortedMethodsAppComponent(configClass.getDeclaredMethods());
                Object configInstance = null;
                configInstance = configClass.getDeclaredConstructor().newInstance();
                setAppComponents(configInstance, methods);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new CreateContextException("Create context exception");
            }
        }
    }

    private List<Class<?>> getSortedAppComponentsContainerConfig(Class<?>... configClasses){
        return Arrays.stream(configClasses)
                .sorted(Comparator.comparingInt(clazz -> clazz.getAnnotation(AppComponentsContainerConfig.class).order()))
                .collect(Collectors.toList());
    }

    private List<Method> getSortedMethodsAppComponent(Method[] methods){
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
    }

    private void setAppComponents(Object configInstance, List<Method> methods) throws InvocationTargetException, IllegalAccessException {
        for (Method method: methods){
            Object componentInstance = getComponentInstance(configInstance, method);
            appComponents.add(componentInstance);
            appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), componentInstance);
        }
    }

    private Object getComponentInstance(Object configInstance, Method method) throws IllegalAccessException, InvocationTargetException {
        method.setAccessible(true);
        Object[] params = getMethodParamsValues(method);
        return method.invoke(configInstance, params);
    }

    private Object[] getMethodParamsValues(Method method) {
        var paramsType = method.getParameterTypes();
        var paramsObj = new Object[paramsType.length];

        for (var i = 0 ; i < paramsType.length ; i++) {
            paramsObj[i] = getAppComponent(paramsType[i]);
            if (paramsObj[i] == null) {
                throw new IllegalArgumentException(String.format("Not enough parameters for method %s.%s()", method.getDeclaringClass(), method.getName()));
            }
        }
        return paramsObj;
    }

    private void checkConfigClass(Class<?>... configClasses) {
        for (Class<?> configClass : configClasses){
            if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
            }
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                                .findFirst()
                                .orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
