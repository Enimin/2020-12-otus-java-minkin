package ru.rt.killBoss;

import java.io.IOException;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        Thread serverThread = new Thread(
                () -> {
                    try {
                        GRPCServer.main(null);
                    } catch (InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        Thread clientThread = new Thread(
                () -> {
                    try {
                        GRPCClient.main(null);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        serverThread.start();
        Thread.currentThread().sleep(1000);
        clientThread.start();
    }
}
