package com.example.security.expression;


import com.example.security.domain.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("mser")
// 自定义授权检验
public class MySecurityExpressionRoot {
    public boolean hasAuthority(String authority) {
        // 用户成功认证后将认证信息存入ContextHolder中，从ContextHolder中获取用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        return permissions.contains(authority);
    }
}
