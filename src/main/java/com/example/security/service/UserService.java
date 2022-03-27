package com.example.security.service;

import com.example.security.entity.User;
import com.example.security.vo.UserVo;
import com.github.yulichang.base.MPJBaseService;

public interface UserService extends MPJBaseService<User> {

    String login(UserVo user);

    void logout();
}
