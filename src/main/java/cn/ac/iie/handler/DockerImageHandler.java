package cn.ac.iie.handler;

import com.github.dockerjava.api.model.Image;

import java.util.List;
import java.util.Map;

/**
 * @author Fighter Created on 2018/9/26.
 */
public interface DockerImageHandler {
    Map<String, String> load(String imagePath) throws Exception;

    void tag(String oldImageNameAndTag, String newImageName, String newTag);

    void push(String imageNameAndTag);

    void pull(String imageNameAndTag);

    List<Image> getImages();

    String build(String dockerFile);

    String build(String dockerFile, String imageNameAndTag);
}
