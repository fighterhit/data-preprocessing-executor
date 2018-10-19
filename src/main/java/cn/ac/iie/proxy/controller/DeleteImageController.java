package cn.ac.iie.proxy.controller;

import cn.ac.iie.di.commons.httpserver.framework.handler.HandlerI;
import cn.ac.iie.entity.HttpClientResult;
import cn.ac.iie.handler.Impl.RegistryHandlerImpl;
import cn.ac.iie.handler.RegistryHandler;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Fighter.
 */
public class DeleteImageController implements HandlerI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteImageController.class);
    private RegistryHandler registryHandler = new RegistryHandlerImpl();

    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            String repositoryName = paramMap.get("repositoryName")[0];
            String[] tags = paramMap.get("tag");
            HttpClientResult result = null;
            if (tags != null && tags.length > 0) {
                List<String> failedDeleteTags = new ArrayList();
                for (String tag : tags) {
                    result = registryHandler.deleteRepository(repositoryName, tag);
                    if (result.getCode() != 200) {
                        failedDeleteTags.add(tag);
                    }
                }
                if (failedDeleteTags.size() > 0) {
                    response.getWriter().println(new HttpClientResult(500, String.format("delete tags: %s failed!", failedDeleteTags)));
                }
            } else {
                result = registryHandler.deleteRepository(repositoryName);
                response.getWriter().println(result);
            }

        } catch (Exception e) {
            LOGGER.error("server error! {}", ExceptionUtils.getFullStackTrace(e));
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "server error.");
        }
    }
}
