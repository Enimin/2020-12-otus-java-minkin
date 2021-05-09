package ru.rt.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.rt.crm.model.AddressDataSet;
import ru.rt.crm.model.Client;
import ru.rt.crm.model.PhoneDataSet;
import ru.rt.crm.service.DBServiceClient;
import ru.rt.services.TemplateProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientServlet extends HttpServlet {

    private static final String PAGE_TEMPLATE = "clients.html";
    private static final String CLIENT_KEY = "clients";
    private static final String USER_KEY = "user";

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;

    public ClientServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var clients = dbServiceClient.findAll();
        var user = request.getSession().getAttribute(USER_KEY);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, Map.of(CLIENT_KEY, clients, USER_KEY, user)));    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var client = createClient(request.getParameterMap());
        this.dbServiceClient.saveClient(client);
        response.sendRedirect("/clients");
    }

    private Client createClient(Map<String, String[]> params){
        var client = new Client(params.get("name")[0]);
        client.setAddress(new AddressDataSet(params.get("address")[0]));
        var phones = Arrays.stream(params.get("phones")).map(PhoneDataSet::new).collect(Collectors.toList());
        client.setPhones(phones);
        return client;
    }

}
