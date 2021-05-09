package ru.rt.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.rt.crm.model.Users;
import ru.rt.services.UserAuthService;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class LoginServlet extends HttpServlet {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 300;
    private static final String ADMIN_LOGIN = "admin";
    private static final String USER_KEY = "user";


    private final UserAuthService userAuthService;

    public LoginServlet(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);
        var user = new Users(name, password);

        if (userAuthService.authenticate(name, password)) {
            var location = getLocation(name);
            HttpSession session = request.getSession();
            session.setAttribute(USER_KEY, user);
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect(location);
        } else {
            response.setStatus(SC_UNAUTHORIZED);
            response.sendRedirect("/");
        }

    }

    private String getLocation(String login) {
        var location = "/users";
        if (login.equals(ADMIN_LOGIN)){
            location = "/clients";
        }
        return location;
    }

}
