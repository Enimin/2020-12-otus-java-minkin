package ru.rt;

import ru.rt.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class TestRunner {
    private final Class<?> classTest;

    public TestRunner(Class<?> classTest){
        this.classTest  = classTest;
    }

    public void runAllTests(){
        TestStat stats = new TestStat();

        ArrayList<Method> testMethods = getAnnotatedMethods(classTest, Test.class);
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

    private static ArrayList<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation){
        Method[] clazzMethods = clazz.getDeclaredMethods();
        ArrayList<Method> annotatedMethods = new ArrayList<>();
        for (Method method : clazzMethods){
            if (method.isAnnotationPresent(annotation)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    private void invokeMethods(Object testObject, Class<? extends Annotation> annotation) throws InvocationTargetException, IllegalAccessException {
            ArrayList<Method> methods = getAnnotatedMethods(classTest, annotation);
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
