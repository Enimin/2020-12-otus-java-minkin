package ru.rt;

public class ExampleRunTests {

    private static final String fullClassName = "ru.rt.ClassTest";

    public static void main(String[] args){
        try {
            TestFramework.run(fullClassName);
        } catch (ClassNotFoundException e) {
            System.err.println("Class " + fullClassName + " not found!");
        }
    }
}
