package cn.ac.iie.handler.Impl;

import cn.ac.iie.ProxyMain;
import cn.ac.iie.common.Constants;
import cn.ac.iie.handler.DockerImageHandler;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Fighter Created on 2018/9/27.
 */
public class DockerImageHandlerImpl implements DockerImageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageHandlerImpl.class);
    private DockerClient dockerClient;

    private String registryUserName;
    private String registryUserPassword;
    private String registryUrl;
    private String registryRepoName;
    private String registryProjectName;
    private AuthConfig authConfig;

    // create a docker client
    public DockerImageHandlerImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
        registryUserName = ProxyMain.conf.getString(Constants.REGISTRY_USERNAME);
        registryUserPassword = ProxyMain.conf.getString(Constants.REGISTRY_PASSWORD);
        registryUrl = ProxyMain.conf.getString(Constants.REGISTRY_URL);
        registryRepoName = ProxyMain.conf.getString(Constants.REGISTRY_REPO_NAME);
        registryProjectName = ProxyMain.conf.getString(Constants.REGISTRY_PROJECT_NAME);
        authConfig = new AuthConfig()
                .withUsername(registryUserName)
                .withPassword(registryUserPassword)
                .withRegistryAddress(registryUrl);
    }

    /**
     * load tar archive to docker
     *
     * @param imagePath imageName_tag
     * @return
     */
    @Override
    public Map<String, String> load(String imagePath) {
        Map<String, String> map = null;
        try {
            File file = new File(imagePath.trim());
            String fileName = file.getName();
            String[] imageNameAndTag = fileName.substring(0, fileName.lastIndexOf('.')).split("_");
            InputStream uploadStream = new FileInputStream(file);
            dockerClient.loadImageCmd(uploadStream).exec();
            map = new HashMap<>();
            map.put("imageName", imageNameAndTag[0]);
            map.put("tag", imageNameAndTag[1]);
        } catch (IOException e) {
            LOGGER.error("{}", e);
        }
        return map;
    }

    public void tag(String oldImageNameAndTag, String newImageName, String newTag) {
        try {
            dockerClient.tagImageCmd(oldImageNameAndTag, newImageName, newTag).exec();
        } catch (Exception e) {
            LOGGER.error("tag image: {} error! {}", e);
        }
    }

    @Override
    public void push(String imageNameAndTag) {
        try {
            //push image
            dockerClient.pushImageCmd(imageNameAndTag)
                    .withAuthConfig(authConfig)
                    .exec(new MyPushImageResultCallback(imageNameAndTag))
                    .awaitSuccess();
        } catch (Exception e) {
            LOGGER.error("push image: {} error! {}", imageNameAndTag, e);
        }
    }

    @Override
    public void pull(String imageNameAndTag) {
        try {
            //pull image
            dockerClient.pullImageCmd(imageNameAndTag)
                    .exec(new MyPullImageResultCallback(imageNameAndTag))
                    .awaitSuccess();
        } catch (Exception e) {
            LOGGER.error("pull image: {} error! {}", imageNameAndTag, e);
        }
    }

    /**
     * get all the images from the remote docker engine
     *
     * @return
     */
    @Override
    public List<Image> getImages() {
        List<Image> images = null;
        try {
            images = dockerClient.listImagesCmd().exec();
        } catch (Exception e) {
            LOGGER.error("get images error! {}", e);
        }
        return images;
    }

    @Override
    public String build(String dockerFile) {
        return dockerClient.buildImageCmd()
                .withDockerfile(new File(dockerFile))
                .exec(new MyBuildImageResultCallback())
                .awaitImageId();
    }

    public String build(String dockerFile, String imageNameAndTag) {
        return dockerClient.buildImageCmd()
                .withDockerfile(new File(dockerFile))
                .withTags(new HashSet<>(Arrays.asList(imageNameAndTag)))
                .exec(new MyBuildImageResultCallback())
                .awaitImageId();
    }

    class MyBuildImageResultCallback extends BuildImageResultCallback {
        @Override
        public void onNext(BuildResponseItem item) {
            super.onNext(item);
        }

        @Override
        public void onComplete() {
            super.onComplete();
        }
    }

    class MyPushImageResultCallback extends PushImageResultCallback {

        private String imageNameAndTag;

        public MyPushImageResultCallback(String imageNameAndTag) {
            this.imageNameAndTag = imageNameAndTag;
        }

        @Override
        public void onNext(PushResponseItem item) {
            LOGGER.info("id:" + item.getId() + " status: " + item.getStatus());
            super.onNext(item);
        }

        @Override
        public void onComplete() {
            LOGGER.info("Image: {} pushed completed!", imageNameAndTag);
            super.onComplete();
        }
    }

    class MyPullImageResultCallback extends PullImageResultCallback {

        private String imageNameAndTag;

        public MyPullImageResultCallback(String imageNameAndTag) {
            this.imageNameAndTag = imageNameAndTag;
        }

        @Override
        public void onNext(PullResponseItem item) {
            LOGGER.info("id:" + item.getId() + " status: " + item.getStatus());
            super.onNext(item);
        }

        @Override
        public void onComplete() {
            LOGGER.info("Image: {} pulled completed!", imageNameAndTag);
            super.onComplete();
        }
    }
}
