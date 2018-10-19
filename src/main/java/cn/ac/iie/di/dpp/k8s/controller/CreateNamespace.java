/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ac.iie.di.dpp.k8s.controller;

import static cn.ac.iie.ProxyMain.api;
import static cn.ac.iie.ProxyMain.k8sUtil;
import cn.ac.iie.di.commons.httpserver.framework.handler.HandlerI;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1ResourceQuota;
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
public class CreateNamespace implements HandlerI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNamespace.class);

    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, String[]> paramterMap = request.getParameterMap();

            String namespaceName = paramterMap.get("namespaceName")[0];
            long podLimit = Long.parseLong(paramterMap.get("podLimit")[0]);
            double cpuRequest = Double.parseDouble(paramterMap.get("cpuRequest")[0]);
            long memoryRequest = Long.parseLong(paramterMap.get("memoryRequest")[0]);
            double cpuLimit = Double.parseDouble(paramterMap.get("cpuLimit")[0]);
            long memoryLimit = Long.parseLong(paramterMap.get("memoryLimit")[0]);

            LOGGER.info("Command is CreateNamespace and spec is \n"
                    + "namespaceName:" + namespaceName + "\n"
                    + "podLimit:" + podLimit + "\n"
                    + "memoryRequest:" + memoryRequest + "\n"
                    + "cpuLimit:" + cpuLimit + "\n"
                    + "memoryLimit:" + memoryLimit);

            V1Namespace myN = k8sUtil.CreateNameSpace(api, namespaceName);
            LOGGER.info("Namespace " + myN.getMetadata().getName() + " is created.\n" + myN);

            V1ResourceQuota myRQ = k8sUtil.CreateResourceQuota(api, namespaceName, podLimit, cpuRequest, memoryRequest, cpuLimit, memoryLimit);
            LOGGER.info("ResourceQuota " + myRQ.getMetadata().getName() + " is created.\n" + myRQ);

            String answer = new StringBuilder().append(myN.getMetadata().getName()).toString();
            
            response.getWriter().print(answer);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
        } catch (Exception e) {
            LOGGER.error("server error! {}", ExceptionUtils.getFullStackTrace(e));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "request failed.Because " + e.getMessage());
        }
    }
}
