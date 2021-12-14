package com.yjx.crm.controller;

import com.yjx.crm.base.BaseController;
import com.yjx.crm.base.ResultInfo;
import com.yjx.crm.bean.CusDevPlan;
import com.yjx.crm.bean.SaleChance;
import com.yjx.crm.query.CusDevPlanQuery;
import com.yjx.crm.service.CusDevPlanService;
import com.yjx.crm.service.SaleChanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {
    @Autowired
    private SaleChanceService saleChanceService;
    @Autowired
    private CusDevPlanService cusDevPlanService;

    @RequestMapping("index")
    public String index() {
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     * 开发计划页面
     *
     * @param model
     * @param sid
     * @return
     */
    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Model model, Integer sid) {
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);
        model.addAttribute("saleChance", saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery query) {
        return cusDevPlanService.queryCusDevPlansByParams(query);
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.saveCusDevPlan(cusDevPlan);
        return success("计划项目记录添加成功!");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan) {
        System.out.println(cusDevPlan);
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("计划项目记录更新成功!");
    }

    @RequestMapping("addOrUpdateCusDevPlanPage")
    public String addOrUpdateCusDevPlanPage(Integer sid, Integer id, Model model) {
        model.addAttribute("cusDevPlan", cusDevPlanService.selectByPrimaryKey(id));
        model.addAttribute("sid", sid);
        return "cusDevPlan/add_update";
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id) {
        cusDevPlanService.delCusDevPlan(id);
        return success("计划项目记录更新成功!");
    }
}