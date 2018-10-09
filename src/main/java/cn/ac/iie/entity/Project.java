package cn.ac.iie.entity;

/**
 * @author Fighter Created on 2018/9/28.
 */
public class Project {

    private int project_id;
    private int owner_id;
    private String name;
    private String creation_time;
    private String update_time;
    private boolean deleted;
    private String owner_name;
    private boolean togglable;
    private int current_user_role_id;
    private int repo_count;
    private MetadataBean metadata;

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public boolean isTogglable() {
        return togglable;
    }

    public void setTogglable(boolean togglable) {
        this.togglable = togglable;
    }

    public int getCurrent_user_role_id() {
        return current_user_role_id;
    }

    public void setCurrent_user_role_id(int current_user_role_id) {
        this.current_user_role_id = current_user_role_id;
    }

    public int getRepo_count() {
        return repo_count;
    }

    public void setRepo_count(int repo_count) {
        this.repo_count = repo_count;
    }

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public static class MetadataBean {

        private String publicX;

        public String getPublicX() {
            return publicX;
        }

        public void setPublicX(String publicX) {
            this.publicX = publicX;
        }
    }

}
