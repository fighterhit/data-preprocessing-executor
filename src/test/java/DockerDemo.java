import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Fighter Created on 2018/8/28.
 */
public class DockerDemo {
    public static void main(String[] args) {
        //初始化
        Properties properties = new Properties();
        properties.setProperty("DOCKER_HOST", "tcp://192.168.11.188:2375");
        DefaultDockerClientConfig config
                = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withProperties(properties).build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();

        //获取所有image
        System.out.println("---------------------------- image ----------------------------");
        List<Image> images = dockerClient.listImagesCmd().exec();
        System.out.println(Arrays.toString(images.get(13).getRepoTags()));
        System.out.println(Arrays.toString(images.get(16).getRepoTags()));
//        System.out.println(images.get(13).getRepoTags()[0]);
//        System.out.println("---------------------------- image count----------------------------");
//        System.out.println(images.size());
//        images.forEach((image) -> {
//            System.out.println("Id: " + image.getId());
//            System.out.println("RepoTags: " + Arrays.toString(image.getRepoTags()));
//            System.out.println("VirtualSize: " + image.getVirtualSize());
//            System.out.println("Created: " + image.getCreated());
//            System.out.println("--------------------------------------------------------");
//        });

        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).getId().split(":")[1].substring(0,12).equals("e1ddd7948a1c")){
                System.out.println(i);
            }
        }


        /*//获取所有Container
        System.out.println("------------------------- containers --------------------------");
        List<Container> containers = dockerClient.listContainersCmd()
                .withShowSize(true)
                .withShowAll(true).exec();
        containers.forEach((container) -> {
            System.out.println("container: " + container.toString());
        });

        //获取所有的network
        System.out.println("-------------------------- network ----------------------------");
        List<Network> networks = dockerClient.listNetworksCmd().exec();
        networks.forEach((network) -> {
            System.out.println("network: " + network.toString());
        });*/

        //上传自己写的helloworld server
        /*System.out.println("----------------------- build an image ------------------------");
        String imageId = dockerClient.buildImageCmd()
                //写完全的Dockerfile路径
                .withDockerfile(new File("C:\\Users\\Limyiter\\Documents\\k8s\\hw\\hello-world-server\\Dockerfile.properties"))
                .withPull(true)
                .withNoCache(true)
                //对应 docker images 后的 REPOSITORY:TAG
                //没有分号的话，就是REPOSITORY，TAG为latest
                .withTag("myhelloworld:1.0")
                .exec(new BuildImageResultCallback())
                .awaitImageId();
        System.out.println("image id: " + imageId);*/


//        //删除image，需要imageId
//        System.out.println("---------------------- remove an image ------------------------");
//        System.out.println("image id: " + imageId);
//        dockerClient.removeImageCmd(imageId).exec();
//
//        //获取所有image
//        System.out.println("---------------------------- image ----------------------------");
//        images = dockerClient.listImagesCmd().exec();
//        images.forEach((image) -> {
//            System.out.println("image: " + image.toString());
//        });
    }
}
