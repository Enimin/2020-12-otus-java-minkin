package ru.rt;

import ru.rt.annotations.*;

public class ClassTest {

    @Before
    void beforeMethod(){
        System.out.println();
        System.out.println("exec beforeMethod");
    }

    @After
    void afterMethod(){
        System.out.println("exec afterMethod");
    }

    @Test
    void testMethod1() throws Exception {
        System.out.println("exec testMethod1");
        throw new java.lang.Error("TEST EXCEPTION!");
    }

    @Test
    void testMethod2(){
        System.out.println("exec testMethod2");
    }

}
