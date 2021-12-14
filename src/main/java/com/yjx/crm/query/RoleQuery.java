package com.yjx.crm.query;

import com.yjx.crm.base.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleQuery extends BaseQuery {
    private String roleName;//角色名
}
