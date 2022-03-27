package com.example.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.security.domain.LoginUser;
import com.example.security.entity.User;
import com.example.security.service.UserRoleService;
import com.example.security.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final UserRoleService userRoleService;

    public UserDetailServiceImpl(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户名未找到");
        }
        List<String> perms = userRoleService.selectPermsByUserId(user.getId());
        return new LoginUser(user, perms);
    }
}
