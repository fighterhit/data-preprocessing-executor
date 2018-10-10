package cn.ac.iie.handler.Impl;

import cn.ac.iie.ProxyMain;
import cn.ac.iie.common.Constants;
import cn.ac.iie.handler.DockerImageHandler;
import cn.ac.iie.proxy.result.CodeMsg;
import cn.ac.iie.proxy.result.Result;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Fighter Created on 2018/9/27.
 */
public class DockerImageHandlerImpl implements DockerImageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageHandlerImpl.class);
    private DockerClient dockerClient;

    private String registryUserName;
    private String registryUserPassword;
    private String registryUrl;

    private AuthConfig authConfig;

    // create a docker client
    public DockerImageHandlerImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
        registryUserName = ProxyMain.conf.getString(Constants.REGISTRY_USERNAME);
        registryUserPassword = ProxyMain.conf.getString(Constants.REGISTRY_PASSWORD);
        registryUrl = ProxyMain.conf.getString(Constants.REGISTRY_URL);

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
    public Result<Integer> load(String imagePath) {
//        Map<String, String> map = null;
        try {
            File file = new File(imagePath.trim());
            String fileName = file.getName();
            String[] imageNameAndTag = fileName.substring(0, fileName.lastIndexOf('.')).split("_");
            InputStream uploadStream = new FileInputStream(file);
            dockerClient.loadImageCmd(uploadStream).exec();
            return Result.success(0);
//            map = new HashMap<>();
//            map.put("imageName", imageNameAndTag[0]);
//            map.put("tag", imageNameAndTag[1]);
        } catch (IOException e) {
            LOGGER.error("{}", e);
            return Result.error(CodeMsg.LOAD_IMAGE_ERROR);
        }
    }

    public Result<Integer> tag(String oldImageNameAndTag, String newImageName, String newTag) {
        try {
            dockerClient.tagImageCmd(oldImageNameAndTag, newImageName, newTag).exec();
            return Result.success(0);
        } catch (Exception e) {
            LOGGER.error("image: {} tag error! {}", oldImageNameAndTag, ExceptionUtils.getFullStackTrace(e));
            return Result.error(CodeMsg.TAG_IMAGE_ERROR);
        }
    }

    @Override
    public Result<Integer> push(String imageNameAndTag) {
        try {
            //push image
            dockerClient.pushImageCmd(imageNameAndTag)
                    .withAuthConfig(authConfig)
                    .exec(new MyPushImageResultCallback(imageNameAndTag))
                    .awaitSuccess();
            return Result.success(0);
        } catch (Exception e) {
            LOGGER.error("push image: {} error! {}", imageNameAndTag, ExceptionUtils.getFullStackTrace(e));
            return Result.error(CodeMsg.PUSH_IMAGE_ERROR);
        }
    }

    @Override
    public Result<Integer> pull(String imageNameAndTag) {
        try {
            //pull image
            dockerClient.pullImageCmd(imageNameAndTag)
                    .exec(new MyPullImageResultCallback(imageNameAndTag))
                    .awaitSuccess();
            return Result.success(0);
        } catch (Exception e) {
            LOGGER.error("pull image: {} error! {}", imageNameAndTag, ExceptionUtils.getFullStackTrace(e));
            return Result.error(CodeMsg.PULL_IMAGE_ERROR);
        }
    }

    /**
     * get all the images from the remote docker engine
     *
     * @return
     */
    @Override
    public Result<List<Image>> getImages() {
        try {
            List<Image> images = dockerClient.listImagesCmd().exec();
            return Result.success(images);
        } catch (Exception e) {
            LOGGER.error("list images error! {}", e);
            return Result.error(CodeMsg.LIST_IMAGE_ERROR);
        }
    }

    /**
     * remove all the images from the remote docker engine
     *
     * @return
     */
    @Override
    public Result<Integer> removeImage(String imageNameAndTag) {
        try {
            dockerClient.removeImageCmd(imageNameAndTag).exec();
            return Result.success(0);
        } catch (Exception e) {
            LOGGER.error("remove image: {} error! {}", imageNameAndTag, ExceptionUtils.getFullStackTrace(e));
            return Result.error(CodeMsg.REMOVE_IMAGE_ERROR);
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
