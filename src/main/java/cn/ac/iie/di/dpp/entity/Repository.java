package cn.ac.iie.di.dpp.entity;

import java.util.List;

/**
 * @author Fighter Created on 2018/9/28.
 */
public class Repository {

    /**
     * id : 51
     * name : hlg_web/nginx
     * project_id : 2
     * description :
     * pull_count : 4
     * star_count : 0
     * tags_count : 2
     * labels : []
     * creation_time : 2018-09-14T02:21:45.31181Z
     * update_time : 2018-09-14T09:14:05.640746Z
     */

    private int id;
    private String name;
    private int project_id;
    private String description;
    private int pull_count;
    private int star_count;
    private int tags_count;
    private String creation_time;
    private String update_time;
    private List<?> labels;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPull_count() {
        return pull_count;
    }

    public void setPull_count(int pull_count) {
        this.pull_count = pull_count;
    }

    public int getStar_count() {
        return star_count;
    }

    public void setStar_count(int star_count) {
        this.star_count = star_count;
    }

    public int getTags_count() {
        return tags_count;
    }

    public void setTags_count(int tags_count) {
        this.tags_count = tags_count;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public List<?> getLabels() {
        return labels;
    }

    public void setLabels(List<?> labels) {
        this.labels = labels;
    }
}
