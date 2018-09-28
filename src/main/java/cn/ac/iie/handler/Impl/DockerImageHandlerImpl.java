package cn.ac.iie.handler.Impl;

import cn.ac.iie.handler.DockerImageHandler;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.command.PushImageResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Fighter Created on 2018/9/27.
 */
public class DockerImageHandlerImpl implements DockerImageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageHandlerImpl.class);
    private DockerClient dockerClient;

    // create a docker client
    public DockerImageHandlerImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    /**
     * load tar archive to docker
     *
     * @param imagePath imageName_tag
     * @return
     */
    @Override
    public Map<String, String> load(String imagePath) {
        Path path = Paths.get(imagePath);
        try {
            String[] imageNameAndTag = imagePath.split("_");
            InputStream uploadStream = Files.newInputStream(path);
            dockerClient.loadImageCmd(uploadStream).exec();
            Map<String, String> map = new HashMap<>();
            map.put("imageName", imageNameAndTag[0]);
            map.put("tag", imageNameAndTag[1]);
            return map;
        } catch (IOException e) {
            LOGGER.error("");
        }
        return null;
    }

    @Override
    public void tag(String image, String repo, String tag) {

    }

    @Override
    public void push(String image, String repo, String tag) {

    }

    @Override
    public List<Image> getImages() {
        return null;
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
}
