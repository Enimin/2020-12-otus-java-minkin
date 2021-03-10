package ru.rt;

import ru.rt.interfaces.IMediator;

public class Component {
    IMediator atm;

    Component(IMediator atm) {
        this.atm = atm;
    }
}
