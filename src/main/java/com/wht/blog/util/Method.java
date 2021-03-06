package com.wht.blog.util;

import com.wht.blog.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用方法
 *
 * @author wht
 * @since 2019-09-22 17:22
 */
public class Method {

    /**
     * 从 request 的 header 中获取 JWT
     * @param request 请求
     * @return JWT
     */
    public static String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public static String getJwt() {
        return getJwtFromRequest(getRequest());
    }

    /**
     * 获取request
     *
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
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
    /**
     * 过滤html标签
     */
    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?</script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?</style>"; //定义style的正则表达式
        String regEx_string = "&nbsp;"; //定义特殊字符串
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
        htmlStr = getString(htmlStr, regEx_style, regEx_script);
        htmlStr = getString(htmlStr, regEx_string, regEx_html);
        return htmlStr.trim(); //返回文本字符串
    }

    private static String getString(String htmlStr, String regEx_string, String regEx_html) {
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签
        Pattern p_string = Pattern.compile(regEx_string, Pattern.CASE_INSENSITIVE);
        Matcher m_string = p_string.matcher(htmlStr);
        htmlStr = m_string.replaceAll(""); //过滤特殊字符
        return htmlStr;
    }

    /**
     * 创建上传文件路径
     * @param model 模块
     * @return upload - 模块 - 用户ID - 时间 - 文件名
     */
    public static String createFilePath(String model, String userId) {
        String fileTempPath = "upload/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String format = sdf.format(new Date());
        return fileTempPath + model + "/" + userId + "/" + format + "/";
    }
}
