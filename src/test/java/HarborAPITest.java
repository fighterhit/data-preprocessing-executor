import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fighter Created on 2018/9/12.
 */
public class HarborAPITest {

    /**
     * create a DockerClientConfig
     *
     * @return
     */
    public static DockerClientConfig dockerClientConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://192.168.11.230:2375")
                .withDockerTlsVerify(false)
                .withRegistryUrl("http://192.168.11.112/v2/")
                .withRegistryUsername("admin")
                .withRegistryPassword("Harbor12345")
                .build();
    }

    /**
     * create a DockerClient
     */
    public static DockerClient dockerClient() {
        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerClientConfig())
//                .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
//                .withDockerCmdExecFactory(new JerseyDockerCmdExecFactory())
//                .withDockerCmdExecFactory(dockerCmdExecFactory())
                .build();
        return dockerClient;
    }

    public static DockerCmdExecFactory dockerCmdExecFactory() {
        return new JerseyDockerCmdExecFactory()
                .withReadTimeout(40000)
                .withConnectTimeout(40000)
                .withMaxTotalConnections(100)
                .withMaxPerRouteConnections(10);
    }

    public static void main(String[] args) throws InterruptedException {
        DockerClient dockerClient = dockerClient();
//        String path = "G:\\IdeaProjects\\docker-registry-proxy\\src\\test\\java\\image_1.0.tar";
//        testLoad(dockerClient, path);
//        testTag(dockerClient, "192.168.11.112/hlg_web/busybox:harbortest");
        testGetImages(dockerClient);
    }


    static void testGetImagesFromRegistry(DockerClient dockerClient) {
        List<Image> images = null;
        try {
            images = dockerClient.listImagesCmd().exec();
            System.out.println(images);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void testGetImages(DockerClient dockerClient) {
         List<Image> images = null;
        try {
            images = dockerClient.listImagesCmd().exec();
            System.out.println(images);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void testTag(DockerClient dockerClient, String oldImageName) {
        StringBuffer sb = new StringBuffer();
        String newImageName = sb.append("192.168.11.112")
                .append("/")
                .append("hlg_web")
                .append("/")
                .append("busybox")
                .toString();
        String newTag = "test";
        dockerClient.tagImageCmd(oldImageName, newImageName, newTag).exec();
    }

    static void testLoad(DockerClient dockerClient, String imagePath) {
        try {
            File file = new File(imagePath.trim());
            String fileName = file.getName();
            String[] imageNameAndTag = fileName.substring(0, fileName.lastIndexOf('.')).split("_");
            InputStream uploadStream = new FileInputStream(file);
            dockerClient.loadImageCmd(uploadStream).exec();
            Map<String, String> map = new HashMap<>();
            map.put("imageName", imageNameAndTag[0]);
            map.put("tag", imageNameAndTag[1]);
            System.out.println(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void testPull(DockerClient dockerClient) {
        //pull image
        dockerClient.pullImageCmd("192.168.11.112/hlg_web/nginx")
                .exec(new MyPullImageResultCallback())
                .awaitSuccess();

        dockerClient
                .pullImageCmd("192.168.11.112/hlg_web/busybox")
                .exec(new MyPullImageResultCallback())
                .awaitSuccess();
    }

    void testPush(DockerClient dockerClient) {

        AuthConfig authConfig = new AuthConfig()
                .withUsername("admin")
                .withPassword("Harbor12345")
                .withRegistryAddress("http://192.168.11.112/v2/");

        List<Image> images = dockerClient.listImagesCmd().exec();

        // 192.168.11.188/hlg_web/nginx:1.11111
        String nginxImageName = images.get(13).getRepoTags()[0];
        // 192.168.11.188/hlg_web/busybox:harbortest
        String busyboxImageName = images.get(16).getRepoTags()[0];

        //push image
        dockerClient.pushImageCmd(busyboxImageName)
                .withAuthConfig(authConfig)
                .exec(new MyPushImageResultCallback())
                .awaitSuccess();

        dockerClient.pushImageCmd(nginxImageName)
                .withAuthConfig(authConfig)
                .exec(new MyPushImageResultCallback())
                .awaitSuccess();
    }

    void testBuild(DockerClient dockerClient) {
        BuildImageResultCallback callback = new BuildImageResultCallback() {
            @Override
            public void onNext(BuildResponseItem item) {
                System.out.println("It's done - > " + item);
                super.onNext(item);
            }
        };

        //build image and start container
/*        dockerClient.buildImageCmd(baseDir).withTag(image.getRegistryEndpoint() + "/apps/test:" + appImage
                .getName()).exec(callback).awaitImageId();

        dockerClient.listImagesCmd().withShowAll(true).exec();

        CreateContainerResponse containerResponse = dockerClient.createContainerCmd(appImage
                .getRegistryEndpoint() + "/apps/test:" + appImage.getName())
                .exec();

        dockerClient.startContainerCmd(containerResponse.getId()).exec();*/
    }
}

class MyPushImageResultCallback extends PushImageResultCallback {

    @Override
    public void onNext(PushResponseItem item) {
        System.out.println("id:" + item.getId() + " status: " + item.getStatus());
        super.onNext(item);
    }

    @Override
    public void onComplete() {
        System.out.println("Image pushed completed!");
        super.onComplete();
    }
}

class MyPullImageResultCallback extends PullImageResultCallback {

    @Override
    public void onNext(PullResponseItem item) {
        System.out.println("id:" + item.getId() + " status: " + item.getStatus());
        super.onNext(item);
    }

    @Override
    public void onComplete() {
        System.out.println("Image pulled completed!");
        super.onComplete();
    }
}