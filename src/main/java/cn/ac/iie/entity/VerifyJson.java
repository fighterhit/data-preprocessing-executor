package cn.ac.iie.entity;

import java.util.List;

/**
 * @author Fighter Created on 2018/10/11.
 */
public class VerifyJson {

    /**
     * imagePath : /tmp/test.tar
     * path : ["/path1","/path2"]
     * files : [["file1.txt"],["file2.txt"]]
     */

    private String imagePath;
    private List<String> path;
    private List<List<String>> files;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public List<List<String>> getFiles() {
        return files;
    }

    public void setFiles(List<List<String>> files) {
        this.files = files;
    }
}
