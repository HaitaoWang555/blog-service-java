package com.wht.blog.service;

import com.wht.blog.dao.UserMapper;
import com.wht.blog.entity.User;
import com.wht.blog.exception.TipException;
import com.wht.blog.util.Method;
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


    public List getAllUser () {
        return userMapper.getAll();
    }
    public User getOneById (int id) {
        return userMapper.selectByPrimaryKey(id);
    }
    public void addUser (User user) {
        userMapper.insert(user);
    }

    public void updateByPrimaryKeySelective (User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void del (Map ids) {
        userMapper.deleteByPrimaryKeyBatch(ids);
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
}
