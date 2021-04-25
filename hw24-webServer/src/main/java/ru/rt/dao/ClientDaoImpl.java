package ru.rt.dao;

import ru.rt.core.repository.DataTemplateHibernate;
import ru.rt.core.sessionmanager.TransactionManager;
import ru.rt.crm.model.Client;
import ru.rt.crm.service.DbServiceClientImpl;

import java.util.List;
import java.util.Optional;

public class ClientDaoImpl implements Dao<Client> {

    private final DbServiceClientImpl client;

    public ClientDaoImpl(TransactionManager transactionManager) {
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        client = new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    @Override
    public Optional<Client> get(Long id) {
        return client.getClient(id);
    }

    @Override
    public Client save(Client client) {
        return this.client.saveClient(client);
    }

    @Override
    public List<Client> findAll() {
        return this.client.findAll();
    }


}
