package cn.ac.iie.handler.Impl;

import cn.ac.iie.entity.Project;
import cn.ac.iie.entity.Repository;
import cn.ac.iie.handler.RegistryHandler;
import org.apache.http.client.HttpClient;

import java.util.List;

/**
 * @author Fighter Created on 2018/9/28.
 */
public class RegistryHandlerImpl implements RegistryHandler {
    private HttpClient client;

    @Override
    public List<Project> listProjects() {
        return null;
    }

    @Override
    public List<Repository> listRepostories(String projectId) {
        return null;
    }

    @Override
    public int deleteRepository(String repositoryName) {
        return 0;
    }

    @Override
    public int deleteRepository(String repo_name, String tag) {
        return 0;
    }

    @Override
    public List<String> listTagsOfRepository(String repositoryName) {
        return null;
    }
}
