package ru.rt.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rt.core.repository.DataTemplate;
import ru.rt.core.sessionmanager.TransactionManager;
import ru.rt.crm.model.Users;

import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger log = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final DataTemplate<Users> userDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceUserImpl(TransactionManager transactionManager, DataTemplate<Users> userDataTemplate) {
        this.transactionManager = transactionManager;
        this.userDataTemplate = userDataTemplate;
    }

    @Override
    public Users saveUser(Users user) {
        return transactionManager.doInTransaction(session -> {
            var userCloned = user.clone();
            if (user.getId() == null) {
                userDataTemplate.insert(session, userCloned);
                return userCloned;
            }
            userDataTemplate.update(session, userCloned);
            return userCloned;
        });
    }

    @Override
    public Optional<Users> getUser(long id) {
        return transactionManager.doInTransaction(session -> {
            var clientOptional = userDataTemplate.findById(session, id);
            return clientOptional;
        });
    }

    @Override
    public Optional<Users> getUserByLogin(String login) {
        return transactionManager.doInTransaction(session -> {
            var results = userDataTemplate.findByField(session, "login", login);
            return  results.stream().findFirst();
        });
    }

}
