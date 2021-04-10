package ru.rt.handler;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rt.listener.Listener;
import ru.rt.listener.homework.ListenerHistory;
import ru.rt.listener.homework.MessageHistory;
import ru.rt.model.Message;
import ru.rt.model.ObjectForMessage;
import ru.rt.processor.Processor;
import ru.rt.processor.ProcessorUpperField10;
import ru.rt.processor.homework.ProcessorSwapValuesOfFields11_12;
import ru.rt.processor.homework.ProcessorTimer;
import ru.rt.processor.homework.SecondIsEven;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

class ComplexProcessorTest {

    @Test
    @DisplayName("Тестируем вызовы процессоров")
    void handleProcessorsTest() {
        //given
        var message = new Message.Builder(1L).field7("field7").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenReturn(message);

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
        });

        //when
        var result = complexProcessor.handle(message);

        //then
        verify(processor1).process(message);
        verify(processor2).process(message);
        assertThat(result).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем обработку исключения")
    void handleExceptionTest() {
        //given
        var message = new Message.Builder(1L).field8("field8").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenThrow(new RuntimeException("Test Exception"));

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        //when
        assertThatExceptionOfType(TestException.class).isThrownBy(() -> complexProcessor.handle(message));

        //then
        verify(processor1, times(1)).process(message);
        verify(processor2, never()).process(message);
    }

    @Test
    @DisplayName("Тестируем уведомления")
    void notifyTest() {
        //given
        var message = new Message.Builder(1L).field9("field9").build();

        var listener = mock(Listener.class);

        var complexProcessor = new ComplexProcessor(new ArrayList<>(), (ex) -> {
        });

        complexProcessor.addListener(listener);

        //when
        complexProcessor.handle(message);
        complexProcessor.removeListener(listener);
        complexProcessor.handle(message);

        //then
        verify(listener, times(1)).onUpdated(message, message);
    }

    @Test
    @DisplayName("Тестируем поля 11 и 12")
    void swapFieldsTest() {
        //given
        var message = new Message.Builder(1L).field11("field11").field12("field12").build();

        List<Processor> processors = List.of(new ProcessorSwapValuesOfFields11_12());

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});

        //when
        var result = complexProcessor.handle(message);

        //then
        assertEquals(result.getField11(), message.getField12());
        assertEquals(result.getField12(), message.getField11());
    }

    @Test
    @DisplayName("Тестируем таймер")
    void timerTest() {
        var message = new Message.Builder(1L).build();

        var oddClock = Clock.fixed(Instant.parse("2021-01-01T00:00:01.00Z"), ZoneId.systemDefault());
        List<Processor> oddProcessors = List.of(new ProcessorTimer(oddClock));
        var oddProcessor = new ComplexProcessor(oddProcessors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        assertThatCode(() -> oddProcessor.handle(message)).doesNotThrowAnyException();

        var evenClock = Clock.fixed(Instant.parse("2021-01-01T00:00:02.00Z"), ZoneId.systemDefault());
        List<Processor> evenProcessors = List.of(new ProcessorTimer(evenClock));
        Exception[] complexProcessorException = {new Exception()};
        var evenProcessor = new ComplexProcessor(evenProcessors, (ex) -> complexProcessorException[0] = ex);

        var newMessage = evenProcessor.handle(message);

        assertEquals(complexProcessorException[0].getClass(), SecondIsEven.class);
    }

    @Test
    @DisplayName("Тестируем историю")
    void historyTest() {
        //given
        var field13Value = new ObjectForMessage();
        field13Value.setData(List.of("a","b","c"));
        var message = new Message.Builder(1L).field4("field4").field10("field10").field13(field13Value).build();

        List<Processor> processors = List.of(new ProcessorUpperField10());
        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});

        var storage = new MessageHistory();
        var listener = new ListenerHistory(storage);
        complexProcessor.addListener(listener);

        var newMessage = complexProcessor.handle(message);

        field13Value.setData(List.of("x","y","z"));

        assertNotEquals(message.getField13().getData(), storage.getLastOldMsg().getField13().getData());
        assertEquals(storage.getLastOldMsg().getField13().getData(),List.of("a","b","c"));
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }
}