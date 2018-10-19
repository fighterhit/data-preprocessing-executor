/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ac.iie.di.dpp.k8s.controller;

import static cn.ac.iie.ProxyMain.api;
import static cn.ac.iie.ProxyMain.appsV1Api;
import static cn.ac.iie.ProxyMain.asV2Api;
import static cn.ac.iie.ProxyMain.k8sUtil;
import cn.ac.iie.di.commons.httpserver.framework.handler.HandlerI;
import io.kubernetes.client.models.V1Status;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Li Mingyang
 */
public class DeleteDeployment implements HandlerI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDeployment.class);

    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, String[]> paramterMap = request.getParameterMap();

            String namespaceName = paramterMap.get("namespaceName")[0];
            String deploymentName = paramterMap.get("deploymentName")[0];

            k8sUtil.DeleteHorizontalPodAutoscaler(asV2Api, deploymentName, namespaceName);
            V1Status deleteServiceStatus = k8sUtil.DeleteService(api, deploymentName, namespaceName);
            V1Status deleteDeploymentStatus = k8sUtil.DeleteDeployment(appsV1Api, deploymentName, namespaceName);

            String answer = new StringBuilder().append(deploymentName).toString();
            response.getWriter().print(answer);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
        } catch (Exception e) {
            LOGGER.error("server error! {}", ExceptionUtils.getFullStackTrace(e));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "request failed.Because " + e.getMessage());
        }
    }
}
