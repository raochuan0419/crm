package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.Role;
import com.yjx.crm.query.RoleQuery;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    List<Map<String,Object>> queryAllRoles(Integer userId);
    Role queryRoleByRoleName(String roleName);
}