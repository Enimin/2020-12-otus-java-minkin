package ru.rt.resultsDT;

import ru.otus.messagesystem.client.ResultDataType;
import ru.rt.crm.model.Client;
import ru.rt.crm.model.PhoneDataSet;
import ru.rt.dto.ClientDTO;
import ru.rt.requestDataTypes.ClientRequestDT;

import java.util.List;
import java.util.stream.Collectors;

public class ClientsResult extends ResultDataType{

    private final String action;
    private List<ClientDTO> clients;

    public ClientsResult(String action, List<ClientDTO> clients) {
        this.action = action;
        this.clients = clients;
    }

 //   public ClientsResult(String action) {
 //       this.action = action;
 //   }

    public String getAction() {
        return action;
    }

    public List<ClientDTO> getResult() {
        return clients;
    }

    public ClientsResult toClientDTO (List<Client> clients){
        this.clients = convertToDTO(clients);
        return new ClientsResult("rFIND_ALL", this.clients);
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
