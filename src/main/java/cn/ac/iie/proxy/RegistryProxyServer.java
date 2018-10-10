package cn.ac.iie.proxy;

import cn.ac.iie.di.commons.httpserver.framework.server.HttpServer;
import cn.ac.iie.proxy.controller.HelloController;
import org.apache.log4j.Logger;

/**
 * docker registry http proxy service
 *
 * @author Fighter Created on 2018/9/26.
 */
public class RegistryProxyServer {
    private static final String IMAGE_ROOT_CONTEXT_URI = "/image";
    private static final String REGISTRY_ROOT_CONTEXT_URI = "/registry";
    private static final Logger LOGGER = Logger.getLogger(RegistryProxyServer.class);
    HttpServer server;

    public RegistryProxyServer(int port, int parallel) throws Exception {
        server = new HttpServer("0.0.0.0", port, parallel);
    }

    public void start() throws Exception {
        server.registerContextHandler("/", "hello", HelloController::new);
        server.registerContextHandler("/", "hello/", HelloController::new);
/*
        server.registerContextHandler(IMAGE_ROOT_CONTEXT_URI, "image", ImageController::new);
        server.registerContextHandler(IMAGE_ROOT_CONTEXT_URI, "image/", ImageController::new);

        server.registerContextHandler(REGISTRY_ROOT_CONTEXT_URI, "registry", RegistryController::new);
        server.registerContextHandler(REGISTRY_ROOT_CONTEXT_URI, "registry/", RegistryController::new);*/

//        server.startup();
    }

    public void stop() throws Exception {
        server.shutdown();
    }
}
