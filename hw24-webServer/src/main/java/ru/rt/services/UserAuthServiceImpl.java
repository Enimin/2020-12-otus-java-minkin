package ru.rt.services;

import ru.rt.crm.model.Users;
import ru.rt.crm.service.DbServiceUserImpl;

public class UserAuthServiceImpl implements UserAuthService {

    private final DbServiceUserImpl dbServiceUser;
    private Users user;


    public UserAuthServiceImpl(DbServiceUserImpl dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Users getUser() {
        return user;
    }

    @Override
    public boolean authenticate(String login, String password) {
        this.user = null;
        var dbUser = dbServiceUser.getUserByLogin(login);
        dbUser.ifPresent(user -> this.user = user);
        return dbUser.map(user -> user != null && user.getPassword().equals(password))
                  .orElse(false);
    }


}
