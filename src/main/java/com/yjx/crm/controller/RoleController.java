package com.yjx.crm.controller;

import com.yjx.crm.base.BaseController;
import com.yjx.crm.base.ResultInfo;
import com.yjx.crm.bean.Role;
import com.yjx.crm.query.RoleQuery;
import com.yjx.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String, Object>> queryAllRoles(Integer userId){
        List<Map<String, Object>> maps = roleService.queryAllRoles(userId);
        System.out.println(maps);
        return maps;
    }

    /**
     * 多功能分页查询
     * @param roleQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryByParamsForTable(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }

    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    /**
     * 跳转到添加和修改页面
     * @param roleId
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateRolePage")
    public String addUserPage(Integer roleId, Model model){
        if (roleId!=null) {
            model.addAttribute("role",roleService.selectByPrimaryKey(roleId));
        }
        return "role/add_update";
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.saveRole(role);
        return success("角色添加成功!");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        //System.out.println("11111111111111111");
        roleService.updateRole(role);
        return success("角色修改成功!");
    }

    /**
     * 删除角色
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer[] ids){
        //System.out.println(ids);
        roleService.deleteRoleIds(ids);
        return success("角色删除成功!");
    }

    /**
     * 跳转授权页面
     * @param roleId
     * @param model
     * @return
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mids,Integer roleId) {
        roleService.addGrant(mids, roleId);
        return success("权限设置成功!");
    }
}
