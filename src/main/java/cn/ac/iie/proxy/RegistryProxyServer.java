package cn.ac.iie.proxy;

import cn.ac.iie.ProxyMain;
import cn.ac.iie.common.Constants;
import cn.ac.iie.di.commons.httpserver.framework.server.HttpServer;
import cn.ac.iie.di.dpp.k8s.controller.CreateDeployment;
import cn.ac.iie.di.dpp.k8s.controller.CreateNamespace;
import cn.ac.iie.di.dpp.k8s.controller.DeleteDeployment;
import cn.ac.iie.di.dpp.k8s.controller.DeleteNamespace;
import cn.ac.iie.di.dpp.k8s.controller.ReplaceDeployment;
import cn.ac.iie.di.dpp.k8s.controller.ReplaceNamespace;
import cn.ac.iie.proxy.controller.HelloController;
import cn.ac.iie.proxy.controller.PushImageController;
import org.apache.log4j.Logger;

/**
 * docker registry http proxy service
 *
 * @author Fighter Created on 2018/9/26.
 */
public class RegistryProxyServer {

    private static final String IMAGE_ROOT_CONTEXT_URI = ProxyMain.conf.getString(Constants.JETTY_SERVER_IMAGE_ROOT_CONTEXT_URI);
    private static final String REGISTRY_ROOT_CONTEXT_URI = ProxyMain.conf.getString(Constants.JETTY_SERVER_REGISTRY_ROOT_CONTEXT_URI);
    private static final String REGISTRY_K8S_CONTEXT_URI = ProxyMain.conf.getString(Constants.JETTY_SERVER_K8S_ROOT_CONTEXT_URI);
    private static final Logger LOGGER = Logger.getLogger(RegistryProxyServer.class);
    HttpServer server;

    public RegistryProxyServer(int port, int parallel) throws Exception {
        server = new HttpServer("0.0.0.0", port, parallel);
    }

    public void start() throws Exception {
        server.registerContext(REGISTRY_K8S_CONTEXT_URI);
//        server.registerContextHandler(IMAGE_ROOT_CONTEXT_URI, "hello", HelloController::new);
//        server.registerContextHandler(REGISTRY_ROOT_CONTEXT_URI, "hello/", HelloController::new);
//
//        server.registerContextHandler(IMAGE_ROOT_CONTEXT_URI, "push", PushImageController::new);
//        server.registerContextHandler(IMAGE_ROOT_CONTEXT_URI, "push/", PushImageController::new);

        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/createNamespace", CreateNamespace::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/createNamespace/", CreateNamespace::new);
        
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/createDeployment", CreateDeployment::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/createDeployment/", CreateDeployment::new);
        
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/deleteNamespace", DeleteNamespace::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/deleteNamespace/", DeleteNamespace::new);
        
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/deleteDeployment", DeleteDeployment::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/deleteDeployment/", DeleteDeployment::new);
        
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/replaceNamespace", ReplaceNamespace::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/replaceNamespace/", ReplaceNamespace::new);
        
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/replaceDeployment", ReplaceDeployment::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/replaceDeployment/", ReplaceDeployment::new);

//        server.registerContextHandler(REGISTRY_ROOT_CONTEXT_URI, "registry", RegistryController::new);
//        server.registerContextHandler(REGISTRY_ROOT_CONTEXT_URI, "registry/", RegistryController::new);
        server.startup();
    }

    public void stop() throws Exception {
        server.shutdown();
    }
}
