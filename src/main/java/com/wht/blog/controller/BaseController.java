package com.wht.blog.controller;

import com.wht.blog.entity.User;
import com.wht.blog.util.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 公共类 Controller
 * @author wht
 * @since 2019-09-16 16:37
 */
public abstract class BaseController {
    @Resource
    protected HttpServletRequest request;

    public User user() {
        return Method.getLoginUser();
    }
}
