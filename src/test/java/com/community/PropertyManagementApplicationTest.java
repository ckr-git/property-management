package com.community;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 应用启动测试
 */
@SpringBootTest
public class PropertyManagementApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("测试应用上下文加载")
    public void contextLoads() {
        assertNotNull(applicationContext, "应用上下文应该成功加载");
    }

    @Test
    @DisplayName("测试关键Bean是否存在")
    public void testBeansExist() {
        // 测试Service Bean
        assertTrue(applicationContext.containsBean("userServiceImpl"), "UserService应该存在");
        assertTrue(applicationContext.containsBean("noticeServiceImpl"), "NoticeService应该存在");
        assertTrue(applicationContext.containsBean("houseServiceImpl"), "HouseService应该存在");
        assertTrue(applicationContext.containsBean("repairRequestServiceImpl"), "RepairRequestService应该存在");
        
        // 测试Controller Bean
        assertTrue(applicationContext.containsBean("userController"), "UserController应该存在");
        assertTrue(applicationContext.containsBean("noticeController"), "NoticeController应该存在");
        assertTrue(applicationContext.containsBean("houseController"), "HouseController应该存在");
        assertTrue(applicationContext.containsBean("repairRequestController"), "RepairRequestController应该存在");
        
        // 测试公共组件Bean
        assertTrue(applicationContext.containsBean("jwtUtil"), "JwtUtil应该存在");
    }

    @Test
    @DisplayName("测试数据源配置")
    public void testDataSourceConfiguration() {
        assertTrue(applicationContext.containsBean("dataSource"), "DataSource应该存在");
    }

    @Test
    @DisplayName("测试Security配置")
    public void testSecurityConfiguration() {
        assertTrue(applicationContext.containsBean("securityConfig"), "SecurityConfig应该存在");
        assertTrue(applicationContext.containsBean("passwordEncoder"), "PasswordEncoder应该存在");
    }
}
