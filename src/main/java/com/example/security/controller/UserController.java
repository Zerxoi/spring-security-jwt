package com.example.security.controller;

import com.example.security.domain.LoginUser;
import com.example.security.domain.ResponseResult;
import com.example.security.service.UserService;
import com.example.security.vo.UserVo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseResult<Map<String, String>> login(@RequestBody UserVo user) {
        String jwt = userService.login(user);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return new ResponseResult<>(200, "登陆成功", map);
    }

    @GetMapping("/logout")
    public ResponseResult<Map<String, String>> logout() {
        userService.logout();
        return new ResponseResult<>(200, "登出成功", null);
    }

    @GetMapping("/hello")
    public String hello() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "hello, " + loginUser.getUsername();
    }
}
