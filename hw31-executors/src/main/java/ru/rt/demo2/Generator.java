package ru.rt.demo2;

public class Generator {

    private static final int RANGE_MIN = 1;
    private static final int RANGE_MAX = 10;

    private int currentNumber = 2;
    private int incrementValue = -1;


    public int getNextNumber(){
        if((currentNumber == RANGE_MAX) | (currentNumber == RANGE_MIN)){
            incrementValue = -incrementValue;
        }
        return currentNumber+=incrementValue;
    }
}
