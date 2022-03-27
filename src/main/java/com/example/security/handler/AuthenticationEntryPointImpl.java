package com.example.security.handler;

import com.alibaba.fastjson.JSON;
import com.example.security.domain.ResponseResult;
import com.example.security.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        WebUtils.renderString(response, JSON.toJSONString(new ResponseResult<>(HttpStatus.UNAUTHORIZED.value(), "用户认证失败", null)));
    }
}
