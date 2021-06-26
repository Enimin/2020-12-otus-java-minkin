package ru.rt.handlers;

import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.RequestHandler;
import ru.rt.crm.model.Client;
import ru.rt.crm.repository.ClientRepository;
import ru.rt.dto.ClientDTO;
import ru.rt.requestDataTypes.ClientRequestDT;
import ru.rt.resultsDT.ClientsResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class GetUserDataRequestHandler implements RequestHandler<ClientRequestDT> {
    private final ClientRepository clientRepository;

    public GetUserDataRequestHandler(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        ClientsResult clientRequestResult = MessageHelper.getPayload(msg);
        if (clientRequestResult.getAction().equals("FIND_ALL")){
            List<Client> clients = (List<Client>) clientRepository.findAll();
            return Optional.of(MessageBuilder.buildReplyMessage(msg, clientRequestResult.toClientDTO(clients)));
        }
        System.out.println("--------");
        System.out.println("not find all");
        System.out.println("--------");
        return Optional.of(MessageBuilder.buildReplyMessage(msg, new ClientsResult("HZ", new ArrayList<ClientDTO>())));
    }
}
