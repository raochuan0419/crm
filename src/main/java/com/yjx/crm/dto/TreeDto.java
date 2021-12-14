package com.yjx.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeDto {
    private Integer id;
    private String name;
    private Integer pId;
    private Boolean checked=false;
}
