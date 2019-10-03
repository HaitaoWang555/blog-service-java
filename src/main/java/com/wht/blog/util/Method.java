package com.wht.blog.util;

import com.wht.blog.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 通用方法
 *
 * @author wht
 * @since 2019-09-22 17:22
 */
public class Method {
    /**
     * 获取session中的users对象
     *
     * @return session中的用户
     */
    public static User getLoginUser() {
        HttpSession session = getSession();
        if (null == session) {
            return null;
        }
        User user = (User) session.getAttribute(Const.USER_SESSION_KEY);
        return user;
    }
    public static Integer getLoginUserId() {
        Integer userId = 0;
        User user = getLoginUser();
        if (user != null) {
            userId = user.getId();
        }
        return userId;
    }
    /**
     * 获取session
     *
     * @return {@link HttpSession}
     */
    private static HttpSession getSession() {
        HttpSession session = null;
        try {
            session = getRequest().getSession();
        } catch (Exception ignored) {
        }
        return session;
    }

    /**
     * 获取request
     *
     * @return {@link HttpServletRequest}
     */
    private static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attrs != null;
        return attrs.getRequest();
    }
    /**
     * 获取字符串md5值(加盐)
     *
     * @param str 字符串
     * @return 加密的字符串
     */
    public static String getMd5(String str) {
        String base = str + Const.MD5_SLAT;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    /**
     * 获取ip
     *
     * @return 访问ip
     */
    public static String getIp() {
        String unknown = "unknown";
        // Nginx反向代理IP
        String ip = getRequest().getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = getRequest().getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = getRequest().getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = getRequest().getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = getRequest().getRemoteAddr();
        }
        return ip;
    }
    /**
     * 获取agent
     *
     * @return User-Agent信息
     */
    public static String getAgent() {
        return getRequest().getHeader(HttpHeaders.USER_AGENT);

    }

    public static User addUser(String username, String email, String screen_name, String password) {
        User user = new User();
        user.setUsername(StringUtils.trim(username));
        user.setEmail(email);
        user.setScreenName(screen_name);
        String passwordMd5 = Method.getMd5(password);
        user.setPasswordMd5(passwordMd5);
        user.setLogged(new Date());
        user.setCreated(new Date());
        return user;
    }
}
