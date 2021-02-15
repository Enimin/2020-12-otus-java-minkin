package ru.rt;

import ru.rt.annotations.Log;

public class TestLoging implements TestLogingInterface {

    @Log
    @Override
    public void calculation1(int param1){
    }

    @Log
    @Override
    public void calculation1(String param1, String pram2){
    }

    @Log
    @Override
    public void calculation2(Integer param2, String param3){
    }

    @Log
    @Override
    public void calculation3(int param1, Integer param2, String param3, boolean param4){
    }

    @Override
    public void calculation4(Double param1){
        System.out.println("calculation4 is not annotated @Log");
    }
}
