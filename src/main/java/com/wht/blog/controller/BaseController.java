package com.wht.blog.controller;

import com.wht.blog.service.JwtService;
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
    @Resource
    private JwtService jwtService;

    Integer getLoginUserId() {
        String jwt = Method.getJwtFromRequest(request);
        return jwtService.getLoginUserId(jwt);
    }
}
