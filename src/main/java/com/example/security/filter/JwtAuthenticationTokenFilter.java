package com.example.security.filter;

import com.example.security.domain.LoginUser;
import com.example.security.utils.JwtUtils;
import com.example.security.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;

    public JwtAuthenticationTokenFilter(JwtUtils jwtUtils, RedisUtils redisUtils) {
        this.jwtUtils = jwtUtils;
        this.redisUtils = redisUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Token");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // 用户认证
            Claims claims = jwtUtils.parseJWT(token);
            String userId = claims.getSubject();
            String key = "login:" + userId;
            LoginUser loginUser = redisUtils.getCacheObject(key);
            if (loginUser == null) {
                throw new RuntimeException("用户未登录");
            }
            // 使用三个参数的构造函数表示认证通过，否则会在FilterSecurityInterceptor中被抛出认证异常
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities()));
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("非法Token");
        }
    }
}
