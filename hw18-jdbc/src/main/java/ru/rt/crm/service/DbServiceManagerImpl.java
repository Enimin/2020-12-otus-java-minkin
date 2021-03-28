package ru.rt.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rt.core.repository.DataTemplate;
import ru.rt.core.sessionmanager.TransactionManager;
import ru.rt.crm.model.Manager;

import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DBServiceManager{
    private static final Logger log = LoggerFactory.getLogger(ru.rt.crm.service.DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceManagerImpl(TransactionManager transactionManager, DataTemplate<Manager> managerDataTemplate) {
        this.transactionManager = transactionManager;
        this.managerDataTemplate = managerDataTemplate;
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionManager.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerId = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerId, manager.getName());
                log.info("created manager: {}", createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long id) {
        return transactionManager.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, id);
            log.info("manager: {}", managerOptional);
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionManager.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
        });
    }
}
