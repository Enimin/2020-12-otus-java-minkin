package ru.rt.killBoss.service;

import io.grpc.stub.StreamObserver;
import ru.rt.killBoss.generated.ResponseMessage;
import ru.rt.killBoss.model.RangeMessage;

public interface RemoteService {
    void getValueOfRange(RangeMessage rangeMessage, StreamObserver<ResponseMessage> responseMessage);
}
