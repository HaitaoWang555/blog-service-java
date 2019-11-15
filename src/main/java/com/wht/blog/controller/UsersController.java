package com.wht.blog.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wht.blog.dto.Pagination;
import com.wht.blog.entity.User;
import com.wht.blog.service.UsersService;
import com.wht.blog.service.JwtService;
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
    @Resource
    private JwtService jwtService;

    @PostMapping("login")
    public RestResponse login(@RequestParam String username, @RequestParam String password) {
        User user = usersService.login(username, password);
        switch (user.getStatus()) {
            case 0:
                return RestResponse.fail("您已被禁止登陆" );
            case 1:
                String jwt = jwtService.createJWT(user.getId());
                return RestResponse.ok(jwt,"登录成功" );
            case 2:
                return RestResponse.fail("您不是管理人员" );
            default:
                return RestResponse.fail("登陆失败" );
        }
    }
    @PostMapping("logout")
    public RestResponse logout() {
        Integer user_id = this.getLoginUserId();
        if (null == user_id) {
            return RestResponse.fail("没有用户登陆");
        }

        Boolean clear = jwtService.invalidateJWT(user_id.toString());
        return clear ? RestResponse.ok("退出成功") : RestResponse.fail("退出失败");
    }
    @GetMapping("/list")
    public RestResponse getAllUser() {
        return RestResponse.ok(usersService.getAllUser());
    }
    @GetMapping("/page")
    public RestResponse getAllUser(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = Const.PAGE_SIZE) Integer limit
    ) {
        String sortBy = "created desc";
        Page<User> userList = PageHelper.startPage(page, limit, sortBy).doSelectPage(() ->
                usersService.getAllUser()
        );
        return RestResponse.ok(new Pagination<User>(userList));
    }
    @GetMapping("/getOneById")
    public RestResponse getOneById() {
        Integer id = this.getLoginUserId();
        return RestResponse.ok(usersService.getOneById(id));
    }

    @PostMapping("/register")
    public RestResponse register(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "screenName", required = false) String screen_name
    ) {
        User user = Method.addUser(username, email, screen_name, password);
        usersService.addUser(user);
        this.login(username, password);
        return RestResponse.ok("注册成功并登录");
    }
    @PostMapping("/addUser")
    public RestResponse addUser(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "screenName", required = false) String screen_name
    ) {
        User user = Method.addUser(username, email, screen_name, password);
        usersService.addUser(user);
        return RestResponse.ok("添加成功");
    }
    @PostMapping("/haveUserName")
    public RestResponse haveUserName(@RequestParam(value = "username") String username) {
        Boolean haveUserName = usersService.haveUserName(username);
        if (!haveUserName) {
            return RestResponse.ok("名称已占用");
        } else {
            return RestResponse.ok();
        }
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
