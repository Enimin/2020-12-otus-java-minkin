package ru.rt.killBoss.model;

public class RangeMessage {
    private int firstValue;
    private int lastValue;

    public RangeMessage(int firstValue, int lastValue) {
        this.firstValue = firstValue;
        this.lastValue = lastValue;
    }

    public int getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(int firstValue) {
        this.firstValue = firstValue;
    }

    public int getLastValue() {
        return lastValue;
    }

    public void setLastValue(int lastValue) {
        this.lastValue = lastValue;
    }
}
