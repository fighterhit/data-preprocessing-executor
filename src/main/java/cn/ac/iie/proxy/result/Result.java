package cn.ac.iie.proxy.result;

/**
 * @author Fighter Created on 2018/10/9.
 */
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    /**
     * 成功时调用
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    /**
     * 失败时调用
     */
    public static <T> Result<T> error(CodeMsg codeMsg) {
        return new Result<T>(codeMsg);
    }

    private Result(T data) {
        this.data = data;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(CodeMsg codeMsg) {
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

}