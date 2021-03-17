package ru.rt;

import ru.rt.annotations.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class TestInvocation {
    private TestInvocation(){}

    static TestLogingInterface createMyClass(Object obj) {
        var annotatedMethods = getAnnotatedMethods(obj.getClass(), Log.class);
        var handler = new TestInvocationHandler(obj, annotatedMethods);
        return (TestLogingInterface) Proxy.newProxyInstance(TestInvocation.class.getClassLoader(),
                                                            new Class<?>[]{TestLogingInterface.class},
                                                            handler);
    }

    static List<String> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation){
        return Arrays.stream(clazz.getDeclaredMethods())
                     .filter(method -> method.isAnnotationPresent(annotation))
                     .map(TestInvocation::getMethodSpecification)
                     .collect(Collectors.toList());
    }

    static String getMethodSpecification(Method method){
        return method.getName()+Arrays.toString(method.getParameterTypes());
    }

    static class TestInvocationHandler implements InvocationHandler{
        private final Object obj;
        private final List<?> logMethods;

        public TestInvocationHandler(Object o, List<?> m) {
            obj = o;
            logMethods = m;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logMethods.contains(getMethodSpecification(method))) {
                System.out.println("executed method: " + method.getName() + ", param(s): " + Arrays.toString(args));
            }
            return method.invoke(obj, args);
        }
    }
}
