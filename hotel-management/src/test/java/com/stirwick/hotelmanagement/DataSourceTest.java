package com.stirwick.hotelmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import com.stirwick.hotelmanagement.result.Result;
@SpringBootTest
public class DataSourceTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testDataSource() throws Exception{
        Result<String> result = Result.success("测试统一返回类");
        System.out.println(result);
        //System.out.println("=====数据源对象：" + dataSource);
        //System.out.println("=====获取数据库连接测试："+dataSource.getConnection());
    }
}
