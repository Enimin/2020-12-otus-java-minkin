package ru.rt;

import org.hibernate.cfg.Configuration;
import ru.rt.core.repository.DataTemplateHibernate;
import ru.rt.core.repository.HibernateUtils;
import ru.rt.core.sessionmanager.TransactionManagerHibernate;
import ru.rt.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.rt.crm.model.AddressDataSet;
import ru.rt.crm.model.Client;
import ru.rt.crm.model.PhoneDataSet;
import ru.rt.crm.model.Users;
import ru.rt.crm.service.DBServiceClient;
import ru.rt.crm.service.DBServiceUser;
import ru.rt.crm.service.DbServiceClientImpl;
import ru.rt.crm.service.DbServiceUserImpl;
import ru.rt.server.WebServer;
import ru.rt.server.WebServerWithFilterBasedSecurity;
import ru.rt.services.TemplateProcessor;
import ru.rt.services.TemplateProcessorImpl;
import ru.rt.services.UserAuthService;
import ru.rt.services.UserAuthServiceImpl;

public class ServerDemo {
    private static final int WEB_SERVER_PORT = 8081;
    private static final String TEMPLATES_DIR = "/webapp/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        migration(configuration);
        var transactionManager = getTransactionManager(configuration);
        var dbServiceUser = getDbServiceUser(transactionManager);
        var dbServiceClient = getDbServiceClient(transactionManager);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(dbServiceUser);

        WebServer webServer = new WebServerWithFilterBasedSecurity(WEB_SERVER_PORT, authService, dbServiceClient, dbServiceUser, templateProcessor);

        webServer.start();
        webServer.join();
    }

    private static void migration(Configuration configuration){
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
    }

    private static DBServiceClient getDbServiceClient(TransactionManagerHibernate transactionManager) {
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    private static DBServiceUser getDbServiceUser(TransactionManagerHibernate transactionManager) {
        var usersTemplate = new DataTemplateHibernate<>(Users.class);
        return new DbServiceUserImpl(transactionManager, usersTemplate);
    }

    private static TransactionManagerHibernate getTransactionManager(Configuration configuration) {
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                                                                            Client.class,
                                                                            AddressDataSet.class,
                                                                            PhoneDataSet.class,
                                                                            Users.class);
        return new TransactionManagerHibernate(sessionFactory);
    }

}
