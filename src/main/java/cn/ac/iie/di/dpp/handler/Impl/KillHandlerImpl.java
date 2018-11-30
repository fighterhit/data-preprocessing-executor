package cn.ac.iie.di.dpp.handler.Impl;

import cn.ac.iie.di.dpp.handler.KillHandler;
import cn.ac.iie.di.dpp.proxy.RegistryProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * KillHandler for clean operation
 *
 * @author Fighter Created on 2018/9/27.
 */
public class KillHandlerImpl implements KillHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KillHandlerImpl.class);
    RegistryProxyServer service;

    public KillHandlerImpl(RegistryProxyServer service) {
        this.service = service;
    }

    @Override
    public void startShutdownHook() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("program is stopping...");
            //some clean operation
            if (service != null) {
                try {
                    service.stop();
                } catch (Exception e) {
                    LOGGER.error("service stop error!");
                    System.exit(1);
                }
            }
            System.exit(0);
        }));

    }

}
