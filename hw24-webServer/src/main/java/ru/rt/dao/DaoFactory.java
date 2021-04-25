package ru.rt.dao;

import org.hibernate.cfg.Configuration;
import ru.rt.core.repository.HibernateUtils;
import ru.rt.core.sessionmanager.TransactionManagerHibernate;
import ru.rt.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.rt.crm.model.AddressDataSet;
import ru.rt.crm.model.Client;
import ru.rt.crm.model.PhoneDataSet;
import ru.rt.crm.model.Users;

public class DaoFactory {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    public final TransactionManagerHibernate transactionManager;

    private UserDao userDao = null;
    private Dao<Client> clientDao = null;


    public DaoFactory() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                                                                             Client.class,
                                                                             AddressDataSet.class,
                                                                             PhoneDataSet.class,
                                                                             Users.class);

        transactionManager = new TransactionManagerHibernate(sessionFactory);
    }

    public UserDao getUserDao(){
        if (userDao == null){
            userDao = new UserDaoImpl(transactionManager);
        }
        return userDao;
    }
    public Dao<Client> getClientDao(){
        if (clientDao == null){
            clientDao = new ClientDaoImpl(transactionManager);
        }
        return clientDao;
    }
}
