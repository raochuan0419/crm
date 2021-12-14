package com.yjx.crm.query;

import com.yjx.crm.base.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQuery extends BaseQuery {
    private String userName;//用户名
    private String email;//邮箱
    private String phone;//电话
}
