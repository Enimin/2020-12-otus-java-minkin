package ru.rt.interfaces;

import ru.rt.Component;
import ru.rt.Events;

public interface IMediator {
    void notify(Component sender, Events event);
}
