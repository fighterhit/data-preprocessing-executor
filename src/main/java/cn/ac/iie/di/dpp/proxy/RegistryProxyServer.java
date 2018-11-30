package cn.ac.iie.di.dpp.proxy;

import cn.ac.iie.di.commons.httpserver.framework.exception.SFStatException;
import cn.ac.iie.di.commons.httpserver.framework.server.HttpServer;
import cn.ac.iie.di.dpp.common.Constants;
import cn.ac.iie.di.dpp.k8s.controller.*;
import cn.ac.iie.di.dpp.main.ProxyMain;
import cn.ac.iie.di.dpp.proxy.controller.DeleteImageController;
import cn.ac.iie.di.dpp.proxy.controller.HelloController;
import cn.ac.iie.di.dpp.proxy.controller.PushImageController;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * docker registry http proxy service
 *
 * @author Fighter Created on 2018/9/26.
 */
public class RegistryProxyServer {

    private static final String IMAGE_ROOT_CONTEXT_URI = ProxyMain.conf.getString(Constants.JETTY_SERVER_IMAGE_ROOT_CONTEXT_URI);
    private static final String REGISTRY_ROOT_CONTEXT_URI = ProxyMain.conf.getString(Constants.JETTY_SERVER_REGISTRY_ROOT_CONTEXT_URI);
    private static final String REGISTRY_K8S_CONTEXT_URI = ProxyMain.conf.getString(Constants.JETTY_SERVER_K8S_ROOT_CONTEXT_URI);
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryProxyServer.class);
    HttpServer server;

    //请求计数器
    public static AtomicInteger count = new AtomicInteger(0);

    public RegistryProxyServer(int port, int parallel) throws Exception {
        server = new HttpServer("0.0.0.0", port, parallel);
    }

    public void start() throws Exception {
        server.registerContext("/");

        server.registerContextHandler(IMAGE_ROOT_CONTEXT_URI, "/hello", HelloController::new);
        server.registerContextHandler(REGISTRY_ROOT_CONTEXT_URI, "/hello/", HelloController::new);

        //上传镜像
        server.registerContextHandler(IMAGE_ROOT_CONTEXT_URI, "/push", PushImageController::new);
        server.registerContextHandler(IMAGE_ROOT_CONTEXT_URI, "/push/", PushImageController::new);

        //查询镜像
        server.registerContextHandler(REGISTRY_ROOT_CONTEXT_URI, "/delete", DeleteImageController::new);
        server.registerContextHandler(REGISTRY_ROOT_CONTEXT_URI, "/delete/", DeleteImageController::new);


        //k8s
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/createNamespace", CreateNamespace::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/createNamespace/", CreateNamespace::new);

        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/createDeployment", CreateDeployment::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/createDeployment/", CreateDeployment::new);

        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/createTask", CreateTask::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/createTask/", CreateTask::new);

        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/deleteNamespace", DeleteNamespace::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/deleteNamespace/", DeleteNamespace::new);

        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/deleteDeployment", DeleteDeployment::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/deleteDeployment/", DeleteDeployment::new);

        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/deleteTask", DeleteTask::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/deleteTask/", DeleteTask::new);

        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/replaceNamespace", ReplaceNamespace::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/replaceNamespace/", ReplaceNamespace::new);

        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/replaceDeployment", ReplaceDeployment::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/replaceDeployment/", ReplaceDeployment::new);

        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/replaceTask", ReplaceTask::new);
        server.registerContextHandler(REGISTRY_K8S_CONTEXT_URI, "/replaceTask/", ReplaceTask::new);

        server.startup();

        server.toString();
    }

    public void stop() {
        int i = 0;
        try {
            //先停http服务
            server.shutdown();
            //若计数器不为0，循环检测3次，最长等待15s
            for (; i < 3; i++) {
                if (count.get() != 0) {
                    LOGGER.info("sleeping {} for wait...", i + 1);
                    Thread.sleep(5000);
                } else {
                    break;
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error("Thread sleep error! {}", ExceptionUtils.getStackTrace(e));
        } catch (SFStatException e) {
            LOGGER.error("server stop failed! {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (i == 3) {
                LOGGER.error("server stop failed because of unfinished task!");
            }
        }
    }
}
