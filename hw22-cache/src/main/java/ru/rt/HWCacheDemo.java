package ru.rt;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rt.cache.HwCache;
import ru.rt.cache.HwListener;
import ru.rt.cache.MyCache;
import ru.rt.core.repository.DataTemplateHibernate;
import ru.rt.core.repository.HibernateUtils;
import ru.rt.core.sessionmanager.TransactionManagerHibernate;
import ru.rt.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.rt.crm.model.AddressDataSet;
import ru.rt.crm.model.Client;
import ru.rt.crm.model.PhoneDataSet;
import ru.rt.crm.service.CacheDbServiceClientImpl;
import ru.rt.crm.service.DBServiceClient;
import ru.rt.crm.service.DbServiceClientImpl;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
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
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        HwCache<String, Client> cache = new MyCache<>();

        HwListener<String, Client> listener = new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                logger.info("[CACHE] action: {}, key:{}, value:{}", action, key, value);
            }
        };
        cache.addListener(listener);

        logger.info("[TEST] Without cache --------------------");
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        startExecutionTest(dbServiceClient);

        logger.info("[TEST] With cache --------------------");
        var cacheDbServiceClient = new CacheDbServiceClientImpl(dbServiceClient, cache);
        startExecutionTest(cacheDbServiceClient);
    }

    private void startExecutionTest(DBServiceClient dbServiceClient){
        long startTime = System.currentTimeMillis();
        dbServiceClient.findAll().forEach(tempClient ->{
            var client = dbServiceClient.getClient(tempClient.getId());
        });
        long estimatedTime = System.currentTimeMillis() - startTime;
        logger.info("[TEST] Time elapsed: {}ms --------------------", estimatedTime);
    }
}
