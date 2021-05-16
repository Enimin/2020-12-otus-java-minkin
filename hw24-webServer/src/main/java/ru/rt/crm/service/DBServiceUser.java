package ru.rt.crm.service;

import ru.rt.crm.model.Users;

import java.util.Optional;

public interface DBServiceUser {

    Users saveUser(Users user);

    Optional<Users> getUser(long id);

    Optional<Users> getUserByLogin(String login);
}
