package cn.ac.iie.di.dpp.common;

import cn.ac.iie.di.dpp.main.ProxyMain;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

/**
 * docker client util
 *
 * @author Fighter Created on 2018/9/27.
 */
public class DockerConfig {

    private static DockerCmdExecFactory dockerCmdExecFactory() {
        return new JerseyDockerCmdExecFactory()
                .withReadTimeout(ProxyMain.conf.getInt(Constants.DOCKER_READ_TIMEOUT))
                .withConnectTimeout(ProxyMain.conf.getInt(Constants.DOCKER_CONNECT_TIMEOUT))
                .withMaxTotalConnections(ProxyMain.conf.getInt(Constants.DOCKER_MAX_TOTAL_CONNECTIONS))
                .withMaxPerRouteConnections(ProxyMain.conf.getInt(Constants.DOCKER_MAX_PER_ROUTE_CONNNECTIONS));
    }

    /**
     * create a DockerClientConfig
     *
     * @return
     */
    private static DockerClientConfig dockerClientConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(ProxyMain.conf.getString(Constants.DOCKER_HOST))
                .withDockerTlsVerify(false)
                .withRegistryUrl(ProxyMain.conf.getString(Constants.REGISTRY_URL))
                .withRegistryUsername(ProxyMain.conf.getString(Constants.REGISTRY_USERNAME))
                .withRegistryPassword(ProxyMain.conf.getString(Constants.REGISTRY_PASSWORD))
                .build();
    }

    /**
     * create a DockerClient
     */
    public static DockerClient getDockerClient() {
        return DockerClientBuilder
                .getInstance(dockerClientConfig())
                .withDockerCmdExecFactory(dockerCmdExecFactory())
                .build();
    }

}
