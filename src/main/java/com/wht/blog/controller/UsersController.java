package com.wht.blog.controller;

import com.wht.blog.entity.User;
import com.wht.blog.service.UsersService;
import com.wht.blog.util.Const;
import com.wht.blog.util.Method;
import com.wht.blog.util.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wht
 * @since 2019-09-13 12:51
 */
@RestController
@RequestMapping("/api/manage/user")
public class UsersController extends BaseController {
    @Resource
    private UsersService usersService;

    @PostMapping("login")
    public RestResponse login(@RequestParam String username, @RequestParam String password) {
        User user = usersService.login(username, password);
        request.getSession().setAttribute(Const.USER_SESSION_KEY, user);

        return RestResponse.ok(user,"登录成功" );
    }
    @PostMapping("logout")
    public RestResponse logout() {
        User user = this.user();
        if (null == user) {
            return RestResponse.fail("没有用户登陆");
        }

        request.getSession().removeAttribute(Const.USER_SESSION_KEY);
        return RestResponse.ok("退出成功");
    }
    @GetMapping("/list")
    public RestResponse getAllUser() {
        return RestResponse.ok(usersService.getAllUser());
    }

    @GetMapping("/getOneById")
    public RestResponse getOneById() {
        int id = this.user().getId();
        return RestResponse.ok(usersService.getOneById(id));
    }

    @PostMapping("/register")
    public RestResponse addUser(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "screenName", required = false) String screen_name
    ) {
        User user = Method.addUser(username, password, email, screen_name);
        usersService.addUser(user);
        this.login(username, password);
        return RestResponse.ok("注册成功并登录");
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
    public RestResponse delUser(@RequestParam(value = "ids") String ids) {
        Map<String, String> map = new HashMap<>();
        map.put("ids", ids);
        usersService.del(map);
        return RestResponse.ok("删除成功");
    }

}
