package ru.rt;

public class TestFramework {

    public static void run(String fullClassTestName) throws ClassNotFoundException {
        Class<?> classTest = Class.forName(fullClassTestName);

        TestRunner<?> testRunner = new TestRunner<>(classTest);
        testRunner.runAllTests();
    }
}
