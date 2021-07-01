package ru.rt.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.rt.dto.ClientDTO;
import ru.rt.messages.Actions;
import ru.rt.messages.DataMessage;
import ru.rt.services.FrontendService;

import java.util.ArrayList;

@Controller
public class ClientController {

    private final SimpMessagingTemplate messagingTemplate;
    private final FrontendService frontendService;

    public ClientController(FrontendService frontendService, SimpMessagingTemplate messagingTemplate) {
        this.frontendService = frontendService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/")
    public String getIndex(Model model){
        return "clients";
    }

    @MessageMapping("/clients")
    public void getClients(@Header("simpSessionId") String sessionId){
        var dataMessageInstance = new DataMessage(Actions.FIND_ALL, null);
        frontendService.sendMessage(dataMessageInstance,
                                    dataMessage -> messagingTemplate.convertAndSend("/topic/response",
                                                                                    dataMessage.getAllClients()));
    }

    @MessageMapping("/client")
    public void saveClient(String jsonClient){
        var clientsDTO = new ArrayList<ClientDTO>();
        clientsDTO.add(new Gson().fromJson(jsonClient, ClientDTO.class));
        var dataMessageInstance = new DataMessage(Actions.SAVE, clientsDTO);
        frontendService.sendMessage(dataMessageInstance,
                                    dataMessage -> messagingTemplate.convertAndSend("/topic/response",
                                                                                    dataMessage.getAllClients()));
    }

}
