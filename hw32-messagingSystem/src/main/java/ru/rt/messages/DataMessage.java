package ru.rt.messages;

import ru.otus.messagesystem.client.ResultDataType;
import ru.rt.crm.model.AddressDataSet;
import ru.rt.crm.model.Client;
import ru.rt.crm.model.PhoneDataSet;
import ru.rt.dto.ClientDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DataMessage extends ResultDataType {

    private final String action;
    private List<ClientDTO> clients;

    public DataMessage(String action, List<ClientDTO> clients) {
        this.action = action;
        this.clients = clients;
    }

    public String getAction() {
        return action;
    }

    public void setClients(List<Client> clients) {
        this.clients = convertToDTO(clients);
    }

    public Client getClient(){
        return convertFromDTO(clients).get(0);
    }

    public List<ClientDTO> getAllClients(){
        return clients;
    }

    private List<ClientDTO> convertToDTO(List<Client> clients){
        return clients.stream()
                .map(client -> {var clientDTO = new ClientDTO();
                                clientDTO.setId(client.getId());
                                clientDTO.setName(client.getName());
                                clientDTO.setAddress(client.getAddress().getStreet());
                                clientDTO.setPhones(client.getPhones()
                                                          .stream()
                                                          .map(PhoneDataSet::getNumber)
                                                          .collect(Collectors.toList()));
                                return clientDTO;})
                .collect(Collectors.toList());
    }

    private List<Client> convertFromDTO(List<ClientDTO> clients){
        return clients.stream()
                .map(clientDTO -> {var client = new Client();
                    client.setId(clientDTO.getId());
                    client.setName(clientDTO.getName());
                    client.setAddress(new AddressDataSet(clientDTO.getAddress()));
                    client.setPhones(clientDTO.getPhones()
                            .stream()
                            .map(PhoneDataSet::new)
                            .collect(Collectors.toSet()));
                    return client;})
                .collect(Collectors.toList());
    }

}
