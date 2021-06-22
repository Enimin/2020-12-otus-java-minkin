package ru.rt.killBoss;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.rt.killBoss.service.RemoteServiceImpl;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(new RemoteServiceImpl())
                .build();
        server.start();

        System.out.println("Server waiting for client connections...");
        server.awaitTermination();
    }
}
