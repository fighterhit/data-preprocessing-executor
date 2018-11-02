package cn.ac.iie.proxy.controller;

import cn.ac.iie.ProxyMain;
import cn.ac.iie.common.Constants;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    //    private static String DockerfilePath ;
    private static final String Dockerfilepath;

    static {
        Dockerfilepath = ClassLoader.getSystemClassLoader().getResource("Dockerfile.properties").getFile();
    }

    //imagePath:url_test
    //check:{"path1":["path1File1","path1File2"],"path2":["path2File1","path2File2"]}
    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            String imagePath = paramMap.get("imagePath")[0];
//            String check = paramMap.get("check")[0];
//            JSONObject jsonObject = JSON.parseObject(check);
            Map<String, String> map = new HashMap<>();
            //解压，创建解压文件夹
            unCompressImageTar(imagePath, map);
            String desDir = map.get("desDir");
            //todo 校验路径是否存在 取消校验路径
//            if (!jsonObject.isEmpty()) {
            //先load
            dockerImageHandler.load(imagePath);
            //build：修改dockerfile模板后build
            build(map);
            //tag
            String pushImageAndTag = getPushTag(map);
            //push
            dockerImageHandler.push(pushImageAndTag);
            //return
            response.getWriter().print(JSON.toJSONString(map));
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
//            } else {
//                response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "verify error!");
//            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "server error.");
        }
    }

    private String getPushTag(Map<String, String> map) {
        try {
            String buildImageAndTag = map.get("buildImageAndTag");
            String oldImage = buildImageAndTag.split(":")[0];
            String tag = buildImageAndTag.split(":")[1];
            String newImageName = new StringBuffer(ProxyMain.conf.getString(Constants.REGISTRY_REPO_NAME))
                    .append("/")
                    .append(ProxyMain.conf.getString(Constants.REGISTRY_PROJECT_NAME))
                    .append("/")
                    .append(oldImage).toString();
            dockerImageHandler.tag(buildImageAndTag, newImageName, tag);
            return newImageName + ":" + tag;
        } catch (Exception e) {
            LOGGER.error("getPushTag error! {}", ExceptionUtils.getFullStackTrace(e));
            throw e;
        }
    }

    private void build(Map<String, String> map) throws Exception {
        try {
            //新tag名为：oldImageName_:oldTag
            String oldImageNameTag = map.get("RepoTags");
            String[] repoTags = oldImageNameTag.split(":");
            String imageName = repoTags[0];
            String tag = repoTags[1];
            String buildImageAndTag = imageName + "_iie:" + tag;

            //修改Dockerfile模板，复制一份到解压文件中
            String desDir = map.get("desDir");
            String buildDockerfilePath = desDir + File.separator + "Dockerfile";
            int ret = IOUtils.copy(new FileInputStream(Dockerfilepath), new FileOutputStream(buildDockerfilePath));
            if (ret < 0) {
                throw new Exception("copy Dockerfile.properties template error!");
            }
//
            List<String> lines = Files.readAllLines(Paths.get(buildDockerfilePath));
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith("FROM")) {
                    lines.set(i, line.replaceFirst("(?<=FROM )(.*)", oldImageNameTag));
                    break;
                }
            }
            //修改模板副本文件
            IOUtils.writeLines(lines, IOUtils.LINE_SEPARATOR, new FileOutputStream(buildDockerfilePath));
//            IOUtils.writeLines(lines, IOUtils.LINE_SEPARATOR, new FileWriter(buildDockerfilePath));
            String imageID = dockerImageHandler.build(buildDockerfilePath, buildImageAndTag);
            map.put("imageID", imageID);
            map.put("buildImageAndTag", buildImageAndTag);
        } catch (Exception e) {
            LOGGER.error("build image error! {}", ExceptionUtils.getFullStackTrace(e));
            throw e;
        }
    }

    //返回压缩文件目录、镜像名:标签
    private void unCompressImageTar(String imagePath, Map<String, String> map) throws Exception {
        try {
            String desDir = imagePath.substring(0, imagePath.lastIndexOf("."));
            UnCompressUtils.unTar(new File(imagePath), desDir);
            map.put("desDir", desDir);
            getRepoTags(desDir, map);
        } catch (Exception e) {
            LOGGER.error("uncompress error! {}", ExceptionUtils.getFullStackTrace(e));
            throw new Exception(e);
        }
    }

    //    @Test
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
}
