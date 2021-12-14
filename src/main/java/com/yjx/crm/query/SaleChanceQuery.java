package com.yjx.crm.query;

import com.yjx.crm.base.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleChanceQuery extends BaseQuery {
    private String customerName; // 客户名称
    private String createMan; // 创建人
    private String state; //状态
    private Integer devResult; // 开发状态
    private Integer assignMan;// 分配人
}
