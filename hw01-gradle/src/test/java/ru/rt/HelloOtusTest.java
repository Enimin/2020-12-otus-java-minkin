package ru.rt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloOtusTest {

    private static final String INPUTDATA = "Hello, ,Otus,,!,";
    private static final char SPLITTERSYMBOL = ',';
    private static final char JOINERSYMBOL = '_';
    private static final String ASSERTSTRING = "Hello_Otus_!";


    @Test
    void output(){
        String actualString = HelloOtus.joinWordsList(INPUTDATA,SPLITTERSYMBOL,JOINERSYMBOL);

        assertEquals(ASSERTSTRING, actualString);
    }
}