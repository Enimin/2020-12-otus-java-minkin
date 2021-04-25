package ru.rt.dao;

import ru.rt.core.repository.DataTemplateHibernate;
import ru.rt.core.sessionmanager.TransactionManager;
import ru.rt.crm.model.Users;
import ru.rt.crm.service.DbServiceUserImpl;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private final DbServiceUserImpl user;

    public UserDaoImpl(TransactionManager transactionManager) {
        var usersTemplate = new DataTemplateHibernate<>(Users.class);
        user = new DbServiceUserImpl(transactionManager, usersTemplate);
    }

    @Override
    public Optional<Users> get(Long id) {
        return user.getUser(id);
    }

    @Override
    public Users save(Users user) {
        return this.user.saveUser(user);
    }

    @Override
    public List<Users> findAll() {
        return null;
    }

    @Override
    public Optional<Users> getByLogin(String login) {
        return this.user.getUserByLogin(login);
    }
}
