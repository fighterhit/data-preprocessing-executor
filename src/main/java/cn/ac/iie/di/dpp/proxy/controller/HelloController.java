package cn.ac.iie.di.dpp.proxy.controller;

import cn.ac.iie.di.commons.httpserver.framework.handler.HandlerI;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Fighter Created on 2018/10/8.
 */
public class HelloController implements HandlerI {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Override
    public void execute(Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        LOGGER.info("Welcome, connected ...");
        httpServletResponse.getWriter().println("Welcome, connected ...");
    }
}
