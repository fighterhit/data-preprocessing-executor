/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ac.iie.di.dpp.k8s.util;

import com.google.gson.JsonSyntaxException;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.AutoscalingV2beta1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.models.ExtensionsV1beta1Deployment;
import io.kubernetes.client.models.ExtensionsV1beta1DeploymentSpec;
import io.kubernetes.client.models.V1Container;
import io.kubernetes.client.models.V1ContainerPort;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1PodSpec;
import io.kubernetes.client.models.V1PodTemplateSpec;
import io.kubernetes.client.models.V1ResourceQuota;
import io.kubernetes.client.models.V1ResourceQuotaSpec;
import io.kubernetes.client.models.V1ResourceRequirements;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServicePort;
import io.kubernetes.client.models.V1ServiceSpec;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.models.V2beta1CrossVersionObjectReference;
import io.kubernetes.client.models.V2beta1HorizontalPodAutoscaler;
import io.kubernetes.client.models.V2beta1HorizontalPodAutoscalerSpec;
import io.kubernetes.client.models.V2beta1MetricSpec;
import io.kubernetes.client.models.V2beta1ResourceMetricSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
public class K8sUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(K8sUtil.class);

    public V1Namespace CreateNameSpace(CoreV1Api api, String namespaceName) throws ApiException {
        V1Namespace v1n = new V1Namespace();
        v1n.setApiVersion("v1");
        v1n.setKind("Namespace");
        V1ObjectMeta v1om = new V1ObjectMeta();
        v1om.setName(namespaceName);
        v1n.setMetadata(v1om);
        return api.createNamespace(v1n, "false");
    }

    public V1Namespace ReplaceNameSpace(CoreV1Api api, String namespaceName) throws Exception {
        throw new Exception("not support yet.");
    }

    public void DeleteNameSpace(CoreV1Api api, String namespaceName) throws ApiException, Exception {
        try {
            api.deleteNamespace(namespaceName, new V1DeleteOptions(), "false", null, null, null);
        } catch (JsonSyntaxException e) {
            if (e.getCause() instanceof IllegalStateException) {
                IllegalStateException ise = (IllegalStateException) e.getCause();
                if (ise.getMessage() != null && ise.getMessage().contains("Expected a string but was BEGIN_OBJECT")) {
                    LOGGER.info("Delete Namespace " + namespaceName + " Catching exception because of issue https://github.com/kubernetes-client/java/issues/86" + e);
                } else {
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    public V1ResourceQuota CreateResourceQuota(CoreV1Api api, String namespaceName,
            long podLimit, double cpuRequest, long memoryRequest, double cpuLimit, long memoryLimit) throws ApiException {
        V1ResourceQuota v1rq = new V1ResourceQuota();
        v1rq.apiVersion("v1");
        v1rq.setKind("ResourceQuota");
        V1ObjectMeta v1om = new V1ObjectMeta();
        v1om.setName(namespaceName);
        v1rq.setMetadata(v1om);
        V1ResourceQuotaSpec v1rqs = new V1ResourceQuotaSpec();
        Map<String, Quantity> qmap = new HashMap<>();
        qmap.put("pods", new Quantity(BigDecimal.valueOf(podLimit), Quantity.Format.DECIMAL_SI));
        qmap.put("requests.cpu", new Quantity(BigDecimal.valueOf(cpuRequest), Quantity.Format.DECIMAL_SI));
        qmap.put("requests.memory", new Quantity(BigDecimal.valueOf(memoryRequest), Quantity.Format.BINARY_SI));
        qmap.put("limits.cpu", new Quantity(BigDecimal.valueOf(cpuLimit), Quantity.Format.DECIMAL_SI));
        qmap.put("limits.memory", new Quantity(BigDecimal.valueOf(memoryLimit), Quantity.Format.BINARY_SI));
        v1rqs.setHard(qmap);
        v1rq.setSpec(v1rqs);
        return api.createNamespacedResourceQuota(namespaceName, v1rq, "false");
    }

    public V1ResourceQuota ReplaceResourceQuota(CoreV1Api api, String namespaceName, String resourceQuotaName,
            long podLimit, double cpuRequest, long memoryRequest, double cpuLimit, long memoryLimit) throws ApiException {
        V1ResourceQuota v1rq = new V1ResourceQuota();
        v1rq.apiVersion("v1");
        v1rq.setKind("ResourceQuota");
        V1ObjectMeta v1om = new V1ObjectMeta();
        v1om.setName(namespaceName);
        v1rq.setMetadata(v1om);
        V1ResourceQuotaSpec v1rqs = new V1ResourceQuotaSpec();
        Map<String, Quantity> qmap = new HashMap<>();
        qmap.put("pods", new Quantity(BigDecimal.valueOf(podLimit), Quantity.Format.DECIMAL_SI));
        qmap.put("requests.cpu", new Quantity(BigDecimal.valueOf(cpuRequest), Quantity.Format.DECIMAL_SI));
        qmap.put("requests.memory", new Quantity(BigDecimal.valueOf(memoryRequest), Quantity.Format.BINARY_SI));
        qmap.put("limits.cpu", new Quantity(BigDecimal.valueOf(cpuLimit), Quantity.Format.DECIMAL_SI));
        qmap.put("limits.memory", new Quantity(BigDecimal.valueOf(memoryLimit), Quantity.Format.BINARY_SI));
        v1rqs.setHard(qmap);
        v1rq.setSpec(v1rqs);
        return api.replaceNamespacedResourceQuota(resourceQuotaName, namespaceName, v1rq, "false");
    }

    public void DeleteResourceQuota(CoreV1Api api, String resourceQuotaName, String namespaceName) throws ApiException, Exception {
        V1DeleteOptions rqDeleteOptions = new V1DeleteOptions();
        try {
            api.deleteNamespacedResourceQuota(resourceQuotaName, namespaceName, rqDeleteOptions, "false", null, null, null);
        } catch (JsonSyntaxException e) {
            if (e.getCause() instanceof IllegalStateException) {
                IllegalStateException ise = (IllegalStateException) e.getCause();
                if (ise.getMessage() != null && ise.getMessage().contains("Expected a string but was BEGIN_OBJECT")) {
                    LOGGER.info("Delete ResourceQuota " + resourceQuotaName + " Catching exception because of issue https://github.com/kubernetes-client/java/issues/86" + e);
                } else {
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    public V1Service CreateService(CoreV1Api api, String namespaceName, String serviceName, int containerPort) throws ApiException {
        V1Service v1s = new V1Service();
        V1ObjectMeta v1o = new V1ObjectMeta();
        v1o.setName(serviceName);
        v1o.setNamespace(namespaceName);
        Map<String, String> labels = new HashMap();
        labels.put("run", serviceName);
        v1o.setLabels(labels);
        V1ServiceSpec v1ss = new V1ServiceSpec();
        List<V1ServicePort> v1spList = new ArrayList<>();
        V1ServicePort v1sp = new V1ServicePort();
        v1sp.setPort(containerPort);
        v1sp.setProtocol("TCP");
        v1spList.add(v1sp);
        v1ss.setPorts(v1spList);
        Map<String, String> selector = new HashMap();
        selector.put("run", serviceName);
        v1ss.setSelector(selector);
        v1ss.setType("NodePort");
        v1s.setApiVersion("v1");
        v1s.setKind("Service");
        v1s.setMetadata(v1o);
        v1s.setSpec(v1ss);
        System.out.println(v1s);
        return api.createNamespacedService(namespaceName, v1s, "false");
    }

    public V1Service ReplaceService(CoreV1Api api, String namespaceName, String image, int nodePort) throws Exception {
        throw new Exception("not support yet.");
    }

    public V1Status DeleteService(CoreV1Api api, String serviceName, String namespaceName) throws Exception {
        return api.deleteNamespacedService(serviceName, namespaceName, new V1DeleteOptions(), null, null, null, null);
    }

    public ExtensionsV1beta1Deployment CreateDeployment(ExtensionsV1beta1Api beta1api, String namespaceName, String deploymentName,
            String image, int replicaRequest, double podcpuRequest, double podcpuLimit,
            long podmemoryRequest, long podmemoryLimit, int containerPort) throws ApiException {
        ExtensionsV1beta1Deployment ev1bd = new ExtensionsV1beta1Deployment();
        V1ObjectMeta v1om = new V1ObjectMeta();
        v1om.setName(deploymentName);
        v1om.setNamespace(namespaceName);
        ExtensionsV1beta1DeploymentSpec ev1bds = new ExtensionsV1beta1DeploymentSpec();
        ev1bds.setReplicas(replicaRequest);
        V1PodTemplateSpec v1pts = new V1PodTemplateSpec();
        V1ObjectMeta v1ompod = new V1ObjectMeta();
        Map<String, String> labels = new HashMap();
        labels.put("run", deploymentName);
        v1ompod.setLabels(labels);
        V1PodSpec v1ps = new V1PodSpec();
        V1Container v1c = new V1Container();
        v1c.setImage(image);
        v1c.setName(deploymentName);
        V1ContainerPort v1cp = new V1ContainerPort();
        v1cp.containerPort(containerPort);
        v1c.addPortsItem(v1cp);
        V1ResourceRequirements v1rr = new V1ResourceRequirements();
        v1rr.putRequestsItem("cpu", new Quantity(BigDecimal.valueOf(podcpuRequest), Quantity.Format.DECIMAL_SI));
        v1rr.putRequestsItem("memory", new Quantity(BigDecimal.valueOf(podmemoryRequest), Quantity.Format.BINARY_SI));
        v1rr.putLimitsItem("cpu", new Quantity(BigDecimal.valueOf(podcpuLimit), Quantity.Format.DECIMAL_SI));
        v1rr.putLimitsItem("memory", new Quantity(BigDecimal.valueOf(podmemoryLimit), Quantity.Format.BINARY_SI));
        v1c.setResources(v1rr);
        v1ps.addContainersItem(v1c);
        v1pts.setMetadata(v1ompod);
        v1pts.setSpec(v1ps);
        ev1bds.setTemplate(v1pts);
        ev1bd.setApiVersion("extensions/v1beta1");
        ev1bd.setKind("Deployment");
        ev1bd.setMetadata(v1om);
        ev1bd.setSpec(ev1bds);
        return beta1api.createNamespacedDeployment(namespaceName, ev1bd, "false");
    }

    public ExtensionsV1beta1Deployment ReplaceDeployment(ExtensionsV1beta1Api beta1api, String namespaceName, String deploymentName,
            String image, int replicaRequest, double podcpuRequest, double podcpuLimit,
            long podmemoryRequest, long podmemoryLimit, int containerPort) throws ApiException {
        ExtensionsV1beta1Deployment ev1bd = new ExtensionsV1beta1Deployment();
        V1ObjectMeta v1om = new V1ObjectMeta();
        v1om.setName(deploymentName);
        v1om.setNamespace(namespaceName);
        ExtensionsV1beta1DeploymentSpec ev1bds = new ExtensionsV1beta1DeploymentSpec();
        ev1bds.setReplicas(replicaRequest);
        V1PodTemplateSpec v1pts = new V1PodTemplateSpec();
        V1ObjectMeta v1ompod = new V1ObjectMeta();
        Map<String, String> labels = new HashMap();
        labels.put("run", deploymentName);
        v1ompod.setLabels(labels);
        V1PodSpec v1ps = new V1PodSpec();
        V1Container v1c = new V1Container();
        v1c.setImage(image);
        v1c.setName(deploymentName);
        V1ContainerPort v1cp = new V1ContainerPort();
        v1cp.containerPort(containerPort);
        v1c.addPortsItem(v1cp);
        V1ResourceRequirements v1rr = new V1ResourceRequirements();
        v1rr.putRequestsItem("cpu", new Quantity(BigDecimal.valueOf(podcpuRequest), Quantity.Format.DECIMAL_SI));
        v1rr.putRequestsItem("memory", new Quantity(BigDecimal.valueOf(podmemoryRequest), Quantity.Format.BINARY_SI));
        v1rr.putLimitsItem("cpu", new Quantity(BigDecimal.valueOf(podcpuLimit), Quantity.Format.DECIMAL_SI));
        v1rr.putLimitsItem("memory", new Quantity(BigDecimal.valueOf(podmemoryLimit), Quantity.Format.BINARY_SI));
        v1c.setResources(v1rr);
        v1ps.addContainersItem(v1c);
        v1pts.setMetadata(v1ompod);
        v1pts.setSpec(v1ps);
        ev1bds.setTemplate(v1pts);
        ev1bd.setApiVersion("extensions/v1beta1");
        ev1bd.setKind("Deployment");
        ev1bd.setMetadata(v1om);
        ev1bd.setSpec(ev1bds);
        return beta1api.replaceNamespacedDeployment(deploymentName, namespaceName, ev1bd, "false");
    }

    public V1Status DeleteDeployment(AppsV1Api appsV1Api, String deploymentName, String namespaceName) throws ApiException {
        return appsV1Api.deleteNamespacedDeployment(deploymentName, namespaceName, new V1DeleteOptions(), null, null, null, null);
    }

    public V2beta1HorizontalPodAutoscaler CreateHorizontalPodAutoscaler(AutoscalingV2beta1Api asV2Api, String phaName,
            String namespaceName, int replicaRequest, int replicaLimit,
            int podcpuThreshold, int podmemoryThreshold) throws ApiException {
        V2beta1HorizontalPodAutoscaler v2bhpa = new V2beta1HorizontalPodAutoscaler();
        V1ObjectMeta v1om = new V1ObjectMeta();
        v1om.setName(phaName);
        v1om.setNamespace(namespaceName);
        V2beta1HorizontalPodAutoscalerSpec v2bhpas = new V2beta1HorizontalPodAutoscalerSpec();
        v2bhpas.setMaxReplicas(replicaLimit);
        List<V2beta1MetricSpec> v2bmsList = new ArrayList<>();
        V2beta1MetricSpec v2bmsMemory = new V2beta1MetricSpec();
        V2beta1ResourceMetricSource v2brmsMemory = new V2beta1ResourceMetricSource();
        v2brmsMemory.setName("memory");
        v2brmsMemory.setTargetAverageUtilization(podmemoryThreshold);
        v2bmsMemory.setResource(v2brmsMemory);
        v2bmsMemory.setType("Resource");
        V2beta1MetricSpec v2bmsCpu = new V2beta1MetricSpec();
        V2beta1ResourceMetricSource v2brmsCpu = new V2beta1ResourceMetricSource();
        v2brmsCpu.setName("cpu");
        v2brmsCpu.setTargetAverageUtilization(podcpuThreshold);
        v2bmsCpu.setResource(v2brmsCpu);
        v2bmsCpu.setType("Resource");
        v2bmsList.add(v2bmsMemory);
        v2bmsList.add(v2bmsCpu);
        v2bhpas.setMetrics(v2bmsList);
        v2bhpas.setMinReplicas(replicaRequest);
        V2beta1CrossVersionObjectReference v2bcvor = new V2beta1CrossVersionObjectReference();
        v2bcvor.setApiVersion("apps/v1beta1");
        v2bcvor.setKind("Deployment");
        v2bcvor.setName(phaName);
        v2bhpas.setScaleTargetRef(v2bcvor);
        v2bhpa.setApiVersion("autoscaling/v2beta1");
        v2bhpa.setKind("HorizontalPodAutoscaler");
        v2bhpa.setMetadata(v1om);
        v2bhpa.setSpec(v2bhpas);
        return asV2Api.createNamespacedHorizontalPodAutoscaler(namespaceName, v2bhpa, "false");
    }

    public V2beta1HorizontalPodAutoscaler ReplaceHorizontalPodAutoscaler(AutoscalingV2beta1Api asV2Api, String phaName,
            String namespaceName, int replicaRequest, int replicaLimit,
            int podcpuThreshold, int podmemoryThreshold) throws ApiException {
        V2beta1HorizontalPodAutoscaler v2bhpa = new V2beta1HorizontalPodAutoscaler();
        V1ObjectMeta v1om = new V1ObjectMeta();
        v1om.setName(phaName);
        v1om.setNamespace(namespaceName);
        V2beta1HorizontalPodAutoscalerSpec v2bhpas = new V2beta1HorizontalPodAutoscalerSpec();
        v2bhpas.setMaxReplicas(replicaLimit);
        List<V2beta1MetricSpec> v2bmsList = new ArrayList<>();
        V2beta1MetricSpec v2bmsMemory = new V2beta1MetricSpec();
        V2beta1ResourceMetricSource v2brmsMemory = new V2beta1ResourceMetricSource();
        v2brmsMemory.setName("memory");
        v2brmsMemory.setTargetAverageUtilization(podmemoryThreshold);
        v2bmsMemory.setResource(v2brmsMemory);
        v2bmsMemory.setType("Resource");
        V2beta1MetricSpec v2bmsCpu = new V2beta1MetricSpec();
        V2beta1ResourceMetricSource v2brmsCpu = new V2beta1ResourceMetricSource();
        v2brmsCpu.setName("cpu");
        v2brmsCpu.setTargetAverageUtilization(podcpuThreshold);
        v2bmsCpu.setResource(v2brmsCpu);
        v2bmsCpu.setType("Resource");
        v2bmsList.add(v2bmsMemory);
        v2bmsList.add(v2bmsCpu);
        v2bhpas.setMetrics(v2bmsList);
        v2bhpas.setMinReplicas(replicaRequest);
        V2beta1CrossVersionObjectReference v2bcvor = new V2beta1CrossVersionObjectReference();
        v2bcvor.setApiVersion("apps/v1beta1");
        v2bcvor.setKind("Deployment");
        v2bcvor.setName(phaName);
        v2bhpas.setScaleTargetRef(v2bcvor);
        v2bhpa.setApiVersion("autoscaling/v2beta1");
        v2bhpa.setKind("HorizontalPodAutoscaler");
        v2bhpa.setMetadata(v1om);
        v2bhpa.setSpec(v2bhpas);
        return asV2Api.replaceNamespacedHorizontalPodAutoscaler(phaName, namespaceName, v2bhpa, "false");
    }

    public V1Status DeleteHorizontalPodAutoscaler(AutoscalingV2beta1Api asV2Api, String phaName, String namespaceName) throws ApiException {
        V1DeleteOptions deo = new V1DeleteOptions();
        return asV2Api.deleteNamespacedHorizontalPodAutoscaler(phaName, namespaceName, deo, "false", Integer.MAX_VALUE, Boolean.TRUE, null);
    }
}