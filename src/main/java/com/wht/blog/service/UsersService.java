package com.wht.blog.service;

import com.wht.blog.dao.UserMapper;
import com.wht.blog.entity.User;
import com.wht.blog.exception.TipException;
import com.wht.blog.util.Method;
import com.wht.blog.util.Types;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wht
 * @since 2019-09-13 12:54
 */
@Service
public class UsersService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private LogService logService;

    public List getAllUser () {
        return userMapper.getAll();
    }
    public User getOneById (int id) {
        return userMapper.selectByPrimaryKey(id);
    }
    public void addUser (User user) {
        int addInt = userMapper.insertSelective(user);
        if (addInt != 0) {
            this.saveLog(user);
        }
    }

    public void updateByPrimaryKeySelective (User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void del (Map<String, String> ids) {
        int delLen = userMapper.deleteByPrimaryKeyBatch(ids);
        if (delLen != 0) {
            this.saveLog(ids.get("ids"));
        }
    }
    
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new TipException("账户不存在");
        }
        String passwordMd5 = Method.getMd5(password);
        if (!user.getPasswordMd5().equals(passwordMd5)) {
            throw new TipException("密码不正确");
        }
        user.setLogged(new Date());
        userMapper.updateByPrimaryKeySelective(user);
        //清空密码
        user.setPasswordMd5(null);
        return user;
    }

    private void saveLog(String ids) {
        logService.save(
                Types.LOG_ACTION_DELETE, "id:" + ids,
                Types.LOG_MESSAGE_DELETE_USER,
                Types.LOG_TYPE_OPERATE,
                Method.getIp(),
                Method.getLoginUserId()
        );
    }
    private void saveLog(User user) {
        logService.save(
                Types.LOG_ACTION_ADD, "name:" + user.getUsername(),
                Types.LOG_MESSAGE_ADD_USER,
                Types.LOG_TYPE_OPERATE,
                Method.getIp(),
                Method.getLoginUserId()
        );
    }
}
