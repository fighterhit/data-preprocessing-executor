package cn.ac.iie.entity;

import java.util.List;

/**
 * @author Fighter Created on 2018/9/28.
 */
public class Tag {

    /**
     * digest : sha256:19fca0f4a812d0ba4ad89a4c345ce660ecc7c14c1ce9a9c12ac9db1ca62b4602
     * name : test
     * size : 735265
     * architecture : amd64
     * os : linux
     * docker_version : 17.06.2-ce
     * author :
     * created : 2018-07-31T22:20:07.617575594Z
     * config : {"labels":null}
     * signature : null
     * labels : []
     */

    private String digest;
    private String name;
    private int size;
    private String architecture;
    private String os;
    private String docker_version;
    private String author;
    private String created;
    private ConfigBean config;
    private Object signature;
    private List<?> labels;

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDocker_version() {
        return docker_version;
    }

    public void setDocker_version(String docker_version) {
        this.docker_version = docker_version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public ConfigBean getConfig() {
        return config;
    }

    public void setConfig(ConfigBean config) {
        this.config = config;
    }

    public Object getSignature() {
        return signature;
    }

    public void setSignature(Object signature) {
        this.signature = signature;
    }

    public List<?> getLabels() {
        return labels;
    }

    public void setLabels(List<?> labels) {
        this.labels = labels;
    }

    public static class ConfigBean {
        /**
         * labels : null
         */

        private Object labels;

        public Object getLabels() {
            return labels;
        }

        public void setLabels(Object labels) {
            this.labels = labels;
        }
    }
}
