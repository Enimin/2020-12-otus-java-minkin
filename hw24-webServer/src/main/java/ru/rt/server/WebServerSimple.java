package ru.rt.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.rt.crm.service.DBServiceClient;
import ru.rt.crm.service.DBServiceUser;
import ru.rt.helpers.FileSystemHelper;
import ru.rt.services.TemplateProcessor;
import ru.rt.servlet.ClientServlet;
import ru.rt.servlet.UserServlet;


public class WebServerSimple implements WebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "webapp";

    private final DBServiceClient dbServiceClient;
    private final DBServiceUser dbServiceUser;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public WebServerSimple(int port, DBServiceClient dbServiceClient, DBServiceUser dbServiceUser, TemplateProcessor templateProcessor) {
        server = new Server(port);
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/clients", "/users"));

        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String ...paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientServlet(templateProcessor, dbServiceClient)), "/clients");
        servletContextHandler.addServlet(new ServletHolder(new UserServlet(templateProcessor)), "/users");
        return servletContextHandler;
    }
}
