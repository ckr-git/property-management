package com.community.service;

import com.community.entity.User;
import com.community.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务单元测试
 */
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("测试正常登录")
    public void testLoginSuccess() {
        // 先注册一个新用户用于测试登录
        User newUser = new User();
        newUser.setUsername("logintest_" + System.currentTimeMillis());
        newUser.setPassword("test123456");
        newUser.setRealName("Test User");
        newUser.setPhone("13800000000");
        newUser.setUserType(1);
        boolean registered = userService.register(newUser);
        assertTrue(registered, "注册应该成功");
        
        // 使用刚注册的用户登录
        User user = userService.login(newUser.getUsername(), "test123456");
        assertNotNull(user, "登录应该成功");
        assertEquals(newUser.getUsername(), user.getUsername(), "用户名应该匹配");
    }

    @Test
    @DisplayName("测试错误密码登录")
    public void testLoginWithWrongPassword() {
        User user = userService.login("admin", "wrongpassword");
        assertNull(user, "错误密码登录应该返回null");
    }

    @Test
    @DisplayName("测试不存在用户登录")
    public void testLoginWithNonExistentUser() {
        User user = userService.login("nonexistent", "123456");
        assertNull(user, "不存在的用户登录应该返回null");
    }

    @Test
    @DisplayName("测试正常注册")
    public void testRegisterSuccess() {
        User newUser = new User();
        newUser.setUsername("testuser_" + System.currentTimeMillis());
        newUser.setPassword("test123456");
        newUser.setRealName("测试用户");
        newUser.setPhone("13800000000");
        newUser.setUserType(1);

        boolean result = userService.register(newUser);
        assertTrue(result, "注册应该成功");
    }

    @Test
    @DisplayName("测试重复用户名注册")
    public void testRegisterDuplicateUsername() {
        // 先注册一个用户
        String uniqueUsername = "duptest_" + System.currentTimeMillis();
        User firstUser = new User();
        firstUser.setUsername(uniqueUsername);
        firstUser.setPassword("test123456");
        firstUser.setRealName("原始用户");
        firstUser.setPhone("13800000000");
        firstUser.setUserType(1);
        userService.register(firstUser);
        
        // 用相同用户名注册第二个
        User duplicateUser = new User();
        duplicateUser.setUsername(uniqueUsername);  // 已存在的用户名
        duplicateUser.setPassword("test123456");
        duplicateUser.setRealName("重复用户");
        duplicateUser.setPhone("13800000001");
        duplicateUser.setUserType(1);

        boolean result = userService.register(duplicateUser);
        assertFalse(result, "重复用户名注册应该失败");
    }

    @Test
    @DisplayName("测试根据用户名查询用户")
    public void testGetUserByUsername() {
        // 先注册一个新用户
        User newUser = new User();
        newUser.setUsername("querytest_" + System.currentTimeMillis());
        newUser.setPassword("test123456");
        newUser.setRealName("QueryTestUser");
        newUser.setPhone("13800000000");
        newUser.setUserType(1);
        userService.register(newUser);
        
        User user = userService.getUserByUsername(newUser.getUsername());
        assertNotNull(user, "应该能找到用户");
        assertEquals("QueryTestUser", user.getRealName(), "真实姓名应该匹配");
    }

    @Test
    @DisplayName("测试根据ID获取用户")
    public void testGetById() {
        // 先注册一个用户
        User newUser = new User();
        newUser.setUsername("getbyid_" + System.currentTimeMillis());
        newUser.setPassword("test123456");
        newUser.setRealName("GetById Test");
        newUser.setPhone("13800000000");
        newUser.setUserType(1);
        userService.register(newUser);
        
        User user = userService.getById(newUser.getId());
        assertNotNull(user, "应该能根据ID找到用户");
    }

    @Test
    @DisplayName("测试更新用户信息")
    public void testUpdateUser() {
        // 先注册一个用户
        User newUser = new User();
        newUser.setUsername("update_" + System.currentTimeMillis());
        newUser.setPassword("test123456");
        newUser.setRealName("Update Test");
        newUser.setPhone("13800000001");
        newUser.setUserType(1);
        userService.register(newUser);
        
        User user = userService.getById(newUser.getId());
        assertNotNull(user);
        
        user.setPhone("13900000000");
        
        boolean result = userService.updateById(user);
        assertTrue(result, "更新用户应该成功");
        
        User updatedUser = userService.getById(newUser.getId());
        assertEquals("13900000000", updatedUser.getPhone(), "手机号应该已更新");
    }

    @Test
    @DisplayName("测试密码加密")
    public void testPasswordEncryption() {
        String rawPassword = "test123456";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        assertNotEquals(rawPassword, encodedPassword, "加密后的密码不应该与原密码相同");
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "原密码应该能匹配加密后的密码");
    }

    @Test
    @DisplayName("测试空用户名登录")
    public void testLoginWithEmptyUsername() {
        User user = userService.login("", "123456");
        assertNull(user, "空用户名登录应该返回null");
    }

    @Test
    @DisplayName("测试空密码登录")
    public void testLoginWithEmptyPassword() {
        User user = userService.login("admin", "");
        assertNull(user, "空密码登录应该返回null");
    }
}
