package ru.rt.dao;

import ru.rt.crm.model.Users;

import java.util.Optional;

public interface UserDao extends Dao<Users>{

    Optional<Users> getByLogin(String login);
}