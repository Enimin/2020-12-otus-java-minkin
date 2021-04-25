package ru.rt.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.rt.dao.DaoFactory;
import ru.rt.services.TemplateProcessor;
import ru.rt.services.UserAuthService;
import ru.rt.servlet.AuthorizationFilter;
import ru.rt.servlet.LoginServlet;

import java.util.Arrays;

public class WebServerWithFilterBasedSecurity extends WebServerSimple {
    private final UserAuthService authService;

    public WebServerWithFilterBasedSecurity(int port,
                                            UserAuthService authService,
                                            DaoFactory daoFactory,
                                            TemplateProcessor templateProcessor) {
        super(port, daoFactory, templateProcessor);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
