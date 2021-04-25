package ru.rt.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import ru.rt.appcontainer.api.AppComponent;
import ru.rt.appcontainer.api.AppComponentsContainer;
import ru.rt.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        processConfig(initialConfigClass);
    }
    public AppComponentsContainerImpl(String packageName) {
        var reflections = new Reflections(packageName,
                                        new TypeAnnotationsScanner(),
                                        new SubTypesScanner(false));
        var classes = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                                        .toArray(Class[]::new);
        processConfig(classes);
    }

    private void processConfig(Class<?>... configClasses) {
        checkConfigClass(configClasses);

        var classes = Arrays.stream(configClasses)
                                       .sorted(Comparator.comparingInt(clazz -> clazz.getAnnotation(AppComponentsContainerConfig.class).order()))
                                       .collect(Collectors.toList());

        for (Class<?> configClass : classes){
            try {
                var methods = Arrays.stream(configClass.getDeclaredMethods())
                                                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                                                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                                                .collect(Collectors.toList());
                var configInstance = configClass.getDeclaredConstructor().newInstance();
                setAppComponents(configInstance, methods);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAppComponents(Object configInstance, List<Method> methods) {
        try {
            for (Method method: methods){
                Object componentInstance = getComponentInstance(configInstance, method);
                appComponents.add(componentInstance);
                appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), componentInstance);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
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
