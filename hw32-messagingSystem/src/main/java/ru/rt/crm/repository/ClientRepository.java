package ru.rt.crm.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rt.crm.model.Client;


public interface ClientRepository extends CrudRepository<Client, Long> {
}
