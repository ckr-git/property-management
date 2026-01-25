package com.community.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT工具类单元测试
 */
@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("测试生成JWT令牌")
    public void testGenerateToken() {
        String token = jwtUtil.generateToken("admin");
        assertNotNull(token, "生成的令牌不应为null");
        assertFalse(token.isEmpty(), "令牌不应为空");
        assertTrue(token.split("\\.").length == 3, "JWT令牌应该包含三部分");
    }

    @Test
    @DisplayName("测试从令牌获取用户名")
    public void testGetUsernameFromToken() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);
        
        String extractedUsername = jwtUtil.getUsernameFromToken(token);
        assertEquals(username, extractedUsername, "提取的用户名应该匹配");
    }

    @Test
    @DisplayName("测试验证有效令牌")
    public void testValidateValidToken() {
        String token = jwtUtil.generateToken("admin");
        boolean isValid = jwtUtil.validateToken(token);
        assertTrue(isValid, "有效令牌验证应该通过");
    }

    @Test
    @DisplayName("测试验证无效令牌")
    public void testValidateInvalidToken() {
        String invalidToken = "invalid.token.here";
        boolean isValid = jwtUtil.validateToken(invalidToken);
        assertFalse(isValid, "无效令牌验证应该失败");
    }

    @Test
    @DisplayName("测试验证空令牌")
    public void testValidateEmptyToken() {
        boolean isValid = jwtUtil.validateToken("");
        assertFalse(isValid, "空令牌验证应该失败");
    }

    @Test
    @DisplayName("测试验证null令牌")
    public void testValidateNullToken() {
        boolean isValid = jwtUtil.validateToken(null);
        assertFalse(isValid, "null令牌验证应该失败");
    }

    @Test
    @DisplayName("测试不同用户生成不同令牌")
    public void testDifferentUsersGenerateDifferentTokens() {
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");
        
        assertNotEquals(token1, token2, "不同用户应该生成不同的令牌");
    }

    @Test
    @DisplayName("测试令牌格式")
    public void testTokenFormat() {
        String token = jwtUtil.generateToken("admin");
        
        // JWT格式: header.payload.signature
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT应该由三部分组成");
        
        // 每部分都不应为空
        for (String part : parts) {
            assertFalse(part.isEmpty(), "JWT的每部分都不应为空");
        }
    }

    @Test
    @DisplayName("测试篡改令牌后验证失败")
    public void testTamperedTokenValidation() {
        String token = jwtUtil.generateToken("admin");
        
        // 篡改令牌
        String tamperedToken = token.substring(0, token.length() - 5) + "aaaaa";
        
        boolean isValid = jwtUtil.validateToken(tamperedToken);
        assertFalse(isValid, "篡改后的令牌验证应该失败");
    }
}
