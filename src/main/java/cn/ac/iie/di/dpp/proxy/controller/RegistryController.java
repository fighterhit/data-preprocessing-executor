package cn.ac.iie.di.dpp.proxy.controller;

import cn.ac.iie.entity.Project;
import cn.ac.iie.entity.Repository;
import cn.ac.iie.handler.Impl.RegistryHandlerImpl;
import cn.ac.iie.handler.RegistryHandler;
import cn.ac.iie.proxy.result.CodeMsg;
import cn.ac.iie.proxy.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Fighter Created on 2018/10/8.
 */
@RestController
@RequestMapping(value = "/registry")
public class RegistryController {

    private RegistryHandler registryHandler = new RegistryHandlerImpl();

    @GetMapping(value = "/projects")
    public Result<List<Project>> listProjects() {
        try {
            return registryHandler.listProjects();
        } catch (Exception e) {
            return Result.error(CodeMsg.LIST_REGISTRY_ERROR);
        }
    }

    @GetMapping(value = "/repositories")
    public Result<List<Repository>> listRepostories(@RequestParam(value = "projectId", defaultValue = "2") String projectId) {
        try {
            Result<List<Repository>> result = registryHandler.listRepostories(projectId);
            return result;
        } catch (Exception e) {
            return Result.error(CodeMsg.LIST_REPOSITORY_ERROR);
        }
    }

    @GetMapping(value = "/remove")
    public Result<Integer> removeImage(@RequestParam("imageName") String imageName,
                                       @RequestParam("tag") String tag) {
        if (tag == null) {
            return registryHandler.deleteRepository(imageName);
        }
        //tag不一样，但image id一样都会被删除
        return registryHandler.deleteRepository(imageName, tag);
    }

    @GetMapping(value = "/tags")
    public Result<List<String>> listTagsOfRepository(@RequestParam("repositoryName") String repositoryName) {
        return registryHandler.listTagsOfRepository(repositoryName);
    }
}
