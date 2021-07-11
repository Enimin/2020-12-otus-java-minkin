package ru.rt;


import java.util.ArrayList;
import java.util.List;

class Benchmark implements BenchmarkMBean {
    private final double REMAINING_PART_PERCENT = 0.05;
    private final int loopCounter;
    private volatile int size = 0;

    private final List<String> array = new ArrayList<>();

    public Benchmark(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    void run() throws InterruptedException {
        for (int idx = 0; idx < loopCounter; idx++) {
            int local = size;
            for (int i = 0; i < local; i++) {
                array.add(new String(new char[10]));
            }
            array.subList((int) (array.size() * REMAINING_PART_PERCENT), array.size()).clear();
            Thread.sleep(100); //Label_1
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        System.out.println("new size:" + size);
        this.size = size;
    }
}
