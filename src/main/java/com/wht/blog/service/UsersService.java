package com.wht.blog.service;

import com.wht.blog.dao.UserMapper;
import com.wht.blog.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wht
 * @since 2019-09-13 12:54
 */
@Service
public class UsersService {
    private final UserMapper userMapper;

    public UsersService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List getAllUser () {
        return userMapper.getAll();
    }
    public User getOneById (int id) {
        return userMapper.selectByPrimaryKey(id);
    }
    public int addUser (User user) {
        return userMapper.insert(user);
    }

    public int updateByPrimaryKeySelective (User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    public int delUser (Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

}
