package com.yjx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjx.crm.base.BaseService;
import com.yjx.crm.bean.SaleChance;
import com.yjx.crm.query.SaleChanceQuery;
import com.yjx.crm.utils.AssertUtil;
import com.yjx.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    /*条件查询列表*/
    /*
    * code
    * msg
    * count:
    * data;
    * */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        //实例
        Map<String,Object> map=new HashMap<>();
        //实例化分页单位
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //开始分页
        PageInfo<SaleChance> plist=new PageInfo<>(selectByParams(saleChanceQuery));
        //准备数据
        map.put("code",0);
        map.put("msg","success");
        map.put("count",plist.getTotal());
        map.put("data",plist.getList());
        //返回map
        return map;
    }


    /**
     * 营销机会数据添加
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSaleChance(SaleChance saleChance) {
        // 1.参数校验
        checkParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        // 2.设置相关参数默认值
        // 未选择分配人
        saleChance.setState(0);
        saleChance.setDevResult(0);
        // 选择分配人
        if (StringUtils.isNotBlank(saleChance.getAssignMan())) {
            saleChance.setState(0);
            saleChance.setDevResult(0);
            saleChance.setAssignTime(new Date());
        }
        saleChance.setIsValid(1);
        saleChance.setUpdateDate(new Date());
        saleChance.setCreateDate(new Date());
        // 3.执行添加 判断结果
        AssertUtil.isTrue(insertSelective(saleChance) < 1, "营销机会数据添加失败！");
    }

    /**
     * 基本参数校验
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkParams(String customerName, String linkMan, String linkPhone)
    {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "请输入客户名！");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "请输入联系人！");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "请输入手机号！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机号格式不正确！");
    }


    /**
     * 验证
     *         //客户名非空
     *         //联系人非空
     *         //联系电话 非空，11位的合法的手机号
     * @param customerName 客户名称
     * @param linkMan 联系人
     * @param linkPhone 手机
     */
    private void checkSaleChanceParam(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名称");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入联系人电话");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"请输入合法的手机号");
    }


    /**
     * 一:验证：
     *  1:当前对象的ID
     *  2.客户名非空
     *  3.联系人非空
     *  4.电话非空，11位合法
     * 二：
     *  设定默认值
     * 三
     *  修改是否成功
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        //验证
        SaleChance temp = selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp==null,"待修改的记录不存在");
        checkSaleChanceParam(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //原来  未分配 state,devResult
        if(StringUtils.isBlank(temp.getAssignMan())&& StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //已经分配
        if(StringUtils.isNotBlank(temp.getAssignMan())&& StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
            saleChance.setAssignTime(null);
            saleChance.setAssignMan("");
        }
        //设定默认值
        saleChance.setUpdateDate(new Date());
        //修改是否成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"修改失败了");
    }


    /**
     * 批量删除
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeSaleChanceIds(Integer [] ids){
        //验证
        AssertUtil.isTrue(ids==null || ids.length==0,"请选择要删除的数据");
        //删除是否成功
        AssertUtil.isTrue(deleteBatch(ids)!=ids.length,"批量删除失败了");
    }

    /**
     * 记录更新
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id,Integer devResult){
        //校验
        AssertUtil.isTrue(id==null,"待更新记录不存在!");
        SaleChance saleChance = selectByPrimaryKey(id);
        AssertUtil.isTrue(saleChance==null,"待更新记录不存在!");
        //设置devResult属性
        saleChance.setDevResult(devResult);
        //执行更新
        AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"机会数据更新失败!");
    }

}
