package ru.rt;

import ru.rt.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {
    private final Class<?> classTest;
    private final Map<Class<?>, List<Method>> annotatedMethods;

    public TestRunner(Class<?> classTest){
        this.classTest  = classTest;
        var annotations = (Class<? extends Annotation>[]) new Class[]{Test.class, Before.class, After.class};
        this.annotatedMethods = getAnnotatedMethods(annotations);
    }

    public void runAllTests(){
        TestStat stats = new TestStat();

        List<Method> testMethods = annotatedMethods.get(Test.class);
        for(Method testMethod : testMethods){
            boolean isPassed = runTest(testMethod);
            stats.saveTestResult(testMethod.getName(), isPassed);
        }

        stats.printStats();
    }

    private boolean runTest(Method testMethod){
        boolean isPassed = false;
        Object testObject = null;
        try{
            Constructor<?> constructor = classTest.getConstructor();
            testObject = constructor.newInstance();

            invokeMethods(testObject, Before.class);

            testMethod.invoke(testObject);

            isPassed = true;
        }catch(Exception e){
            printError(e);
        }finally {
            isPassed &= invokeMethodsFinally(testObject, After.class);
        }

        return isPassed;
    }

    private Map<Class<?>, List<Method>> getAnnotatedMethods(Class<? extends Annotation>[] annotations){
        Map<Class<?>, List<Method>> annotatedMethods = new HashMap<>();
        for (Class<? extends Annotation> annotation : annotations){
            annotatedMethods.put(annotation, new ArrayList<>());
        }
        Method[] clazzMethods = classTest.getDeclaredMethods();
        for (Method method : clazzMethods){
            for (Class<? extends Annotation> annotation : annotations){
                if (method.isAnnotationPresent(annotation)) {
                    List<Method> methods = annotatedMethods.get(annotation);
                    methods.add(method);
                }
            }
        }
        return annotatedMethods;
    }

    private void invokeMethods(Object testObject, Class<? extends Annotation> annotation) throws InvocationTargetException, IllegalAccessException {
            List<Method> methods = annotatedMethods.get(annotation);
            for (Method beforeMethod : methods) {
                beforeMethod.invoke(testObject);
            }
    }

    private boolean invokeMethodsFinally(Object testObject, Class<? extends Annotation> annotation){
        try{
            invokeMethods(testObject, annotation);
            return true;
        }catch(Exception e){
            printError(e);
            return false;
        }
    }

    private void printError(Exception e){
        System.out.println("     " + e.getCause().toString());
    }
}
