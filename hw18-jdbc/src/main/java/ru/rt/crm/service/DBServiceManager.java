package ru.rt.crm.service;

import ru.rt.crm.model.Manager;

import java.util.List;
import java.util.Optional;

public interface DBServiceManager {

    Manager saveManager(Manager manager);

    Optional<Manager> getManager(long id);

    List<Manager> findAll();
}
