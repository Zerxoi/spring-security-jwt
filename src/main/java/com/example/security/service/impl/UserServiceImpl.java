package com.example.security.service.impl;

import com.example.security.domain.LoginUser;
import com.example.security.entity.User;
import com.example.security.mapper.UserMapper;
import com.example.security.service.UserService;
import com.example.security.utils.JwtUtils;
import com.example.security.utils.RedisUtils;
import com.example.security.vo.UserVo;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends MPJBaseServiceImpl<UserMapper, User>
        implements UserService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RedisUtils redisUtils;

    public UserServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, RedisUtils redisUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.redisUtils = redisUtils;
    }

    @Override
    public String login(UserVo userVo) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userVo.getUsername(), userVo.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authenticate == null) {
            // TODO 异常处理
            throw new RuntimeException("登陆失败");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        redisUtils.setCacheObject("login:" + userId, loginUser);
        return jwtUtils.createJWT(userId);
    }

    @Override
    public void logout() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) usernamePasswordAuthenticationToken.getPrincipal();
        Long userId = loginUser.getUser().getId();
        redisUtils.deleteObject("login:" + userId);
    }
}
