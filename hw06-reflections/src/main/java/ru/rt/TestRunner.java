package ru.rt;

import ru.rt.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class TestRunner<T> {
    private final Class<T> classTest;
    private boolean isPassed = false;
    private boolean isBeforePassed = false;
    private boolean isTestPassed = false;

    public TestRunner(Class<T> classTest){
        this.classTest  = classTest;
    }

    public void runAllTests(){
        TestStat stats = new TestStat();

        ArrayList<Method> testMethods = getAnnotatedMethods(classTest, Test.class);
        for(Method testMethod : testMethods){
            runTest(testMethod);
            stats.saveTestResult(testMethod.getName(), isPassed);
        }

        stats.printStats();
    }

    private void runTest(Method testMethod){

        ArrayList<Method> beforeMethods = getAnnotatedMethods(classTest, Before.class);
        ArrayList<Method> afterMethods = getAnnotatedMethods(classTest, After.class);

        Object testObject = null;

        try{
            Constructor<T> constructor = classTest.getConstructor();
            testObject = constructor.newInstance();

            for(Method beforeMethod : beforeMethods){
                beforeMethod.invoke(testObject);
            }
            isBeforePassed = true;

            testMethod.invoke(testObject);
            isTestPassed = true;

        }catch(Exception e){
            isTestPassed = false;
            System.out.println(e.getCause().toString());
        }finally {
            try{
                for(Method afterMethod : afterMethods) {
                    afterMethod.invoke(testObject);
                }
                isPassed = isBeforePassed && isTestPassed;

            }catch(Exception e){
                isPassed = false;
                System.out.println(e.getCause().toString());
            }
        }
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

}
