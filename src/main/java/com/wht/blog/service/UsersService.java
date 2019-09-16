package com.wht.blog.service;

import com.wht.blog.dao.UserMapper;
import com.wht.blog.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    public void delUser (Integer id) {
        userMapper.deleteByPrimaryKey(id);
    }

}
