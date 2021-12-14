package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {
    Integer countPermissionByRoleId(Integer roleId);
    Integer deletePermissionsByRoleId(Integer roleId);
    List<Integer> queryRoleHasAllModuleIdsByRoleId(Integer roleId);
    List<String> queryUserHasRolesHasPermissions(Integer userId);
    Integer countPermissionsByModuleId(Integer mid);
    Integer deletePermissionsByModuleId(Integer mid);
}