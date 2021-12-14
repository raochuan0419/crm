package com.yjx.crm.mapper;

import com.yjx.crm.base.BaseMapper;
import com.yjx.crm.bean.User;
import com.yjx.crm.query.UserQuery;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
    User queryUserByUserName(String username);
    List<Map<String,Object>> queryAllSales();
}