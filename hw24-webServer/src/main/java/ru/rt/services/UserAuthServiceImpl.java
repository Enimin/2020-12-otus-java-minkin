package ru.rt.services;

import ru.rt.crm.service.DBServiceUser;

public class UserAuthServiceImpl implements UserAuthService {

    private final DBServiceUser dbServiceUser;


    public UserAuthServiceImpl(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public boolean authenticate(String login, String password) {
        var dbUser = dbServiceUser.getUserByLogin(login);
        return dbUser.map(user -> user != null && user.getPassword().equals(password))
                  .orElse(false);
    }


}
