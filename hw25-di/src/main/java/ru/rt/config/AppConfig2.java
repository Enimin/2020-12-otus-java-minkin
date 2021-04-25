package ru.rt.config;

import ru.rt.appcontainer.api.AppComponent;
import ru.rt.appcontainer.api.AppComponentsContainerConfig;
import ru.rt.services.IOService;
import ru.rt.services.IOServiceConsole;

@AppComponentsContainerConfig(order = 0)
public class AppConfig2 {

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceConsole(System.out, System.in);
    }

}
