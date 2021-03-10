package ru.rt;

import ru.rt.interfaces.IStoraging;

class Cassette implements IStoraging {
    int denomination;
    int count;

    public Cassette(int denomination, int count){
        this.denomination = denomination;
        this.count = count;
    }

    @Override
    public int denomination() {
        return denomination;
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public void getNotes(int count) {
        this.count -= count;
    }

    @Override
    public void takeNote() {
        count ++;
    }
}
