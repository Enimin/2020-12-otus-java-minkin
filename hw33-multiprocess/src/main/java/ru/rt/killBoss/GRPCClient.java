package ru.rt.killBoss;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rt.killBoss.generated.RemoteServiceGrpc;
import ru.rt.killBoss.generated.RangeMessage;
import ru.rt.killBoss.generated.ResponseMessage;

import java.util.concurrent.*;

public class GRPCClient {
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int FIRST_VALUE = 0;
    private static final int LAST_VALUE = 30;
    private static final int CHECK_NUMBER_COUNT = 50;
    private static final int TIMER_SECONDS = 1;

    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static final ConcurrentSkipListMap<Integer, Boolean> serverNumbers = new ConcurrentSkipListMap<>();
    private static int currentValue = 0;
    private static int index = 0;

    public static void main(String[] args) throws InterruptedException {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var rangeMessage = RangeMessage.newBuilder().setFirstValue(FIRST_VALUE).setLastValue(LAST_VALUE).build();

        CountDownLatch latch = new CountDownLatch(1);

        logger.info("\n\n\nStart client\n\n");

        RemoteServiceGrpc.RemoteServiceStub newStub = RemoteServiceGrpc.newStub(channel);
        newStub.getValueOfRange(rangeMessage, new StreamObserver<ResponseMessage>() {
            @Override
            public void onNext(ResponseMessage responseMessage) {
                serverNumbers.put(responseMessage.getServerNumber(), false);
                logger.info("new value: {}", responseMessage.getServerNumber());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
            }

            @Override
            public void onCompleted() {
                logger.info("\n\n\nRequest completed\n\n");
                latch.countDown();
            }
        });

        logger.info("\n\n\nNumbers client is starting...\n\n");
        executorService.scheduleAtFixedRate(getCurrentValue(),TIMER_SECONDS,TIMER_SECONDS, TimeUnit.SECONDS);

        latch.await();

        channel.shutdown();
    }

    private static Runnable getCurrentValue(){
        return () -> {
           if (index <= CHECK_NUMBER_COUNT){
               var serverNumber = serverNumbers.lastKey();
               if (serverNumbers.get(serverNumber)){
                   serverNumber = 0;
               }
               else{
                   serverNumbers.put(serverNumber, true);
               }

               currentValue = currentValue + serverNumber + 1;

               logger.info("currentValue: {}", currentValue);
               index++;
           }
           else{
               executorService.shutdown();
           }
        };
    }
}
