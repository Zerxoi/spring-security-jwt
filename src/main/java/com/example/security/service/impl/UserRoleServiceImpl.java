package com.example.security.service.impl;

import com.example.security.entity.Menu;
import com.example.security.entity.Role;
import com.example.security.entity.RoleMenu;
import com.example.security.entity.UserRole;
import com.example.security.mapper.UserRoleMapper;
import com.example.security.service.UserRoleService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl extends MPJBaseServiceImpl<UserRoleMapper, UserRole>
        implements UserRoleService {

    @Override
    public List<String> selectPermsByUserId(Long userId) {
        // 不用过滤（Spring Security 内部会将List转为Set）
        return new ArrayList<>(selectJoinList(String.class, new MPJLambdaWrapper<UserRole>()
                .select(Menu::getPerms)
                .leftJoin(Role.class, Role::getId, UserRole::getRoleId)
                .leftJoin(RoleMenu.class, RoleMenu::getRoleId, UserRole::getRoleId)
                .leftJoin(Menu.class, Menu::getId, RoleMenu::getMenuId)
                .eq(UserRole::getUserId, 1)
                .eq(Role::getStatus, 0)
                .eq(Menu::getStatus, 0)
        ));
    }
}




