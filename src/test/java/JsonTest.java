import cn.ac.iie.di.dpp.entity.Tag;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fighter Created on 2018/9/29.
 */
public class JsonTest {

    @Test
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
