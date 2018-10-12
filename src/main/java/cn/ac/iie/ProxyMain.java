package cn.ac.iie;

import cn.ac.iie.common.Constants;
import cn.ac.iie.handler.Impl.KillHandlerImpl;
import cn.ac.iie.proxy.RegistryProxyServer;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fighter Created on 2018/9/26.
 */
public class ProxyMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyMain.class);
    private static final String PROPERTIES_PATH;
    public static FileBasedConfiguration conf;
    private static RegistryProxyServer service;

    static {
        PROPERTIES_PATH = ClassLoader.getSystemClassLoader()
                .getResource("configuration.properties").getFile();
        if (PROPERTIES_PATH == null || PROPERTIES_PATH.isEmpty()) {
            throw new RuntimeException("properties path is empty!");
        } else {
            LOGGER.info("PROPERTIES_PATH = {}", PROPERTIES_PATH);
        }

        try {
            conf = initConfiguration();
        } catch (ConfigurationException e) {
            LOGGER.error("init conf error: {}", ExceptionUtils.getFullStackTrace(e));
            System.exit(1);
        }

        try {
            service = new RegistryProxyServer(conf.getInt(Constants.JETTY_SERVER_PORT), conf.getInt(Constants.JETTY_SERVER_PARALLEL));
        } catch (Exception e) {
            LOGGER.error("jetty server start error: {}", ExceptionUtils.getFullStackTrace(e));
            System.exit(1);
        }
    }

    private static FileBasedConfiguration initConfiguration() throws ConfigurationException {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> confBuilder
                = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
                PropertiesConfiguration.class)
                .configure(params.properties()
                        .setFileName(PROPERTIES_PATH)
                        .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                        .setThrowExceptionOnMissing(true));
        return confBuilder.getConfiguration();
    }

    public static void main(String[] args) {
        try {
            //先注册java虚拟机关闭钩子,程序退出/System.exit()/crtl+c/系统关闭/OOM/kill pid
            new KillHandlerImpl(service).startShutdownHook();


            //启动代理服务
            service.start();

            LOGGER.info("registry service start success...");
        } catch (Exception e) {
            LOGGER.error("registry service start failed! {}", ExceptionUtils.getFullStackTrace(e));
        }

    }

}
