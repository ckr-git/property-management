package com.community;

import com.community.entity.User;
import com.community.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 安全测试类
 * 测试SQL注入、XSS、密码安全等
 */
@SpringBootTest
@Transactional
public class SecurityTest {

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("测试SQL注入防护 - 登录用户名")
    public void testSqlInjectionInUsername() {
        // 尝试SQL注入攻击
        String[] sqlInjections = {
            "admin' OR '1'='1",
            "admin'; DROP TABLE sys_user; --",
            "' OR 1=1 --",
            "admin'/*",
            "1' OR '1'='1'/*"
        };
        
        for (String injection : sqlInjections) {
            User user = userService.login(injection, "123456");
            assertNull(user, "SQL注入攻击应该失败: " + injection);
        }
    }

    @Test
    @DisplayName("测试SQL注入防护 - 查询用户名")
    public void testSqlInjectionInGetUsername() {
        String[] sqlInjections = {
            "admin' OR '1'='1",
            "' UNION SELECT * FROM sys_user --",
            "admin'; DELETE FROM sys_user; --"
        };
        
        for (String injection : sqlInjections) {
            User user = userService.getUserByUsername(injection);
            assertNull(user, "SQL注入查询应该返回null: " + injection);
        }
    }

    @Test
    @DisplayName("测试XSS攻击防护 - 注册")
    public void testXssInRegistration() {
        User xssUser = new User();
        xssUser.setUsername("xss_test_" + System.currentTimeMillis());
        xssUser.setPassword("test123456");
        xssUser.setRealName("<script>alert('XSS')</script>");
        xssUser.setPhone("13800000000");
        xssUser.setUserType(1);
        
        boolean result = userService.register(xssUser);
        assertTrue(result, "包含XSS内容的注册应该成功（存储但不执行）");
        
        // 验证数据被原样存储（实际应用中需要输出时转义）
        User savedUser = userService.getUserByUsername(xssUser.getUsername());
        assertNotNull(savedUser, "应该能找到注册的用户");
    }

    @Test
    @DisplayName("测试密码不以明文存储")
    public void testPasswordNotStoredInPlainText() {
        String rawPassword = "test123456";
        
        User newUser = new User();
        newUser.setUsername("pwd_test_" + System.currentTimeMillis());
        newUser.setPassword(rawPassword);
        newUser.setRealName("密码测试用户");
        newUser.setPhone("13800000000");
        newUser.setUserType(1);
        
        userService.register(newUser);
        
        User savedUser = userService.getUserByUsername(newUser.getUsername());
        assertNotNull(savedUser);
        assertNotEquals(rawPassword, savedUser.getPassword(), "密码不应以明文存储");
        assertTrue(passwordEncoder.matches(rawPassword, savedUser.getPassword()), 
                   "原始密码应该能匹配加密后的密码");
    }

    @Test
    @DisplayName("测试BCrypt加密强度")
    public void testBcryptStrength() {
        String password = "test123456";
        
        // 生成多个hash，应该都不同
        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);
        String hash3 = passwordEncoder.encode(password);
        
        assertNotEquals(hash1, hash2, "同一密码每次加密结果应不同（因为盐不同）");
        assertNotEquals(hash2, hash3, "同一密码每次加密结果应不同（因为盐不同）");
        
        // 但都能验证成功
        assertTrue(passwordEncoder.matches(password, hash1));
        assertTrue(passwordEncoder.matches(password, hash2));
        assertTrue(passwordEncoder.matches(password, hash3));
    }

    @Test
    @DisplayName("测试空密码注册")
    public void testEmptyPasswordRegistration() {
        User user = new User();
        user.setUsername("empty_pwd_" + System.currentTimeMillis());
        user.setPassword("");
        user.setRealName("空密码用户");
        user.setPhone("13800000000");
        user.setUserType(1);
        
        // 空密码注册行为取决于业务逻辑
        // 这里测试系统是否会崩溃
        try {
            boolean result = userService.register(user);
            // 不管结果如何，系统不应该崩溃
        } catch (Exception e) {
            fail("空密码注册不应导致系统异常: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试超长输入处理")
    public void testVeryLongInput() {
        // 创建超长用户名
        StringBuilder longUsername = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longUsername.append("a");
        }
        
        try {
            User user = userService.login(longUsername.toString(), "123456");
            // 应该返回null或抛出受控异常，不应崩溃
            assertNull(user, "超长用户名登录应该失败");
        } catch (Exception e) {
            // 受控异常是可接受的
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("测试特殊字符输入")
    public void testSpecialCharacterInput() {
        String[] specialInputs = {
            "admin\0admin",      // null字符
            "admin\nadmin",      // 换行符
            "admin\radmin",      // 回车符
            "admin\tadmin",      // 制表符
            "admin\\admin",      // 反斜杠
            "admin%admin"        // 百分号
        };
        
        for (String input : specialInputs) {
            try {
                User user = userService.login(input, "123456");
                // 应该返回null，不应崩溃
                assertNull(user, "特殊字符输入应该安全处理: " + input);
            } catch (Exception e) {
                // 受控异常是可接受的
                assertTrue(true);
            }
        }
    }

    @Test
    @DisplayName("测试Unicode字符输入")
    public void testUnicodeInput() {
        User user = new User();
        user.setUsername("unicode_" + System.currentTimeMillis());
        user.setPassword("test123456");
        user.setRealName("中文用户名测试🎉");  // 包含emoji
        user.setPhone("13800000000");
        user.setUserType(1);
        
        try {
            boolean result = userService.register(user);
            // 系统应该能处理Unicode字符
        } catch (Exception e) {
            fail("Unicode字符处理不应导致异常: " + e.getMessage());
        }
    }
}
