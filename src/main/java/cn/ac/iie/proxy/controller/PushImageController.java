package cn.ac.iie.proxy.controller;

import cn.ac.iie.common.DockerConfig;
import cn.ac.iie.di.commons.httpserver.framework.handler.HandlerI;
import cn.ac.iie.entity.VerifyJson;
import cn.ac.iie.handler.DockerImageHandler;
import cn.ac.iie.handler.Impl.DockerImageHandlerImpl;
import cn.ac.iie.util.UnCompressUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @author Fighter Created on 2018/10/8.
 */
public class PushImageController implements HandlerI {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushImageController.class);
    private DockerImageHandler dockerImageHandler = new DockerImageHandlerImpl(DockerConfig.getDockerClient());

    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            String reqJson = request.getParameter("q");
            VerifyJson verifyJson = JSONObject.parseObject(reqJson, VerifyJson.class);
            String imagePath = verifyJson.getImagePath();
            List<String> path = verifyJson.getPath();
            List<List<String>> files = verifyJson.getFiles();

            //解压，创建解压文件夹
            String desDir = unCompressImageTar(imagePath);

            //校验路径是否存在
            if (path.size() > 0 && existPaths(desDir, path)) {
                //检查文件
                if (files.size() > 0 && files.size() == path.size()) {
                    if (existFiles(desDir, path, files)) {
                        //先load
                        dockerImageHandler.load(imagePath);
                        String dockerFilePath = null;
                        build(dockerFilePath);

                    } else {
                        response.sendError(HttpServletResponse.SC_NO_CONTENT, "verify error");
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NO_CONTENT, "files error");
                }
            } else {
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "image path error!");
            }
        } catch (Exception e) {
            LOGGER.error("server error! {}", ExceptionUtils.getFullStackTrace(e));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "request failed.");
        }
    }

    private void build(String dockerFilePath) {


    }

    private String unCompressImageTar(String imagePath) throws Exception {
        String fileName = new File(imagePath).getName();
        String desDir = fileName.substring(0, fileName.lastIndexOf('.'));
        try {
            UnCompressUtils.unTar(new File(imagePath), desDir);
        } catch (Exception e) {
            LOGGER.error("uncompress error! {}", ExceptionUtils.getFullStackTrace(e));
        }
        return desDir;
    }

    //检查文件是否存在
    private boolean existFiles(String desDir, List<String> path, List<List<String>> files) {


        return true;
    }

    //检查tar包内这些路径是否存在
    private boolean existPaths(String desDir, List<String> path) {
        //code

        return true;
    }
}
