package ru.rt.demo2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rt.demo1.ThreadsDemo;

public class Counter {
    private static final Logger logger = LoggerFactory.getLogger(ThreadsDemo.class);
    private final Generator generator = new Generator();

    public void showNextNumber(){
        logger.info("{}: {}", Thread.currentThread().getName(), generator.getNextNumber());
    }

}
