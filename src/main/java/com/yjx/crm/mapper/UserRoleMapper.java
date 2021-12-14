package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    Integer countUserRoleByUserId(Integer userId);
    Integer deleteUserRoleByUserId(Integer userId);

}