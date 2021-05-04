package ru.rt.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rt.cache.HwCache;
import ru.rt.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class CacheDbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(CacheDbServiceClientImpl.class);

    private final DBServiceClient dbServiceClient;
    private final HwCache<String, Client> cache;

    public CacheDbServiceClientImpl(DBServiceClient dbServiceClient, HwCache<String, Client> cache) {
        this.dbServiceClient = dbServiceClient;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        var dbClient = dbServiceClient.saveClient(client);
        cache.put(dbClient.getId().toString(), dbClient);
        return dbClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var client  = Optional.ofNullable(cache.get(String.valueOf(id)));
        if (!client.isPresent()){
            client = dbServiceClient.getClient(id);
            client.ifPresent(value -> cache.put(value.getId().toString(), value));
        }
        return client;
    }

    @Override
    public List<Client> findAll() {
        var clients = dbServiceClient.findAll();
        clients.forEach(client -> cache.put(client.getId().toString(), client));
        return clients;
    }
}
