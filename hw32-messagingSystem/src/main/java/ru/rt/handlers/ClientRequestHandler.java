package ru.rt.handlers;

import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.RequestHandler;
import ru.rt.crm.model.Client;
import ru.rt.crm.repository.ClientRepository;
import ru.rt.messages.DataMessage;

import java.util.List;
import java.util.Optional;


public class ClientRequestHandler implements RequestHandler<DataMessage> {
    private final ClientRepository clientRepository;

    public ClientRequestHandler(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        DataMessage dataMessage = MessageHelper.getPayload(msg);
        if (dataMessage.getAction().equals("SAVE")){
            var client = dataMessage.getClient();
            clientRepository.save(client);
        }
        List<Client> clients = (List<Client>) clientRepository.findAll();
        dataMessage.setClients(clients);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, dataMessage));
    }
}
