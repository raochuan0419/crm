package com.yjx.crm.controller;

import com.yjx.crm.base.BaseController;
import com.yjx.crm.base.ResultInfo;
import com.yjx.crm.bean.Module;
import com.yjx.crm.dto.TreeDto;
import com.yjx.crm.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;

    /**
     * 查询授权
     * @return
     */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeDto> queryAllModules(Integer roleId){
        System.out.println(roleId);
        return moduleService.queryAllModules02(roleId);
    }

    /**
     * 权限列表
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> moduleList(){
        return moduleService.moduleList();
    }

    /**
     * 跳转权限列表主页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "module/module";
    }

    /**
     * 添加菜单
     * @param module
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveModule(Module module){
        //System.out.println(module);
        moduleService.saveModule(module);
        return success("菜单添加成功!");
    }
    // 添加资源页视图转发
    @RequestMapping("addModulePage")
    public String addModulePage(Integer grade,Integer parentId,Model model){
        model.addAttribute("grade",grade);
        model.addAttribute("parentId",parentId);
        return "module/add";
    }
    // 更新资源页视图转发
    @RequestMapping("updateModulePage")
    public String updateModulePage(Integer id, Model model) {
        model.addAttribute("module", moduleService.selectByPrimaryKey(id));
        return "module/update";
    }

    /**
     * 更新菜单
     * @param module
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("菜单更新成功!");
    }

    /**
     * 菜单列表
     * @param grade
     * @return
     */
    @RequestMapping("queryAllModulesByGrade")
    @ResponseBody
    public List<Map<String,Object>> queryAllModulesByGrade(Integer grade){
        return moduleService.queryAllModulesByGrade(grade);
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModuleById(id);
        return success("菜单删除成功!");
    }
}
