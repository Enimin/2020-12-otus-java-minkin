package ru.rt;

public class StackOfCash {
    private int denomination;
    private int count;

    public StackOfCash(int denomination, int count){
        this.denomination = denomination;
        this.count = count;
    }

    public int getDenomination() {
        return denomination;
    }

    public int getCount() {
        return count;
    }

}
