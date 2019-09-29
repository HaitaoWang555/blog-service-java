package com.wht.blog.service;

import com.wht.blog.dao.LogMapper;
import com.wht.blog.entity.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wht
 * @since 2019-09-25 9:33
 */
@Service
public class LogService {
    @Resource
    private LogMapper logMapper;

    public void save(String action, String data, String message, String type, String ip) {
        this.save(action, data, message, type, ip, null);
    }
    public void save(String action, String data, String message, String type) {
        this.save(action, data, message, type, null, null);
    }

    public void save(String action, String data, String message, String type, String ip, Integer userId) {
        Log log = new Log();
        log.setAction(action);
        log.setData(data);
        log.setMessage(message);
        log.setType(type);
        log.setIp(ip);
        log.setUserId(userId);
        logMapper.insertSelective(log);
    }
}
