/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ac.iie.di.dpp.k8s.controller;

import cn.ac.iie.di.commons.httpserver.framework.handler.HandlerI;
import io.kubernetes.client.models.V1Status;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static cn.ac.iie.di.dpp.main.ProxyMain.*;
import cn.ac.iie.di.dpp.proxy.RegistryProxyServer;

/**
 *
 * @author Li Mingyang
 */
public class DeleteTask implements HandlerI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTask.class);

    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            RegistryProxyServer.count.incrementAndGet();
            LOGGER.info("counter: {}", RegistryProxyServer.count.get());

            Map<String, String[]> paramterMap = request.getParameterMap();

            String namespaceName = paramterMap.get("namespaceName")[0];
            String taskName = paramterMap.get("taskName")[0];

            k8sUtil.DeleteHorizontalPodAutoscaler(asV2Api, taskName, namespaceName);
            V1Status deleteServiceStatus = k8sUtil.DeleteService(api, taskName, namespaceName);
            V1Status deleteDeploymentStatus = k8sUtil.DeleteDeployment(appsV1Api, taskName, namespaceName);

            String answer = new StringBuilder().append(taskName).toString();

            RegistryProxyServer.count.decrementAndGet();
            LOGGER.info("counter: {}", RegistryProxyServer.count.get());

            response.getWriter().print(answer);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
        } catch (Exception e) {
            RegistryProxyServer.count.decrementAndGet();
            LOGGER.info("counter: {}", RegistryProxyServer.count.get());
            LOGGER.error("server error! {}", ExceptionUtils.getFullStackTrace(e));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "request failed.Because " + e.getMessage());
        }
    }
}
