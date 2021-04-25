package ru.rt;

import ru.rt.crm.model.AddressDataSet;
import ru.rt.crm.model.Client;
import ru.rt.crm.model.PhoneDataSet;
import ru.rt.crm.model.Users;
import ru.rt.dao.DaoFactory;
import ru.rt.server.WebServer;
import ru.rt.server.WebServerWithFilterBasedSecurity;
import ru.rt.services.TemplateProcessor;
import ru.rt.services.TemplateProcessorImpl;
import ru.rt.services.UserAuthService;
import ru.rt.services.UserAuthServiceImpl;

public class ServerDemo {
    private static final int WEB_SERVER_PORT = 8081;
    private static final String TEMPLATES_DIR = "/webapp/templates/";

    public static void main(String[] args) throws Exception {
        var daoFactory = new DaoFactory();

        dataPrepare(daoFactory);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(daoFactory.getUserDao());

        WebServer webServer = new WebServerWithFilterBasedSecurity(WEB_SERVER_PORT, authService, daoFactory, templateProcessor);

        webServer.start();
        webServer.join();
    }

    private static void dataPrepare(DaoFactory daoFactory){

        var user = daoFactory.getUserDao();
        var admin = new Users("admin", "admin");
        user.save(admin);
        var simpleUser = new Users("user", "1");
        user.save(simpleUser);

        var client = daoFactory.getClientDao();
        var firstClient = new Client("dbServiceFirst");
        firstClient.setAddress(new AddressDataSet("First street"));
        firstClient.setPhone(new PhoneDataSet("123456"));
        firstClient.setPhone(new PhoneDataSet("789012"));
        client.save(firstClient);

        var secondClient = new Client("dbServiceSecond");
        secondClient.setAddress(new AddressDataSet("Second street"));
        secondClient.setPhone(new PhoneDataSet("01-234-567"));
        client.save(secondClient);

    }
}
