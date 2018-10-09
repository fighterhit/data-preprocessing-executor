package cn.ac.iie.proxy.controller;

import cn.ac.iie.di.commons.httpserver.framework.handler.HandlerI;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Fighter Created on 2018/10/8.
 */
public class RegistryController implements HandlerI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryController.class);

    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}
