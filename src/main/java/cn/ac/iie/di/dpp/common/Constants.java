package cn.ac.iie.di.dpp.common;

/**
 * @author Fighter Created on 2018/9/27.
 */
public interface Constants {
    String DOCKER_HOST = "docker.host";
    String REGISTRY_URL = "registry.url";
    String REGISTRY_USERNAME = "registry.username";
    String REGISTRY_PASSWORD = "registry.password";
    String DOCKER_READ_TIMEOUT = "docker.read.timeout";
    String DOCKER_CONNECT_TIMEOUT = "docker.connect.timeout";
    String DOCKER_MAX_TOTAL_CONNECTIONS = "docker.max.total.connections";
    String DOCKER_MAX_PER_ROUTE_CONNNECTIONS = "docker.max.per.route.connections";
    String REGISTRY_REPO_NAME = "registry.repo.name";
    String REGISTRY_PROJECT_NAME = "registry.project.name";
    String HARBOR_BASEAPI = "harbor.base.api";
    String HTTP_HEADER_AUTHORIZATION = "http.header.authorization";
    String JETTY_SERVER_PORT = "jetty.server.port";
    String JETTY_SERVER_PARALLEL = "jetty.server.parallel";
    String JETTY_SERVER_IMAGE_ROOT_CONTEXT_URI = "jetty.server.image.root.context.uri";
    String JETTY_SERVER_REGISTRY_ROOT_CONTEXT_URI = "jetty.server.registry.root.context.uri";
    String JETTY_SERVER_K8S_ROOT_CONTEXT_URI = "jetty.server.k8s.root.context.uri";
    String K8S_MASTER = "k8s.master";
    String K8S_USERNAME = "k8s.username";
    String K8S_PASSWORD = "k8s.password";
}
