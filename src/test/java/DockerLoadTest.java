import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

/**
 * @author Fighter Created on 2018/9/12.
 */
public class DockerLoadTest {
    public static void main(String[] args) {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withRegistryUrl("http://192.168.11.188/")
                .withRegistryUsername("admin")
                .withRegistryPassword("Harbor12345")
                .build();
        DockerClient docker = DockerClientBuilder.getInstance(config).build();
        System.out.println(docker.listImagesCmd().withShowAll(true));
    }
}
