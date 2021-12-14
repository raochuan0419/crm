package com.yjx.crm.service;

import com.yjx.crm.base.BaseService;
import com.yjx.crm.bean.Permission;
import com.yjx.crm.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Autowired
    private PermissionMapper permissionMapper;

    public List<String> queryUserHasRolesHasPermissions(Integer userId){
        return permissionMapper.queryUserHasRolesHasPermissions(userId);
    }
}
