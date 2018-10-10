package cn.ac.iie.handler;

import cn.ac.iie.proxy.result.Result;
import com.github.dockerjava.api.model.Image;

import java.util.List;

/**
 * @author Fighter Created on 2018/9/26.
 */
public interface DockerImageHandler {
    Result<Integer> load(String imagePath);

    Result<Integer> tag(String oldImageNameAndTag, String newImageName, String newTag);

    Result<Integer> push(String imageNameAndTag);

    Result<Integer> pull(String imageNameAndTag);

    Result<List<Image>> getImages();

    Result<Integer> removeImage(String imageNameAndTag);
}
