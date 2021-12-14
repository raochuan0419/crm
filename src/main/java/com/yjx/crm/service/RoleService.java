package com.yjx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjx.crm.base.BaseService;
import com.yjx.crm.bean.Permission;
import com.yjx.crm.bean.Role;
import com.yjx.crm.mapper.ModuleMapper;
import com.yjx.crm.mapper.PermissionMapper;
import com.yjx.crm.mapper.RoleMapper;
import com.yjx.crm.query.RoleQuery;
import com.yjx.crm.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private ModuleMapper moduleMapper;

    /**
     * 查询所有角色列表
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRole(Role role){
        //校验数据
        AssertUtil.isTrue(role.getRoleName().isBlank(),"请输入角色名称!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp!=null,"角色名称已存在!");
        //设置属性
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        //执行添加
        AssertUtil.isTrue(roleMapper.insertSelective(role)<1,"角色添加失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        System.out.println(role.getId());
        //校验数据
        AssertUtil.isTrue(role.getId()==null||roleMapper.selectByPrimaryKey(role.getId())==null,"被修改的角色记录不存在!");
        AssertUtil.isTrue(role.getRoleName().isBlank(),"请输入角色名称!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp!=null&&!(temp.getId().equals(role.getId())),"被修改的角色已存在!");
        //设置修改数据
        role.setUpdateDate(new Date());
        //执行修改
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"角色修改失败!");
    }

    /**
     * 角色删除
     * @param roleIds
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRoleIds(Integer[] roleIds){
        //校验数据
        AssertUtil.isTrue(roleIds==null,"被删除的数据不存在!");
        //指向删除
        AssertUtil.isTrue(roleMapper.deleteBatch(roleIds)<1,"角色删除失败!");
    }

    /**
     * 添加权限
     * @param mids
     * @param roleId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer[] mids,Integer roleId) {
        //校验数据
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(roleId==null||role==null,"待授权角色不存在!");
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        if (count>0){
            AssertUtil.isTrue(permissionMapper.deletePermissionsByRoleId(roleId)<count,"权限分配失败!");
        }
        if (mids!=null&&mids.length!=0) {
            List<Permission> permissions = new ArrayList<>();
            for (Integer mid : mids) {
                Permission permission = new Permission();
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(mid);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permissions.add(permission);
            }
            //执行添加
            permissionMapper.insertBatch(permissions);
        }
    }
}
