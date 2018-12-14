import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.models.*;
import io.kubernetes.client.util.Config;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fighter.
 */
public class K8STest {
    static ApiClient client = Config.fromUserPassword("https://192.168.11.222:6443", "admin", "admin")
            .setVerifyingSsl(false).setDebugging(true);
    AppsV1Api appsV1Api = new AppsV1Api(client);
    CoreV1Api api = new CoreV1Api(client);
    ExtensionsV1beta1Api beta1api = new ExtensionsV1beta1Api(client);

//    String yamlContent = FileUtils.readFileToString(new File("G:\\IdeaProjects\\data-preprocessing-executor\\src\\test\\java\\logcollection.yaml"), "UTF-8");
    String yamlContent = FileUtils.readFileToString(new File("/root/IdeaProjects/data-preprocessing-executor/src/test/java/logcollection.yaml"), "UTF-8");

    public K8STest() throws IOException {
    }

    @Test
    public void addConfigMapTest() throws ApiException {
        String namespaceName = "default";
        String deploymentName = "deployment";

        V1ConfigMap configMap = new V1ConfigMap();
        configMap.setApiVersion("v1");
        configMap.setKind("ConfigMap");

        V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
        String configMapName = new StringBuffer(namespaceName)
                .append(".")
                .append(deploymentName)
                .append(".")
                .append("fc")
                .toString();

        v1ObjectMeta.setName(configMapName);
        configMap.setMetadata(v1ObjectMeta);

        Map<String, String> dataMap = new HashMap<>();
        String dataVal = new StringBuffer(namespaceName)
                .append("-")
                .append(deploymentName)
                .append("-log")
                .toString();

        dataMap.put("filebeat.yml", "filebeat.prospectors:\n- input_type: log\n  paths:\n    - \"/log/*\"\noutput.elasticsearch:\n  hosts: [\"192.168.11.218:9200\"]\n  index: \"" + dataVal + "\"");
        configMap.setData(dataMap);
        System.out.println(api.createNamespacedConfigMap(namespaceName, configMap, null));
    }

    @Test
    public void deleteConfigMapTest() throws ApiException {
        String namespaceName = "default";
        String deploymentName = "deployment";
        String configMapName = new StringBuffer(namespaceName)
                .append(".")
                .append(deploymentName)
                .append(".")
                .append("fc")
                .toString();
        System.out.println(api.deleteNamespacedConfigMap(configMapName, namespaceName, new V1DeleteOptions(), null, null, false, null));
    }

    @Test
    public void createDeploymentTest() throws ApiException, IOException {
        List<Object> ls = io.kubernetes.client.util.Yaml.loadAll(yamlContent);
        System.out.println(ls.toString());

        ExtensionsV1beta1Deployment deployment = (ExtensionsV1beta1Deployment) ls.get(0);
        V1ConfigMap configMap = (V1ConfigMap) ls.get(1);

        //deploment1
        deployment.getMetadata().setName("logcollection-test1");
        V1PodSpec spec = deployment.getSpec().getTemplate().getSpec();
        List<V1Container> containers = spec.getContainers();
        containers.get(1).setImage("192.168.11.200/hlg_web/datalower:1.1");
        spec.getVolumes().get(1).getConfigMap().setName("default.deployment.fc");

        //configmap1
        configMap.getMetadata().setName("default.deployment.fc");
        configMap.getData().put("filebeat.yml", "filebeat.prospectors:\n- input_type: log\n  paths:\n    - \"/log/*\"\noutput.elasticsearch:\n  hosts: [\"192.168.11.218:9200\"]\n  index: \"" + "logcollection-test2" + "\"");

        V1ConfigMap v1cf = api.createNamespacedConfigMap("default", configMap, null);
        ExtensionsV1beta1Deployment ev1d = beta1api.createNamespacedDeployment("default", deployment, null);
        System.out.println(v1cf);
        System.out.println(ev1d);
    }

    @Test
    public void deleteDeploymentTest() throws ApiException, FileNotFoundException {
        String namespaceName = "default";
        String deploymentName = "logcollection-test1";
        String configMapName = "default.deployment.fc";
        V1Status status1 = appsV1Api.deleteNamespacedDeployment(deploymentName, namespaceName, new V1DeleteOptions(), null, null, null, null);
        V1Status status = api.deleteNamespacedConfigMap(configMapName, namespaceName, new V1DeleteOptions(), null, null, null, null);
        System.out.println(status);
        System.out.println(status1);
    }

    @Test
    public void readDeploymentTest() throws ApiException, FileNotFoundException {
        ExtensionsV1beta1Deployment deployment = beta1api.readNamespacedDeployment("logcollection-test1","default",null,null,null);
        System.out.println(deployment);
    }
}
