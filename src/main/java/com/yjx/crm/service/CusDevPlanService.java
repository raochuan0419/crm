package com.yjx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjx.crm.base.BaseService;
import com.yjx.crm.bean.CusDevPlan;
import com.yjx.crm.mapper.CusDevPlanMapper;
import com.yjx.crm.query.CusDevPlanQuery;
import com.yjx.crm.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {
    @Autowired
    private CusDevPlanMapper cusDevPlanMapper;

    /**
     * 多条件分页查询
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlansByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCusDevPlan(CusDevPlan cusDevPlan){
        //数据校验
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        //设置参数
        cusDevPlan.setIsValid(1);
        cusDevPlan.setUpdateDate(new Date());
        cusDevPlan.setCreateDate(new Date());
        //执行添加是否成功
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)<1,"计划项记录添加失败!!");
    }

    private void checkParams(Integer saleChanceId, String planItem, Date planDate) {
        //客户机会Id不能为空
        AssertUtil.isTrue(saleChanceId==null||cusDevPlanMapper.selectByPrimaryKey(saleChanceId)==null,"请设置营销机会ID！");
        //开发计划能让不能为空
        AssertUtil.isTrue(planItem.isBlank(),"请输入计划项内容！");
        //开发时间不能为空
        AssertUtil.isTrue(planDate==null,"请指定计划项目日期");
    }

    /**
     * 更新计划项
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        //更新记录不能为空
        AssertUtil.isTrue(cusDevPlan.getId()==null||cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId())==null,"更新的记录不存在!");
        //校验数据
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        //设置更新时间
        cusDevPlan.setUpdateDate(new Date());
        //执行更新
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)<1,"计划项记录更新失败!!");
    }

    /**
     * 删除计划项
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void  delCusDevPlan(Integer id){
        //获取计划项
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        //校验计划项
        AssertUtil.isTrue(id==null||cusDevPlan==null,"待删记录不存在!");
        //设置计划项
        cusDevPlan.setIsValid(0);
        //执行删除
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)<1,"计划项记录删除失败!!");
    }
}
