package cn.ac.iie.proxy.controller;

import cn.ac.iie.common.DockerConfig;
import cn.ac.iie.di.commons.httpserver.framework.handler.HandlerI;
import cn.ac.iie.handler.DockerImageHandler;
import cn.ac.iie.handler.Impl.DockerImageHandlerImpl;
import cn.ac.iie.util.UnCompressUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jetty.server.Request;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fighter Created on 2018/10/8.
 */
public class PushImageController implements HandlerI {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushImageController.class);
    private DockerImageHandler dockerImageHandler = new DockerImageHandlerImpl(DockerConfig.getDockerClient());

    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            String imagePath = paramMap.get("imagePath")[0];
            String check = paramMap.get("check")[0];
            JSONObject jsonObject = JSON.parseObject(check);
            Map<String, String> map = new HashMap<>();
            //解压，创建解压文件夹
            unCompressImageTar(imagePath, map);
            String desDir = map.get("desDir");
            //todo 校验路径是否存在
            if (!jsonObject.isEmpty() && existFiles(desDir, jsonObject)) {
                //先load
                dockerImageHandler.load(imagePath);
                String dockerFilePath = null;
                String imageID = build(dockerFilePath);
            } else {
                response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "verify error!");
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getFullStackTrace(e));
            LOGGER.error("server error! {}", ExceptionUtils.getFullStackTrace(e));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "request failed.");
        }
    }

    private String build(String dockerFilePath) {
        return dockerImageHandler.build(dockerFilePath);
    }

    //返回压缩文件目录、镜像名:标签
    private void unCompressImageTar(String imagePath, Map<String, String> map) throws Exception {
        String fileName = new File(imagePath).getName();
        String desDir = fileName.substring(0, fileName.lastIndexOf('.'));
        try {
            UnCompressUtils.unTar(new File(imagePath), desDir);
            map.put("desDir", desDir);
        } catch (Exception e) {
            LOGGER.error("uncompress error! {}", ExceptionUtils.getFullStackTrace(e));
            throw new Exception(e);
        }
    }

    @Test
    private void getRepoTags(String desDir, Map<String, String> map) throws IOException {
        //读取压缩文件目录内的 manifest.json
        InputStream inputStream = new FileInputStream(desDir + File.separator + "manifest.json");
        String jsonStr = IOUtils.toString(inputStream, "utf8");
        JSONObject jsonObject = JSONObject.parseArray(jsonStr).getJSONObject(0);
        String repoTags = jsonObject.getJSONArray("RepoTags").getString(0);
        String imageID = jsonObject.getString("Config");
        map.put("RepoTags", repoTags);
        map.put("imageID", imageID);
    }

    //检查文件是否存在
    private boolean existFiles(String desDir, JSONObject jsonObject) {
        for (String checkPath : jsonObject.keySet()) {
            File path = new File(checkPath);
            if (!Files.isDirectory(Paths.get(desDir + path))) {
                return false;
            }
            String preFix = desDir + path;
            List<String> files = jsonObject.getJSONArray(checkPath).toJavaList(String.class);
            for (String file : files) {
                if (Files.exists(Paths.get(preFix + file))) {
                    return false;
                }
            }
        }
        return true;
    }
}
