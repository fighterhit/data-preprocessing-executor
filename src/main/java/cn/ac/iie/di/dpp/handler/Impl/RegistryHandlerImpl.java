package cn.ac.iie.di.dpp.handler.Impl;

import cn.ac.iie.di.dpp.main.ProxyMain;
import cn.ac.iie.di.dpp.common.Constants;
import cn.ac.iie.di.dpp.entity.HttpClientResult;
import cn.ac.iie.di.dpp.entity.Project;
import cn.ac.iie.di.dpp.entity.Repository;
import cn.ac.iie.di.dpp.entity.Tag;
import cn.ac.iie.di.dpp.handler.RegistryHandler;
import cn.ac.iie.di.dpp.util.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Fighter Created on 2018/9/28.
 */
public class RegistryHandlerImpl implements RegistryHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryHandlerImpl.class);

    private String authorization;
    private String harborBaseAPI;

    private String registryRepoName;
    private String registryProjectName;

    public RegistryHandlerImpl() {
        harborBaseAPI = ProxyMain.conf.getString(Constants.HARBOR_BASEAPI);
        authorization = ProxyMain.conf.getString(Constants.HTTP_HEADER_AUTHORIZATION);
        registryRepoName = ProxyMain.conf.getString(Constants.REGISTRY_REPO_NAME);
        registryProjectName = ProxyMain.conf.getString(Constants.REGISTRY_PROJECT_NAME);
    }

    @Override
    public List<Project> listProjects() {
        List<Project> projects = null;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            String reqUrl = new StringBuffer(harborBaseAPI).append("/").append("projects").toString();
            HttpClientResult result = HttpClientUtils.doGet(reqUrl, headers, null);
            JSONArray jsonArray = JSON.parseArray(result.getContent());
            projects = jsonArray.toJavaList(Project.class);
        } catch (Exception e) {
            LOGGER.error("list projects error! {}", e);
        }
        return projects;
    }

    @Override
    public List<Repository> listRepostories(String projectId) {
        List<Repository> repositories = null;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            Map<String, String> params = new HashMap<>();
            params.put("project_id", projectId);
            String reqUrl = new StringBuffer(harborBaseAPI).append("/").append("repositories").toString();
            HttpClientResult result = HttpClientUtils.doGet(reqUrl, headers, params);
            JSONArray jsonArray = JSON.parseArray(result.getContent());
            repositories = jsonArray.toJavaList(Repository.class);
        } catch (Exception e) {
            LOGGER.error("list repostories error! {}", ExceptionUtils.getFullStackTrace(e));
        }
        return repositories;
    }

    @Override
    public HttpClientResult deleteRepository(String repositoryName) {
        int retCode = -1;
        HttpClientResult result = null;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            String reqUrl = new StringBuffer(harborBaseAPI)
                    .append("/repositories/")
                    .append(repositoryName)
                    .toString();
            result = HttpClientUtils.doDelete(reqUrl, headers);
        } catch (Exception e) {
            LOGGER.error("delete repository error! {}", e);
        }
        return result;
    }

    @Override
    public HttpClientResult deleteRepository(String repositoryName, String tag) {
        int retCode = -1;
        HttpClientResult result = null;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            String reqUrl = new StringBuffer(harborBaseAPI)
                    .append("/repositories/")
                    .append(registryProjectName)
                    .append("/")
                    .append(repositoryName)
                    .append("/tags/")
                    .append(tag)
                    .toString();
            result = HttpClientUtils.doDelete(reqUrl, headers);
        } catch (Exception e) {
            LOGGER.error("delete repository error! {}", e);
        }
        return result;
    }

    @Override
    public List<String> listTagsOfRepository(String repositoryName) {
        List<String> tags = null;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            String reqUrl = new StringBuffer(harborBaseAPI)
                    .append("/repositories/")
                    .append(repositoryName)
                    .append("/tags")
                    .toString();
            HttpClientResult result = HttpClientUtils.doGet(reqUrl, headers, null);
            JSONArray jsonArray = JSON.parseArray(result.getContent());
            tags = jsonArray.toJavaList(Tag.class).stream().map(Tag::getName).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("list tags error! {}", e);
        }
        return tags;
    }
}
