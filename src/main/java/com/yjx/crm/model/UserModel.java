package com.yjx.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录查询返回的用户的信息的实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private String userIdStr;//用户Id加密
    private String userName;//用户名
    private String trueName;//真实姓名
}
