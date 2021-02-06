package ru.rt;

public class Demo {
    public static void main(String[] params){
        TestLogingInerface testLoging = TestInvocation.createMyClass();
        testLoging.calculation1(55);
        testLoging.calculation1("Test","OTUS");
        testLoging.calculation2(55, "OTUS");
        testLoging.calculation3(11, 99, "OTUS", false);
        testLoging.calculation4(1.0);
    }
}
