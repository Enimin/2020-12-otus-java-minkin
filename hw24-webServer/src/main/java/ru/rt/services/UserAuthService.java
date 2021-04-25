package ru.rt.services;

import ru.rt.crm.model.Users;

public interface UserAuthService {
    Users getUser();
    boolean authenticate(String login, String password);
}
