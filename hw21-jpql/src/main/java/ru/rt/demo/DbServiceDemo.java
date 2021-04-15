package ru.rt.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rt.core.repository.DataTemplateHibernate;
import ru.rt.core.repository.HibernateUtils;
import ru.rt.core.sessionmanager.TransactionManagerHibernate;
import ru.rt.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.rt.crm.model.AddressDataSet;
import ru.rt.crm.model.Client;
import ru.rt.crm.model.PhoneDataSet;
import ru.rt.crm.service.DbServiceClientImpl;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                                                                             Client.class,
                                                                             AddressDataSet.class,
                                                                             PhoneDataSet.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        var clientFirstData = new Client("dbServiceFirst");
        clientFirstData.setAddress(new AddressDataSet("First street"));
        clientFirstData.addPhone(new PhoneDataSet("123456"));
        clientFirstData.addPhone(new PhoneDataSet("789012"));
        dbServiceClient.saveClient(clientFirstData);

        var clientSecondData = new Client("dbServiceSecond");
        clientSecondData.setAddress(new AddressDataSet("Second street"));
        clientSecondData.addPhone(new PhoneDataSet("01-234-567"));

        var clientSecond = dbServiceClient.saveClient(clientSecondData);
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                                                         .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
///
        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated",
                                              clientSecondSelected.getAddress(),
                                              clientSecondSelected.getPhones()));
        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                                                  .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll()
                       .forEach(client -> log.info("client:{}", client));
    }
}
