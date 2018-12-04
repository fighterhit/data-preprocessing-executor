package cn.ac.iie.di.dpp.proxy.controller;

import cn.ac.iie.di.commons.httpserver.framework.handler.HandlerI;
import cn.ac.iie.di.dpp.handler.Impl.RegistryHandlerImpl;
import cn.ac.iie.di.dpp.handler.RegistryHandler;
import cn.ac.iie.di.dpp.proxy.RegistryProxyServer;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fighter Created on 2018/10/8.
 */
public class RegistryController implements HandlerI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryController.class);
    private RegistryHandler RegistryHandler = new RegistryHandlerImpl();

    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
       try {
           //每收到一个请求 计数器 +1
           RegistryProxyServer.count.incrementAndGet();
           LOGGER.info("counter: {}", RegistryProxyServer.count.get());
           Map<String, String[]> paramMap = request.getParameterMap();
           String repositoryName = paramMap.get("repositoryName")[0];
           LOGGER.info("receive request: repositoryName {}", repositoryName);
           String tagsInfo = RegistryHandler.listTagsOfRepository(repositoryName);
           Map<String,Object> res = new HashMap<>();
           res.put("tags",tagsInfo);
           response.getWriter().print(JSON.toJSONString(res));
           response.setStatus(HttpServletResponse.SC_OK);
           response.getWriter().flush();
       }catch (Exception e){
           LOGGER.error("get images info error! {}", ExceptionUtils.getFullStackTrace(e));
           //请求返回时 计数器 -1
           RegistryProxyServer.count.decrementAndGet();
           LOGGER.info("counter: {}", RegistryProxyServer.count.get());

           Map errMsg = new HashMap();
           errMsg.put("code", 400);
           errMsg.put("msg", "get images info error!");
           response.setStatus(HttpServletResponse.SC_OK, "get images info error!");
           response.getWriter().print(JSON.toJSONString(errMsg));
       }
    }
}
