package cn.ac.iie.proxy.controller;

import cn.ac.iie.ProxyMain;
import cn.ac.iie.common.Constants;
import cn.ac.iie.common.DockerConfig;
import cn.ac.iie.handler.DockerImageHandler;
import cn.ac.iie.handler.Impl.DockerImageHandlerImpl;
import cn.ac.iie.proxy.result.CodeMsg;
import cn.ac.iie.proxy.result.Result;
import com.github.dockerjava.api.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Fighter Created on 2018/10/8.
 */
@RestController
@RequestMapping(value = "/image")
public class ImageController {

    private DockerImageHandler dockerImageHandler = new DockerImageHandlerImpl(DockerConfig.getDockerClient());
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
    private String registryRepoName = ProxyMain.conf.getString(Constants.REGISTRY_REPO_NAME);
    private String registryProjectName = ProxyMain.conf.getString(Constants.REGISTRY_PROJECT_NAME);

    @GetMapping(value = "/push")
    public Result<Integer> push(@RequestParam("imagePath") String imagePath,
                                @RequestParam("imageName") String imageName,
                                @RequestParam("tag") String tag) {
        //先 load
        Result<Integer> loadCodeMsg = dockerImageHandler.load(imagePath);
        String newImageNameAndTag = new StringBuffer(imageName)
                .append(":")
                .append(tag)
                .toString();
        String newImageName = new StringBuffer(registryRepoName)
                .append("/")
                .append(registryProjectName)
                .append("/")
                .append(imageName)
                .toString();
        dockerImageHandler.tag(newImageNameAndTag, newImageName, tag);
        if (CodeMsg.SUCCESS.getCode() == loadCodeMsg.getCode()) {
            return dockerImageHandler.push(newImageName + ":" + tag);
        } else {
            return loadCodeMsg;
        }
    }

    @GetMapping(value = "/pull")
    public Result<Integer> pull(@RequestParam("imageName") String imageName,
                                @RequestParam("tag") String tag) {
        return dockerImageHandler.pull(imageName + ":" + tag);
    }

    @GetMapping(value = "/tag")
    public Result<Integer> tag(@RequestParam("oldImageNameAndTag") String oldImageNameAndTag,
                               @RequestParam("newImageName") String newImageName,
                               @RequestParam("newTag") String newTag) {
        return dockerImageHandler.tag(oldImageNameAndTag, newImageName, newTag);
    }

    @GetMapping(value = "/remove")
    public Result<Integer> removeImage(@RequestParam("imageName") String imageName,
                                       @RequestParam("tag") String tag) {
        return dockerImageHandler.removeImage(imageName + ":" + tag);
    }

    @GetMapping(value = "/list")
    public Result<List<Image>> listImage() {
        return dockerImageHandler.getImages();
    }
}
