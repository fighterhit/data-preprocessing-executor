package cn.ac.iie.handler.Impl;

import cn.ac.iie.ProxyMain;
import cn.ac.iie.common.Constants;
import cn.ac.iie.entity.HttpClientResult;
import cn.ac.iie.entity.Project;
import cn.ac.iie.entity.Repository;
import cn.ac.iie.entity.Tag;
import cn.ac.iie.handler.RegistryHandler;
import cn.ac.iie.proxy.result.CodeMsg;
import cn.ac.iie.proxy.result.Result;
import cn.ac.iie.util.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jetty.http.HttpStatus;
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

    public RegistryHandlerImpl() {
        harborBaseAPI = ProxyMain.conf.getString(Constants.HARBOR_BASEAPI);
        authorization = ProxyMain.conf.getString(Constants.HTTP_HEADER_AUTHORIZATION);
    }

    @Override
    public Result<List<Project>> listProjects() {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            String reqUrl = new StringBuffer(harborBaseAPI).append("/").append("projects").toString();
            HttpClientResult result = HttpClientUtils.doGet(reqUrl, headers, null);
            JSONArray jsonArray = JSON.parseArray(result.getContent());
            List<Project> projects = jsonArray.toJavaList(Project.class);
            return Result.success(projects);
        } catch (Exception e) {
            LOGGER.error("list projects error! {}", e);
            return Result.error(CodeMsg.LIST_REGISTRY_ERROR);
        }
    }

    @Override
    public Result<List<Repository>> listRepostories(String projectId) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            Map<String, String> params = new HashMap<>();
            params.put("project_id", projectId);
            String reqUrl = new StringBuffer(harborBaseAPI).append("/").append("repositories").toString();
            HttpClientResult result = HttpClientUtils.doGet(reqUrl, headers, params);
            JSONArray jsonArray = JSON.parseArray(result.getContent());
            List<Repository> repositories = jsonArray.toJavaList(Repository.class);
            return Result.success(repositories);
        } catch (Exception e) {
            LOGGER.error("list repostories error! {}", e);
            return Result.error(CodeMsg.LIST_REPOSITORY_ERROR);
        }
    }

    @Override
    public Result<Integer> deleteRepository(String repositoryName) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            String reqUrl = new StringBuffer(harborBaseAPI)
                    .append("/repositories/")
                    .append(repositoryName)
                    .toString();
            HttpClientResult result = HttpClientUtils.doDelete(reqUrl, headers);
            if (HttpStatus.OK_200 == result.getCode()) {
                return Result.success(0);
            }
        } catch (Exception e) {
            LOGGER.error("delete repository error! {}", e);
        }
        return Result.error(CodeMsg.REMOVE_IMAGE_REGISTRY_ERROR);
    }

    @Override
    public Result<Integer> deleteRepository(String repositoryName, String tag) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            String reqUrl = new StringBuffer(harborBaseAPI)
                    .append("/repositories/")
                    .append(repositoryName)
                    .append("/tags/")
                    .append(tag)
                    .toString();
            HttpClientResult result = HttpClientUtils.doDelete(reqUrl, headers);
            if (HttpStatus.OK_200 == result.getCode()) {
                return Result.success(0);
            }
        } catch (Exception e) {
            LOGGER.error("delete repository error! {}", ExceptionUtils.getFullStackTrace(e));
        }
        return Result.error(CodeMsg.REMOVE_IMAGE_REGISTRY_ERROR);
    }

    @Override
    public Result<List<String>> listTagsOfRepository(String repositoryName) {
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
            List<String> tags = jsonArray.toJavaList(Tag.class).stream().map(Tag::getName).collect(Collectors.toList());
            return Result.success(tags);
        } catch (Exception e) {
            LOGGER.error("list tags error! {}", e);
            return Result.error(CodeMsg.LIST_TAG_ERROR);
        }
    }
}
