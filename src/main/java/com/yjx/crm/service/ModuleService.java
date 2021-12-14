package com.yjx.crm.service;

import com.yjx.crm.base.BaseService;
import com.yjx.crm.bean.Module;
import com.yjx.crm.dto.TreeDto;
import com.yjx.crm.mapper.ModuleMapper;
import com.yjx.crm.mapper.PermissionMapper;
import com.yjx.crm.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 授权页面
     * @return
     */
    public List<TreeDto> queryAllModules(){
        return moduleMapper.queryAllModules();
    }

    public List<TreeDto> queryAllModules02(Integer roleId){
        List<TreeDto> treeDtos = moduleMapper.queryAllModules();
        //查询roleId的所有权限Id的
        List<Integer> integerList = permissionMapper.queryRoleHasAllModuleIdsByRoleId(roleId);
        if (integerList!=null||integerList.size()>0) {
            treeDtos.forEach(treeDto -> {
                if (integerList.contains(treeDto.getId())) {
                    //说明该权限分配了菜单
                    treeDto.setChecked(true);
                }
            });
        }
        return treeDtos;
    }

    /**
     * 权限列表
     * @return
     */
    public Map<String,Object> moduleList(){
        Map<String,Object> map = new HashMap<>();
        List<Module> modules = moduleMapper.queryModules();
        map.put("code",0);
        map.put("msg","");
        map.put("count",modules.size());
        map.put("data",modules);
        return map;
    }

    /**
     * 添加菜单
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveModule(Module module){
        //数据校验
        AssertUtil.isTrue(module.getModuleName().isBlank(),"请输入菜单名!");
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade==null||!(grade==0||grade==1||grade==2),"菜单层级不合法");
        AssertUtil.isTrue(moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(),module.getModuleName())!=null,"该层级二级菜单名重复!");
        if (grade==1) {
            AssertUtil.isTrue(module.getUrl().isBlank(),"请输入二级菜单地址!");
            AssertUtil.isTrue(moduleMapper.queryModuleByGradeAndUrl(module.getGrade(),module.getUrl())!=null,"该二级菜单地址重复!");
        }
        if (grade==0) {
            Integer parentId = module.getParentId();
            //System.out.println(parentId);
            AssertUtil.isTrue(parentId==null,"请指定上级菜单!");
        }
        AssertUtil.isTrue(module.getParentId()==null,"请输入权限码!");
        AssertUtil.isTrue(moduleMapper.queryModuleByOptValue(module.getOptValue())!=null,"权限码重复!");
        //设置状态,创建时间,更新时间
        module.setIsValid((byte)1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        //执行添加
        AssertUtil.isTrue(insertSelective(module)<1,"菜单创建失败!");
    }

    /**
     * 更新菜单
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module){
        //数据校验
        AssertUtil.isTrue(module.getId()==null||selectByPrimaryKey(module.getId())==null,"待更新菜单不存在!");
        AssertUtil.isTrue(module.getModuleName().isBlank(),"请输入菜单名!");
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade==null||!(grade==0||grade==1||grade==2),"菜单层级不合法!");
        Module temp = moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName());
        if (temp!=null) {
            AssertUtil.isTrue(!(module.getId().equals(temp.getId())),"该层级下级菜单已存在!");
        }
        if (grade==1) {
            AssertUtil.isTrue(module.getUrl()==null,"请输入二级菜单地址");
            temp = moduleMapper.queryModuleByGradeAndUrl(module.getGrade(), module.getUrl());
            if (temp!=null) {
                AssertUtil.isTrue(!(module.getId().equals(temp.getId())),"二级菜单地址重复!");
            }
        }
        if (grade==0) {
            Integer parentId = module.getParentId();
            AssertUtil.isTrue(parentId==null||selectByPrimaryKey(parentId)==null,"请指定上级菜单!");
        }
        AssertUtil.isTrue(module.getOptValue()==null,"请输入权限码!");
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        if (temp!=null) {
            AssertUtil.isTrue(!(module.getId().equals(temp.getId())),"二权限码重复!!");
        }
        //设置更新时间
        module.setUpdateDate(new Date());
        //执行更新
        AssertUtil.isTrue(updateByPrimaryKeySelective(module)<1,"菜单更新失败!");
    }

    /**
     * 菜单列表
     * @param grade
     * @return
     */
    public List<Map<String,Object>> queryAllModulesByGrade(Integer grade){
        return moduleMapper.queryAllModulesByGrade(grade);
    }

    /**
     * 删除菜单
     * @param mid
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModuleById(Integer mid){
        //System.out.println(mid);
        Module module = selectByPrimaryKey(mid);
        AssertUtil.isTrue(mid==null||module==null,"待删除菜单不存在!");
        //如果存在子菜单不能删除
        Integer count = moduleMapper.countSubModuleByParentId(mid);
        AssertUtil.isTrue(count>0,"存在子菜单,不能删除!");
        //权限判断
        count = permissionMapper.countPermissionsByModuleId(mid);
        if (count>0) {
            AssertUtil.isTrue(permissionMapper.deletePermissionsByModuleId(mid)<count,"菜单删除失败!");
        }
        module.setIsValid((byte)0);
        //执行删除
        AssertUtil.isTrue(updateByPrimaryKeySelective(module)<1,"菜单删除失败!");
    }
}
