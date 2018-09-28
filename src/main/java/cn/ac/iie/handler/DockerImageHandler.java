package cn.ac.iie.handler;

import com.github.dockerjava.api.model.Image;

import java.util.List;
import java.util.Map;

/**
 * @author Fighter Created on 2018/9/26.
 */
public interface DockerImageHandler {
    Map<String,String> load(String imagePath);
    void tag(String image, String repo, String tag);
    void push(String image, String repo, String tag);
    List<Image> getImages();

}
