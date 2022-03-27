package com.example.security.service;

import com.example.security.entity.UserRole;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface UserRoleService extends MPJBaseService<UserRole> {
    List<String> selectPermsByUserId(Long userId);
}
