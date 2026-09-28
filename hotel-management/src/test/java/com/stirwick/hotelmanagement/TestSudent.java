package com.stirwick.hotelmanagement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.stirwick.hotelmanagement.mapper.StudentInfoMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.stirwick.hotelmanagement.service.ClassInfoService;
@SpringBootTest

public class TestSudent {
    @Autowired
    private StudentInfoMapper studentInfoMapper;

    @Autowired
    private ClassInfoService classInfoService;

    @Test
    public void testCountStudentByClassIdList() {

    }

}
