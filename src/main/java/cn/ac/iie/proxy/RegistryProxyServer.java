package cn.ac.iie.proxy;

import cn.ac.iie.di.commons.httpserver.framework.server.HttpServer;
import cn.ac.iie.handler.DockerImageHandler;
import org.apache.log4j.Logger;

/**
 *  docker registry http proxy service
 *
 * @author Fighter Created on 2018/9/26.
 */
public class RegistryProxyServer {
    private static final String ROOT_CONTEXT_URI = "/";
    private static final Logger LOGGER = Logger.getLogger(DockerImageHandler.class);
    HttpServer server;



    public void stop(){

    }

}
