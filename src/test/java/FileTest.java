import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fighter Created on 2018/10/12.
 */
public class FileTest {
    @Test
    void testFileFunc() throws IOException {
//        String fileName  = "G:\\IdeaProjects\\docker-registry-proxy\\src\\test\\java\\image_1.0.tar";
//        File file = new File("G:\\IdeaProjects\\docker-registry-proxy\\src\\test\\java\\image_1.0.tar");
//        System.out.println(fileName.substring(0,fileName.lastIndexOf(".")));
//        Files.createDirectory(Paths.get(fileName.substring(0,fileName.lastIndexOf("."))));
        //        System.out.println(file.getParent() + File.separator + file.getName());
        //        System.out.println(file.getName());
//        System.out.println(file.getParent() + File.separator + file.getName().substring(0, file.getName().lastIndexOf('.')));

        /*
        Map map = new HashMap();
        map.put("1",1);
        modifyStr(map);
        System.out.println(map);*/
//        System.out.println(Files.exists(Paths.get("G:\\IdeaProjects\\docker-registry-proxy\\src\\test\\java\\DEProxyChallengeHandler.ava")));
        Map map = new HashMap();
        map.put("1", 1);
        map.put("2", 2);
        System.out.println(JSON.toJSONString(map));
    }

    static String modifyStr() throws Exception {
        String ret = null;
        try {
            //some code
            ret = "xxxx";
        } catch (Exception e) {
//            throw new Exception(e);
            e.printStackTrace();
        }
        return ret;
    }
}
