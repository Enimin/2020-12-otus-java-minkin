package ru.rt.killBoss.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rt.killBoss.GRPCClient;
import ru.rt.killBoss.generated.RemoteServiceGrpc;
import ru.rt.killBoss.generated.ResponseMessage;
import ru.rt.killBoss.generated.RangeMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RemoteServiceImpl extends RemoteServiceGrpc.RemoteServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);
    private static final int TIMER_SECONDS = 2;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private int currentValue = 0;
    private int lastValue = 0;
    private StreamObserver<ResponseMessage> responseObserver;

    @Override
    public void getValueOfRange(RangeMessage rangeMessage, StreamObserver<ResponseMessage> responseObserver){
        this.currentValue = rangeMessage.getFirstValue();
        this.lastValue = rangeMessage.getLastValue();
        this.responseObserver = responseObserver;

        executorService.scheduleAtFixedRate(sendNextNumber(),0,TIMER_SECONDS, TimeUnit.SECONDS);
    }

    private Runnable sendNextNumber(){
        return () -> {
            var responseMessage = ResponseMessage.newBuilder()
                                                                .setServerNumber(currentValue)
                                                                .build();
            responseObserver.onNext(responseMessage);

            currentValue++;

            if (currentValue > lastValue) {
                executorService.shutdown();
                responseObserver.onCompleted();
            }
        };
    }


}
