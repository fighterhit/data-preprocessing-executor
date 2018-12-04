package cn.ac.iie.di.dpp.handler;

import cn.ac.iie.di.dpp.entity.HttpClientResult;
import cn.ac.iie.di.dpp.entity.Project;
import cn.ac.iie.di.dpp.entity.Repository;

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
    List<Project> listProjects();

    /**
     * /repositories    Get repositories accompany with relevant project and repo name.
     *
     * @param projectId
     * @return
     */
    List<Repository> listRepostories(String projectId);

    /**
     * /repositories/{repo_name}    Delete a repository.
     *
     * @param repositoryName
     * @return
     */
    HttpClientResult deleteRepository(String repositoryName);

    /**
     * /repositories/{repo_name}/tags/{tag} Delete a tag in a repository.
     *
     * @param repo_name
     * @param tag
     * @return
     */
    HttpClientResult deleteRepository(String repo_name, String tag);

    /**
     * /repositories/{repo_name}/tags   Get tags of a relevant repository.
     *
     * @param repositoryName
     * @return
     */
//    List<String> listTagsOfRepository(String repositoryName);
    String listTagsOfRepository(String repositoryName);

}
