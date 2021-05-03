package ru.rt.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.rt.crm.service.DbServiceClientImpl;
import ru.rt.crm.service.DbServiceUserImpl;
import ru.rt.services.TemplateProcessor;
import ru.rt.services.UserAuthService;
import ru.rt.servlet.AuthorizationFilter;
import ru.rt.servlet.LoginServlet;

import java.util.Arrays;

public class WebServerWithFilterBasedSecurity extends WebServerSimple {
    private final UserAuthService authService;

    public WebServerWithFilterBasedSecurity(int port,
                                            UserAuthService authService,
                                            DbServiceClientImpl dbServiceClient,
                                            DbServiceUserImpl dbServiceUser,
                                            TemplateProcessor templateProcessor) {
        super(port, dbServiceClient, dbServiceUser, templateProcessor);
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
