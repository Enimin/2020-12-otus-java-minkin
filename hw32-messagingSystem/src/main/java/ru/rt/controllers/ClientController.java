package ru.rt.controllers;

import com.google.gson.Gson;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.rt.crm.model.Client;
import ru.rt.crm.repository.ClientRepository;

@Controller
public class ClientController {
    private final ClientRepository clientRepository;

    public ClientController(ClientRepository dbServiceClient) {
        this.clientRepository = dbServiceClient;
    }

    @GetMapping("/")
    public String getIndex(Model model){
        return "clients";
    }

    @MessageMapping("/clients")
    @SendTo("/topic/response")
    public Iterable<Client> getClients(){
        return this.clientRepository.findAll();
    }

    @MessageMapping("/client")
    @SendTo("/topic/response")
    public Iterable<Client> saveClient(String jsonClient){
        var client = new Gson().fromJson(jsonClient, Client.class);
        this.clientRepository.save(client);
        return getClients();
    }

}
