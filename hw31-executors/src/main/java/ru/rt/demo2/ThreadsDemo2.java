package ru.rt.demo2;

import java.util.concurrent.TimeUnit;

public class ThreadsDemo2 {

    private String last = "Поток 2";

    public static void main(String[] args) {
        var threads = new ThreadsDemo2();

        new Thread(threads::action, "Поток 2").start();
        new Thread(threads::action, "Поток 1").start();
    }

    private synchronized void action() {
        var counter = new Counter();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (last.equals(Thread.currentThread().getName())) {
                    this.wait();
                }

                last = Thread.currentThread().getName();

                counter.showNextNumber();

                notifyAll();
                sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new NotInterestingException(e);
            }
        }
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
