package ru.rt.services;

import ru.rt.crm.model.Users;
import ru.rt.dao.UserDao;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;
    private Users user;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Users getUser() {
        return user;
    }

    @Override
    public boolean authenticate(String login, String password) {
        this.user = null;
        var dao = userDao.getByLogin(login);
        dao.ifPresent(user -> this.user = user);
        return dao.map(user -> user != null && user.getPassword().equals(password))
                  .orElse(false);
    }


}
