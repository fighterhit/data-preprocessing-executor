package cn.ac.iie.handler;

import cn.ac.iie.entity.Project;
import cn.ac.iie.entity.Repository;
import cn.ac.iie.proxy.result.Result;

import java.util.List;

/**
 * @author Fighter Created on 2018/9/28.
 */
public interface RegistryHandler {

    /**
     * /projects List projects
     *
     * @return
     */
    Result<List<Project>> listProjects();

    /**
     * /repositories    Get repositories accompany with relevant project and repo name.
     *
     * @param projectId
     * @return
     */
    Result<List<Repository>> listRepostories(String projectId);

    /**
     * /repositories/{repo_name}    Delete a repository.
     *
     * @param repositoryName
     * @return
     */
    Result<Integer> deleteRepository(String repositoryName);

    /**
     * /repositories/{repo_name}/tags/{tag} Delete a tag in a repository.
     *
     * @param repo_name
     * @param tag
     * @return
     */
    Result<Integer> deleteRepository(String repo_name, String tag);

    /**
     * /repositories/{repo_name}/tags   Get tags of a relevant repository.
     *
     * @param repositoryName
     * @return
     */
    Result<List<String>> listTagsOfRepository(String repositoryName);


}
