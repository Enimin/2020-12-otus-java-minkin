package ru.rt.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.rt.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UserServlet extends HttpServlet {

    private static final String PAGE_TEMPLATE = "users.html";
    private static final String USER_KEY = "user";

    private final TemplateProcessor templateProcessor;

    public UserServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(USER_KEY, req.getSession().getAttribute(USER_KEY));

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, paramsMap));
    }

}
