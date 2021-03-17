package ru.rt.handler;

import ru.rt.model.Message;
import ru.rt.listener.Listener;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);
    void removeListener(Listener listener);
}
