package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.Module;
import com.yjx.crm.dto.TreeDto;

import java.util.List;
import java.util.Map;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    List<TreeDto> queryAllModules();
    List<Module> queryModules();
    Module queryModuleByGradeAndModuleName(Integer grade,String moduleName);
    Module queryModuleByGradeAndUrl(Integer grade,String url);
    Module queryModuleByOptValue(String optValue);
    List<Map<String,Object>> queryAllModulesByGrade(Integer grade);
    Integer countSubModuleByParentId(Integer mid);
}