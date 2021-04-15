package ru.rt.processor.homework;

import ru.rt.model.Message;
import ru.rt.processor.Processor;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;

public class ProcessorTimer implements Processor {
    private final Clock currTime;

    public ProcessorTimer(Clock currTime){
        this.currTime = currTime;
    }

    @Override
    public Message process(Message message) {
        var seconds = currTime.instant().getEpochSecond();
        if ((seconds & 1) == 0) {
            var formatDate = new SimpleDateFormat("ss");
            throw new SecondIsEven("Четная секунда: " + formatDate.format(Date.from(currTime.instant())));
        }
        return message.toBuilder().build();
    }
}
