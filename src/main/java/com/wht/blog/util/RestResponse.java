package com.wht.blog.util;

import lombok.Data;

/**
 * json通用返回类
 *
 * @author wht
 * @since 2019-09-15 21:45
 */
@Data
public class RestResponse<T> {

    private int code;
    private String msg;
    private T data;
    private boolean success;

    private RestResponse() {
    }

    private RestResponse(boolean success) {
        this.success = success;
    }

    private RestResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    private RestResponse(boolean success, int code) {
        this.success = success;
        this.code = code;
    }

    private RestResponse(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }
    private RestResponse(boolean success, T data, int code) {
        this.success = success;
        this.data = data;
        this.code = code;
    }
    private RestResponse(boolean success, int code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }
    private RestResponse(boolean success,T data, String msg) {
        this.success = success;
        this.data = data;
        this.msg = msg;
    }

    public static <T> RestResponse ok() {
        return new RestResponse(true);
    }

    public static <T> RestResponse ok(T data) {
        if (data == null) {
            return new RestResponse<>(true, "暂无数据");
        }
        return new RestResponse<>(true, data);
    }

    public static RestResponse ok(String msg) {
        return new RestResponse(true, msg);
    }

    public static <T> RestResponse ok(T data, int code) {
        return new RestResponse<>(true, data, code);
    }

    public static <T> RestResponse ok(T data, String msg) {
        return new RestResponse<>(true, data, msg);
    }

    public static RestResponse fail() {
        return new RestResponse(false);
    }

    public static RestResponse fail(int code) {
        return new RestResponse(false, code);
    }

    public static RestResponse fail(String msg) {
        return new RestResponse(false, msg);
    }

    public static RestResponse fail(int code, String msg) {
        return new RestResponse(false, code, msg);
    }
}
