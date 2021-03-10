package ru.rt;

import ru.rt.interfaces.IDisplaying;

class Display implements IDisplaying {

    public Display(){
    }

    @Override
    public void display(String message) {
        System.out.println(message);
    }
}
