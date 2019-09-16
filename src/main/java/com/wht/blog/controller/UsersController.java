package com.wht.blog.controller;

import com.wht.blog.entity.User;
import com.wht.blog.service.UsersService;
import com.wht.blog.util.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
/**
 * @author wht
 * @since 2019-09-13 12:51
 */
@RestController
public class UsersController {
    @Resource
    private UsersService usersService;

    @GetMapping("/user")
    public RestResponse getAllUser() {
        return RestResponse.ok(usersService.getAllUser());
    }

    @GetMapping("/getOneById")
    public RestResponse getOneById(@RequestParam(value = "id") Integer id) {
        return RestResponse.ok(usersService.getOneById(id));
    }

    @PostMapping("/addUser")
    public RestResponse addUser(
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
        usersService.addUser(user);
        return RestResponse.ok("添加成功");
    }

    @PostMapping("/updateUser")
    public RestResponse updateUser(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "screenName", required = false) String screen_name
    ) {
        User user = new User();
        user.setId(id);
        if (StringUtils.isNotBlank(username)) user.setUsername(StringUtils.trim(username));
        if (StringUtils.isNotBlank(email)) user.setEmail(email);
        if (StringUtils.isNotBlank(screen_name)) user.setScreenName(screen_name);
        user.setLogged(new Date());

        usersService.updateByPrimaryKeySelective(user);
        return RestResponse.ok("更新成功");
    }



    @DeleteMapping("/delUser")
    public RestResponse delUser(@RequestParam(value = "id") int id) {
        usersService.delUser(id);
        return RestResponse.ok("删除成功");
    }

}
