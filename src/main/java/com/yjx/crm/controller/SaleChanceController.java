package com.yjx.crm.controller;

import com.yjx.crm.annotation.RequirePermission;
import com.yjx.crm.base.BaseController;
import com.yjx.crm.base.ResultInfo;
import com.yjx.crm.bean.SaleChance;
import com.yjx.crm.query.SaleChanceQuery;
import com.yjx.crm.service.SaleChanceService;
import com.yjx.crm.service.UserService;
import com.yjx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Autowired
    private SaleChanceService saleChanceService;

    @Autowired
    private UserService userService;



    @RequestMapping("index")
    public String index(){
        return  "saleChance/sale_chance";
    }


    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdate(Integer id, Model model){
        //判断
        if(id!=null){
            //查询用户信息
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            //存储
            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }


    @RequestMapping("list")
    @ResponseBody
    //@RequirePermission(code = "101001")
    public Map<String,Object> saylist(HttpServletRequest request,SaleChanceQuery saleChanceQuery,Integer flag){
        if (null != flag && flag == 1) {
            // 获取当前登录用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }
        //调用方法获取数据
        Map<String, Object> map = saleChanceService.querySaleChanceByParams(saleChanceQuery);
        //map--json
        //返回目标map
        return  map;
    }


    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(HttpServletRequest req,SaleChance saleChance){
        //获取登录用户的id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        String trueName = userService.selectByPrimaryKey(userId).getTrueName();
        //创建人
        saleChance.setCreateMan(trueName);
        //添加操作
        saleChanceService.saveSaleChance(saleChance);
        //返回目标对象
        return success("添加成功了");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(SaleChance saleChance){
        //添加操作
        saleChanceService.updateSaleChance(saleChance);
        //返回目标对象
        return success("修改成功了");
    }


    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deletes(Integer[] ids){
        //添加操作
        saleChanceService.removeSaleChanceIds(ids);
        //返回目标对象
        return success("批量删除成功了");
    }

    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("机会数据更新成功!");
    }


}
