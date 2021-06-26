package ru.rt.requestDataTypes;

import ru.otus.messagesystem.client.ResultDataType;
import ru.rt.crm.model.Client;
import ru.rt.crm.model.PhoneDataSet;
import ru.rt.dto.ClientDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ClientRequestDT extends ResultDataType {
    private final List<ClientDTO> clients;

    public ClientRequestDT(List<ClientDTO> clients) {
        this.clients = clients;
    }

    public ClientRequestDT toClientDTO (List<Client> clients){
        var clientsDTO = convertToDTO(clients);
        return new ClientRequestDT(clientsDTO);
    }

    private List<ClientDTO> convertToDTO(List<Client> clients){
        return clients.stream()
                      .map(client -> new ClientDTO(//client.getId(),
                                                   client.getName(),
                                                   client.getAddress().getStreet(),
                                                   client.getPhones()
                                                         .stream()
                                                         .map(PhoneDataSet::getNumber)
                                                         .collect(Collectors.joining("<br>"))))
                      .collect(Collectors.toList());
    }
}
