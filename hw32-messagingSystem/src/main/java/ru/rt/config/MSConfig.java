package ru.rt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;
import ru.rt.crm.repository.ClientRepository;
import ru.rt.handlers.ClientRequestHandler;
import ru.rt.handlers.ClientResponseHandler;
import ru.rt.services.DatabaseService;
import ru.rt.services.FrontendService;

@Configuration
public class MSConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean
    public MessageSystem getMessageSystem(){
        return new MessageSystemImpl();
    }

    @Bean
    public CallbackRegistry callbackRegistry(){
        return new CallbackRegistryImpl();
    }

    @Bean
    public DatabaseService getDatabaseService(CallbackRegistry callbackRegistry, MessageSystem messageSystem, ClientRepository clientRepository){
        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.USER_DATA, new ClientRequestHandler(clientRepository));
        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);

        return new DatabaseService(databaseMsClient, FRONTEND_SERVICE_CLIENT_NAME);
    }

    @Bean
    public FrontendService getFrontendService(CallbackRegistry callbackRegistry, MessageSystem messageSystem){
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.USER_DATA, new ClientResponseHandler(callbackRegistry));
        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerFrontendStore, callbackRegistry);
        messageSystem.addClient(frontendMsClient);

        return new FrontendService(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
    }
}
