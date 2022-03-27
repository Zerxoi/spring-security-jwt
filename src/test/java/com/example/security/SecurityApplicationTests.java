package com.example.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.security.entity.Menu;
import com.example.security.entity.Role;
import com.example.security.entity.RoleMenu;
import com.example.security.entity.UserRole;
import com.example.security.mapper.UserMapper;
import com.example.security.service.UserRoleService;
import com.example.security.utils.JwtUtils;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.KeyPair;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class SecurityApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRoleService userRoleService;

    @Test
    void contextLoads() {
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        System.out.println(Encoders.BASE64.encode(keyPair.getPrivate().getEncoded()));
        System.out.println(Encoders.BASE64.encode(keyPair.getPublic().getEncoded()));

        String jws = Jwts.builder()
                .setId("8888") // jti
                .setSubject("rose") // sub
                .setIssuedAt(new Date()) // iat
                .signWith(keyPair.getPrivate()).compact();
        System.out.println(jws);
    }

    @Test
    void mapperTest() {
        System.out.println(userMapper.selectCount(new QueryWrapper<>()));
    }

    @Test
    void passwordEncoderTest() {
        String raw = "123456789";
        String encode = passwordEncoder.encode(raw);
        System.out.println(encode);
        System.out.println(passwordEncoder.matches(raw, encode));
    }

    @Test
    void jwtTest() {
        try {
//            String jwt = jwtUtils.createJWT("123456789", null);
            System.out.println(jwtUtils.parseJWT("eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI4MWUwN2Q5Yy1jYmZjLTQ3NzYtYjg1Mi1hMzYwZWYyNDcwMTciLCJzdWIiOiIxIiwiaWF0IjoxNjQ4MjYwNDg5LCJpc3MiOiJ6ZXJ4b2kiLCJleHAiOjE2NDgyNjQwODl9.EqGbPYbPOTeweTtJRsl6cTw8S0VQlHjgMxh-Ti2npkyj6S9DYiudZeSdyZWPVRjXnb2ET3f0f5lePrfTDLmyf-Qj2-DMgGDL-FvBxpApVCflk8U_FKbIPgJz9SGAHpFfeeVd15Gm3pM1vWIO4Tu1vk2pEv91Toy1K9ArpgEF6efC_hmc60t_kKB272OXv_3Vx1D9r2ANHChEVgES6I3OJg4SJf__DSU045YqD9GlCsmNYd33OQakg52g2XCG3JeL6C8ePO0Xcgv6Cw8y1sEahjOd673ldPB5914ThiD-AIY8xz8_bFq323dYJcszYq_L5Qjz7_oiBIBOXevJ81CLuA"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void permsTest() {
        List<String> collect = userRoleService.selectJoinList(String.class, new MPJLambdaWrapper<UserRole>()
                .select(Menu::getPerms)
                .leftJoin(Role.class, Role::getId, UserRole::getRoleId)
                .leftJoin(RoleMenu.class, RoleMenu::getRoleId, UserRole::getRoleId)
                .leftJoin(Menu.class, Menu::getId, RoleMenu::getMenuId)
                .eq(UserRole::getUserId, 1)
                .eq(Role::getStatus, 0)
                .eq(Menu::getStatus, 0)
        ).stream().distinct().collect(Collectors.toList());
        System.out.println(collect);
    }
}
