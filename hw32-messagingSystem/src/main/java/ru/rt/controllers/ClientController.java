package ru.rt.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.rt.crm.model.Client;
import ru.rt.crm.repository.ClientRepository;
import ru.rt.requestDataTypes.ClientRequest;
import ru.rt.requestDataTypes.ClientRequestDT;
import ru.rt.resultsDT.ClientsResult;
import ru.rt.services.FrontendServiceImpl;

import java.io.IOException;
import java.util.Collections;

@Controller
public class ClientController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final FrontendServiceImpl frontendService;

    public ClientController(FrontendServiceImpl frontendService) {
        this.frontendService = frontendService;
    }

    @GetMapping("/")
    public String getIndex(Model model){
        return "clients";
    }

    @MessageMapping("/clients")
    @SendTo("/topic/response")
    public void getClients(@Header("simpSessionId") String sessionId){

        frontendService.sendMessage(ClientRequest.getAllClients(),
                                    response -> messagingTemplate.convertAndSendToUser(sessionId,
                                                                             "/topic/response",
                                                                                       response.getResult(),
                                                                                       createHeaders(sessionId)));
    }

    private MessageHeaders createHeaders(String sessionId) {
        var headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        return headerAccessor.getMessageHeaders();
    }

//    @MessageMapping("/client")
//    @SendTo("/topic/response")
//    public Iterable<Client> saveClient(String jsonClient){
//        var client = new Gson().fromJson(jsonClient, Client.class);
//        this.clientRepository.save(client);
//        return getClients();
//    }

}
