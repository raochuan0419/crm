package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.CusDevPlan;
import com.yjx.crm.query.CusDevPlanQuery;

import java.util.List;

public interface CusDevPlanMapper extends BaseMapper<CusDevPlan,Integer> {
    List<CusDevPlan> selectByParams(CusDevPlanQuery cusDevPlanQuery);
}