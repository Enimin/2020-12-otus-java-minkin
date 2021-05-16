package ru.rt.demo1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ThreadsDemo {
    private static final Logger logger = LoggerFactory.getLogger(ThreadsDemo.class);

    private static final int RANGE_MIN = 1;
    private static final int RANGE_MAX = 10;

    private int currentNumber = 2;
    private int incrementValue = -1;
    private String last = "GET";

    public static void main(String[] args) {
        ThreadsDemo threads = new ThreadsDemo();
        new Thread(() -> threads.action("GET"), "Поток 2").start();
        new Thread(() -> threads.action("SET"), "Поток 1").start();
    }

    private synchronized void action(String message) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (last.equals(message)) {
                    this.wait();
                }

                last = message;

                if (message.equals("SET")) {
                    setCurrentNumber();
                }

                logger.info("{}: {}", Thread.currentThread().getName(), currentNumber);

                notifyAll();
                sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new NotInterestingException(e);
            }
        }
    }

    private void setCurrentNumber(){
        if((currentNumber == RANGE_MAX) | (currentNumber == RANGE_MIN)){
            incrementValue = -incrementValue;
        }
        currentNumber+=incrementValue;
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static class NotInterestingException extends RuntimeException {
        NotInterestingException(InterruptedException ex) {
            super(ex);
        }
    }
}
