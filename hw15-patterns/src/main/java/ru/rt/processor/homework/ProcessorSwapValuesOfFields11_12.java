package ru.rt.processor.homework;

import ru.rt.model.Message;
import ru.rt.processor.Processor;

public class ProcessorSwapValuesOfFields11_12 implements Processor {

    @Override
    public Message process(Message message) {
        return message.toBuilder()
                      .field11(message.getField12())
                      .field12(message.getField11())
                      .build();
    }
}