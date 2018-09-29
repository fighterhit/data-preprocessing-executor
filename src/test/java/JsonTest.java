import cn.ac.iie.entity.HttpClientResult;
import cn.ac.iie.entity.Project;
import cn.ac.iie.entity.Repository;
import cn.ac.iie.entity.Tag;
import cn.ac.iie.util.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Fighter Created on 2018/9/29.
 */
public class JsonTest {
    public static void main(String[] args) {

    }

    private String harborBaseAPI = "http://192.168.11.112/api";

    @Test
    void testListRepostories() {
        List<Repository> repositories = new ArrayList<>();
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            Map<String, String> params = new HashMap<>();
            params.put("project_id", "2");
            String reqUrl = new StringBuffer(harborBaseAPI).append("/").append("repositories").toString();
            HttpClientResult result = HttpClientUtils.doGet(reqUrl, headers, params);
            JSONArray jsonArray = JSON.parseArray(result.getContent());
            List<Repository> ls = jsonArray.toJavaList(Repository.class);
            ls.stream().forEach((e) -> System.out.println(e.getName()));
        } catch (Exception e) {
        }
    }

    @Test
    void testListProjects() {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            Map<String, String> params = new HashMap<>();
            params.put("project_id", "2");
            String reqUrl = new StringBuffer(harborBaseAPI).append("/").append("projects").toString();
            HttpClientResult result = HttpClientUtils.doGet(reqUrl, headers, params);
            JSONArray jsonArray = JSON.parseArray(result.getContent());
            List<Project> ls = jsonArray.toJavaList(Project.class);
            ls.stream().forEach(e -> System.out.println(e.getName()));
        } catch (Exception e) {
        }
    }

    @Test
    void testlistTagsOfRepository() {
        List<String> tags = new ArrayList<>();
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            String reqUrl = new StringBuffer("http://192.168.11.112/api")
                    .append("/repositories/")
                    .append("hlg_web/busybox")
                    .append("/tags")
                    .toString();
            HttpClientResult result = HttpClientUtils.doGet(reqUrl, headers, null);
            JSONArray jsonArray = JSON.parseArray(result.getContent());
            tags = jsonArray.toJavaList(Tag.class).stream().map(Tag::getName).collect(Collectors.toList());
            System.out.println(tags);
        } catch (Exception e) {
        }
    }


    @Test
    void deleteRepository() {
        int retCode = -1;
        HttpClientResult result;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            String reqUrl = new StringBuffer(harborBaseAPI)
                    .append("/repositories/")
                    .append("hlg_web/busybox")
                    .toString();
            result = HttpClientUtils.doDelete(reqUrl, headers);
            System.out.println(result.getCode());
            System.out.println(result.getContent());
        } catch (Exception e) {
        }
    }

    @Test
    void deleteRepository(Tag tag){
        int retCode = -1;
        HttpClientResult result;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("authorization", "Basic YWRtaW46SGFyYm9yMTIzNDU=");
            String reqUrl = new StringBuffer(harborBaseAPI)
                    .append("/repositories/")
                    .append("hlg_web/busybox")
                    .append("/tags/")
                    .append("harbortest")
                    .toString();
            result = HttpClientUtils.doDelete(reqUrl, headers);
            System.out.println(result.getCode());
            System.out.println(result.getContent());
        } catch (Exception e) {
        }
    }

    void JsonTest() {
        String json = "[\n" +
                "  {\n" +
                "    \"digest\": \"sha256:54b2d12e8b7c3337ec340be45022745ee3b204dbfa2b430e5b958e78eb4a1636\",\n" +
                "    \"name\": \"1.11111\",\n" +
                "    \"size\": 44676964,\n" +
                "    \"architecture\": \"amd64\",\n" +
                "    \"os\": \"linux\",\n" +
                "    \"docker_version\": \"17.06.2-ce\",\n" +
                "    \"author\": \"NGINX Docker Maintainers \\u003cdocker-maint@nginx.com\\u003e\",\n" +
                "    \"created\": \"2018-09-05T00:57:00.322491744Z\",\n" +
                "    \"config\": {\n" +
                "      \"labels\": {\n" +
                "        \"maintainer\": \"NGINX Docker Maintainers \\u003cdocker-maint@nginx.com\\u003e\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"signature\": null,\n" +
                "    \"labels\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"digest\": \"sha256:54b2d12e8b7c3337ec340be45022745ee3b204dbfa2b430e5b958e78eb4a1636\",\n" +
                "    \"name\": \"test\",\n" +
                "    \"size\": 44676964,\n" +
                "    \"architecture\": \"amd64\",\n" +
                "    \"os\": \"linux\",\n" +
                "    \"docker_version\": \"17.06.2-ce\",\n" +
                "    \"author\": \"NGINX Docker Maintainers \\u003cdocker-maint@nginx.com\\u003e\",\n" +
                "    \"created\": \"2018-09-05T00:57:00.322491744Z\",\n" +
                "    \"config\": {\n" +
                "      \"labels\": {\n" +
                "        \"maintainer\": \"NGINX Docker Maintainers \\u003cdocker-maint@nginx.com\\u003e\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"signature\": null,\n" +
                "    \"labels\": []\n" +
                "  }\n" +
                "]";
        JSONArray jsonObject = JSON.parseArray(json);
        List<String> list = jsonObject
                .toJavaList(Tag.class)
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        System.out.println(list.toString());
    }
}
