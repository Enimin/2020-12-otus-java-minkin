package ru.rt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.rt.crm.model.AddressDataSet;
import ru.rt.crm.model.Client;
import ru.rt.crm.model.PhoneDataSet;
import ru.rt.crm.repository.ClientRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ClientController {
    private final ClientRepository clientRepository;

    public ClientController(ClientRepository dbServiceClient) {
        this.clientRepository = dbServiceClient;
    }

    @GetMapping("/")
    public String getClients(Model model){
        model.addAttribute("client", new Client());
        model.addAttribute("clients", clientRepository.findAll());
        return "clients";
    }

    @GetMapping("/clients")
    public String toGetClients(Model model){
        return "redirect:/";
    }

    @PostMapping("/clients")
    public String saveClient(@RequestParam(value="name") String name, @RequestParam(value="address") String address, @RequestParam(value="phones") Set<String> phones){
        var clientAddress = new AddressDataSet(address);
        var clientPhones = phones.stream().map(PhoneDataSet::new).collect(Collectors.toSet());
        var client = new Client(name, clientAddress, clientPhones);
        this.clientRepository.save(client);
        return "redirect:/";
    }


}
