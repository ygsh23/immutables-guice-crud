package com.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        try{
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(9000);

            File base = new File(System.getProperty("java.io.tmpdir"));
            tomcat.setBaseDir(base.getAbsolutePath());

            Context ctx = tomcat.addContext("", base.getAbsolutePath());

            Injector injector = Guice.createInjector(new UserModule());
            UserResource userResource = injector.getInstance(UserResource.class);

            ResourceConfig resourceConfig = new ResourceConfig();
            resourceConfig.register(userResource);
            ServletContainer servletContainer = new ServletContainer(resourceConfig);

            tomcat.addServlet(ctx, "jerseyServlet", servletContainer);
            ctx.addServletMappingDecoded("/api/*", "jerseyServlet");

            tomcat.start();
            System.out.println("Tomcat started on port " + tomcat.getConnector().getLocalPort());

            tomcat.getServer().await();
        } catch (Exception e) {
            log.error("Error starting Tomcat: {}", e.getMessage());
        }
    }
}