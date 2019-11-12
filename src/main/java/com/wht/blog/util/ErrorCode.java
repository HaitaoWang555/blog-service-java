package com.wht.blog.util;

/**
 * @author wht
 * @since 2019-09-15 21:39
 */
public enum ErrorCode {
    /**
     * 运行时异常
     */
    RUNTIME(1000, "RuntimeException"),
    /**
     * 空指针异常
     */
    NULL_POINTER(1001, "NullPointerException "),
    /**
     * 类型住转换异常
     */
    CLASS_CAST(1002, "ClassCastException"),
    /**
     * IO异常
     */
    IO(1003, "IOException"),
    /**
     * 找不到方法异常
     */
    NO_SUCH_METHOD(1004, "NoSuchMethodException"),
    /**
     * 数组越界异常
     */
    INDEX_OUTOF_BOUNDS(1005, "IndexOutOfBoundsException"),
    /**
     * 400异常
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * 404异常
     */
    NOT_FOUND(404, "Not Found"),
    /**
     * 方法不允许异常
     */
    METHOD_BOT_ALLOWED(405, "Method Not Allowed"),
    /**
     * 不可到达异常
     */
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    /**
     * 500异常
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    /**
     * 用户未登陆
     */
    NOT_LOGIN(999, "Not Login"),
    /**
     * 用户名或密码错误！
     */
    USERNAME_PASSWORD_ERROR(5001, "用户名或密码错误！"),

    /**
     * token 已过期，请重新登录！
     */
    TOKEN_EXPIRED(5002, "token 已过期，请重新登录！"),

    /**
     * token 解析失败，请尝试重新登录！
     */
    TOKEN_PARSE_ERROR(5002, "token 解析失败，请尝试重新登录！"),

    /**
     * 当前用户已在别处登录，请尝试更改密码或重新登录！
     */
    TOKEN_OUT_OF_CTRL(5003, "当前用户已在别处登录，请尝试更改密码或重新登录！");

    private final int code;
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
