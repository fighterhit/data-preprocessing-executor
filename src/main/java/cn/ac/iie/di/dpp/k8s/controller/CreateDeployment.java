/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ac.iie.di.dpp.k8s.controller;

import static cn.ac.iie.ProxyMain.api;
import static cn.ac.iie.ProxyMain.asV2Api;
import static cn.ac.iie.ProxyMain.beta1api;
import static cn.ac.iie.ProxyMain.k8sUtil;
import cn.ac.iie.di.commons.httpserver.framework.handler.HandlerI;
import io.kubernetes.client.models.ExtensionsV1beta1Deployment;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V2beta1HorizontalPodAutoscaler;
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
public class CreateDeployment implements HandlerI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDeployment.class);

    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, String[]> paramterMap = request.getParameterMap();

            String namespaceName = paramterMap.get("namespaceName")[0];
            String deploymentName = paramterMap.get("deploymentName")[0];
            String image = paramterMap.get("image")[0];
            int replicaRequest = Integer.parseInt(paramterMap.get("replicaRequest")[0]);
            double podcpuRequest = Double.parseDouble(paramterMap.get("podcpuLimit")[0]);
            double podcpuLimit = Double.parseDouble(paramterMap.get("podcpuLimit")[0]);
            long podmemoryRequest = Long.parseLong(paramterMap.get("podmemoryRequest")[0]);
            long podmemoryLimit = Long.parseLong(paramterMap.get("podmemoryLimit")[0]);
            int containerPort = Integer.parseInt(paramterMap.get("containerPort")[0]);
            int replicaLimit = Integer.parseInt(paramterMap.get("replicaLimit")[0]);
            int podcpuThreshold = Integer.parseInt(paramterMap.get("podcpuThreshold")[0]);
            int podmemoryThreshold = Integer.parseInt(paramterMap.get("podmemoryThreshold")[0]);

            LOGGER.info("Command is CreateDeployment and spec is \n"
                    + "namespaceName:" + namespaceName + "\n"
                    + "deploymentName:" + deploymentName + "\n"
                    + "image:" + image + "\n"
                    + "replicaRequest:" + replicaRequest + "\n"
                    + "podcpuRequest:" + podcpuRequest + "\n"
                    + "podcpuLimit:" + podcpuLimit + "\n"
                    + "podmemoryRequest:" + podmemoryRequest + "\n"
                    + "podmemoryLimit:" + podmemoryLimit + "\n"
                    + "containerPort:" + containerPort + "\n"
                    + "replicaLimit:" + replicaLimit + "\n"
                    + "podcpuThreshold:" + podcpuThreshold + "\n"
                    + "podmemoryThreshold:" + podmemoryThreshold);

            ExtensionsV1beta1Deployment myD = k8sUtil.CreateDeployment(beta1api, namespaceName,
                    deploymentName, image, replicaRequest, podcpuRequest, podcpuLimit, podmemoryRequest, podmemoryLimit, containerPort);
            LOGGER.info("Deployment " + myD.getMetadata().getName() + " is created.\n" + myD);

            V1Service myS = k8sUtil.CreateService(api, namespaceName, deploymentName, containerPort);
            LOGGER.info("Service " + myS.getMetadata().getName() + " is created.\n" + myS);

            V2beta1HorizontalPodAutoscaler myHPA = k8sUtil.CreateHorizontalPodAutoscaler(asV2Api, deploymentName,
                    namespaceName, replicaRequest, replicaLimit, podcpuThreshold, podmemoryThreshold);
            LOGGER.info("HorizontalPodAutoscaler " + myHPA.getMetadata().getName() + " is created.\n" + myHPA);

            String answer = new StringBuilder().append(myS.getSpec().getPorts().get(0).getNodePort()).toString();

            response.getWriter().print(answer);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
        } catch (Exception e) {
            LOGGER.error("server error! {}", ExceptionUtils.getFullStackTrace(e));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "request failed.Because " + e.getMessage());
        }
    }
}
