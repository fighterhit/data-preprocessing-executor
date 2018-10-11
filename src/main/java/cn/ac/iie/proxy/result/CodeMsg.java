package cn.ac.iie.proxy.result;

/**
 * @author Fighter Created on 2018/10/9.
 */
public class CodeMsg {

    private int code;
    private String msg;

    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(-1, "server error!");

    //docker engine模块 2XX
    public static CodeMsg LOAD_IMAGE_ERROR = new CodeMsg(210, "Load image error!");
    public static CodeMsg TAG_IMAGE_ERROR = new CodeMsg(211, "Tag image error!");
    public static CodeMsg PUSH_IMAGE_ERROR = new CodeMsg(212, "Push image error!");
    public static CodeMsg PULL_IMAGE_ERROR = new CodeMsg(213, "Pull image error!");
    public static CodeMsg REMOVE_IMAGE_ERROR = new CodeMsg(214, "Remove image error!");
    public static CodeMsg LIST_IMAGE_ERROR = new CodeMsg(215, "List image error!");

    //docker registry模块 3XX
    public static CodeMsg LIST_REGISTRY_ERROR = new CodeMsg(310, "List registry error!");
    public static CodeMsg LIST_REPOSITORY_ERROR = new CodeMsg(311, "List repository error!");
    public static CodeMsg REMOVE_IMAGE_REGISTRY_ERROR = new CodeMsg(312, "remove image in registry error!");
    public static CodeMsg LIST_TAG_ERROR = new CodeMsg(313, "List tag error!");

    private CodeMsg() {
    }

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

    /**
     * 返回带参数的错误码
     *
     * @param args
     * @return
     */
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", msg=" + msg + "]";
    }
}
