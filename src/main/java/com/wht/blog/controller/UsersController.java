package com.wht.blog.controller;

import com.wht.blog.entity.User;
import com.wht.blog.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
/**
 * @author wht
 * @since 2019-09-13 12:51
 */
@Controller
public class UsersController {
    @Resource
    private UsersService usersService;

    @GetMapping("/user")
    @ResponseBody
    public List getAllUser() {
        return usersService.getAllUser();
    }

    @GetMapping("/getOneById")
    @ResponseBody
    public User getOneById(@RequestParam(value = "id") Integer id) {
        return usersService.getOneById(id);
    }

    @PostMapping("/addUser")
    @ResponseBody
    public int addUser(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password_md5,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "screenName", required = false) String screen_name
    ) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setScreenName(screen_name);
        user.setPasswordMd5(password_md5);
        user.setLogged(new Date());

        user.setCreated(new Date());
        return usersService.addUser(user);
    }

    @PostMapping("/updateUser")
    @ResponseBody
    public int updateUser(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "screenName", required = false) String screen_name
    ) {
        User user = new User();
        user.setId(id);
        if (!StringUtils.isEmpty(username)) user.setUsername(username);
        if (!StringUtils.isEmpty(email)) user.setEmail(email);
        if (!StringUtils.isEmpty(screen_name)) user.setScreenName(screen_name);
        user.setLogged(new Date());

        return usersService.updateByPrimaryKeySelective(user);
    }



    @DeleteMapping("/delUser")
    @ResponseBody
    public int delUser(@RequestParam(value = "id") int id) {
        return usersService.delUser(id);
    }

}
