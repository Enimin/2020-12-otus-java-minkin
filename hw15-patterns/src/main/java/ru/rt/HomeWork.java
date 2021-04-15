package ru.rt;

import ru.rt.handler.ComplexProcessor;
import ru.rt.listener.homework.ListenerHistory;
import ru.rt.listener.homework.MessageHistory;
import ru.rt.model.Message;
import ru.rt.model.ObjectForMessage;
import ru.rt.processor.homework.ProcessorSwapValuesOfFields11_12;
import ru.rt.processor.homework.ProcessorTimer;

import java.time.Clock;
import java.time.ZoneId;
import java.util.List;

public class HomeWork {

    public static void main(String[] args) {
        var processors = List.of(new ProcessorSwapValuesOfFields11_12(),
                new ProcessorTimer(Clock.system(ZoneId.systemDefault())));

        var storage = new MessageHistory();
        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            System.out.println("timer: second is even");
        });
        var listenerPrinter = new ListenerHistory(storage);
        complexProcessor.addListener(listenerPrinter);

        var field13Value = new ObjectForMessage();
        field13Value.setData(List.of("field13_1","field13_2","field13_3"));
        var message = new Message.Builder(1L)
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(field13Value)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result: " + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
