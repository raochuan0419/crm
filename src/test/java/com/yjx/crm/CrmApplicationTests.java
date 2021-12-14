package com.yjx.crm;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CrmApplicationTests {

    @Autowired
    DruidDataSource dataSource;
    @Test
    void contextLoads() {
        System.out.println(dataSource.getClass());
    }

}
