package com.community.controller;

import com.alibaba.fastjson.JSON;
import com.community.entity.House;
import com.community.entity.Notice;
import com.community.entity.RepairRequest;
import com.community.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API接口集成测试
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // ==================== 用户接口测试 ====================
    
    @Test
    @DisplayName("测试用户登录接口")
    public void testLoginApi() throws Exception {
        // 先注册用户
        String username = "apitest_" + System.currentTimeMillis();
        User user = new User();
        user.setUsername(username);
        user.setPassword("test123456");
        user.setRealName("API Test User");
        user.setPhone("13800000000");
        user.setUserType(1);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(user)))
                .andExpect(status().isOk());

        // 然后登录
        mockMvc.perform(post("/user/login")
                .param("username", username)
                .param("password", "test123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value(username));
    }

    @Test
    @DisplayName("测试用户登录失败 - 错误密码")
    public void testLoginApiFail() throws Exception {
        mockMvc.perform(post("/user/login")
                .param("username", "admin")
                .param("password", "wrongpassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    @DisplayName("测试用户注册接口")
    public void testRegisterApi() throws Exception {
        User user = new User();
        user.setUsername("api_test_" + System.currentTimeMillis());
        user.setPassword("test123456");
        user.setRealName("API测试用户");
        user.setPhone("13800000000");
        user.setUserType(1);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("测试获取用户信息接口")
    public void testGetUserApi() throws Exception {
        // 先创建用户
        String username = "getuserapi_" + System.currentTimeMillis();
        User user = new User();
        user.setUsername(username);
        user.setPassword("test123456");
        user.setRealName("Get User API Test");
        user.setPhone("13800000000");
        user.setUserType(1);

        MvcResult registerResult = mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        
        // 从注册响应中获取用户ID
        String responseBody = registerResult.getResponse().getContentAsString();
        com.alibaba.fastjson.JSONObject json = JSON.parseObject(responseBody);
        Long userId = json.getJSONObject("data").getLong("id");
        
        mockMvc.perform(get("/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(userId.intValue()));
    }

    // ==================== 公告接口测试 ====================

    @Test
    @DisplayName("测试获取公告列表接口")
    public void testGetNoticeListApi() throws Exception {
        mockMvc.perform(get("/notice/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("测试获取置顶公告接口")
    public void testGetTopNoticesApi() throws Exception {
        mockMvc.perform(get("/notice/top"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("测试获取公告详情接口")
    public void testGetNoticeByIdApi() throws Exception {
        // 先发布公告
        Notice notice = new Notice();
        notice.setTitle("GetById测试公告_" + System.currentTimeMillis());
        notice.setContent("测试公告内容");
        notice.setType(1);
        notice.setPublisherId(1L);
        notice.setPublisherName("测试发布人");
        notice.setIsTop(0);

        MvcResult publishResult = mockMvc.perform(post("/notice/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(notice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        
        String responseBody = publishResult.getResponse().getContentAsString();
        com.alibaba.fastjson.JSONObject json = JSON.parseObject(responseBody);
        Long noticeId = json.getJSONObject("data").getLong("id");
        
        mockMvc.perform(get("/notice/" + noticeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(noticeId.intValue()));
    }

    @Test
    @DisplayName("测试发布公告接口")
    public void testPublishNoticeApi() throws Exception {
        Notice notice = new Notice();
        notice.setTitle("API测试公告_" + System.currentTimeMillis());
        notice.setContent("测试公告内容");
        notice.setType(1);
        notice.setPublisherId(1L);
        notice.setPublisherName("测试发布人");
        notice.setIsTop(0);

        mockMvc.perform(post("/notice/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(notice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    // ==================== 房屋接口测试 ====================

    @Test
    @DisplayName("测试获取房屋列表接口")
    public void testGetHouseListApi() throws Exception {
        mockMvc.perform(get("/house/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("测试按楼栋查询房屋接口")
    public void testGetHousesByBuildingApi() throws Exception {
        mockMvc.perform(get("/house/building/1号楼"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("测试按业主查询房屋接口")
    public void testGetHousesByOwnerApi() throws Exception {
        mockMvc.perform(get("/house/owner/张三"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("测试获取空置房屋接口")
    public void testGetVacantHousesApi() throws Exception {
        mockMvc.perform(get("/house/vacant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("测试新增房屋接口")
    public void testAddHouseApi() throws Exception {
        House house = new House();
        house.setBuildingNo("API测试楼");
        house.setUnitNo("1单元");
        house.setRoomNo("888");
        house.setArea(new BigDecimal("100.5"));
        house.setHouseType(1);
        house.setOwnerName("API测试业主");
        house.setOwnerPhone("13800000000");
        house.setOccupancyStatus(0);
        house.setStatus(0);

        mockMvc.perform(post("/house/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(house)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    // ==================== 报修接口测试 ====================

    @Test
    @DisplayName("测试获取报修列表接口")
    public void testGetRepairListApi() throws Exception {
        mockMvc.perform(get("/repair/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("测试获取待处理报修接口")
    public void testGetPendingRepairsApi() throws Exception {
        mockMvc.perform(get("/repair/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("测试提交报修申请接口")
    public void testSubmitRepairApi() throws Exception {
        RepairRequest repair = new RepairRequest();
        repair.setApplicantId(2L);
        repair.setApplicantName("API测试申请人");
        repair.setApplicantPhone("13800000000");
        repair.setHouseId(1L);
        repair.setRepairType(1);
        repair.setDescription("API测试报修问题描述");

        mockMvc.perform(post("/repair/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(repair)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("测试获取报修详情接口")
    public void testGetRepairByIdApi() throws Exception {
        // 先提交报修
        RepairRequest repair = new RepairRequest();
        repair.setApplicantId(1L);
        repair.setApplicantName("GetById测试申请人");
        repair.setApplicantPhone("13800000000");
        repair.setHouseId(1L);
        repair.setRepairType(1);
        repair.setDescription("GetById测试报修问题描述");

        MvcResult submitResult = mockMvc.perform(post("/repair/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(repair)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        
        String responseBody = submitResult.getResponse().getContentAsString();
        com.alibaba.fastjson.JSONObject json = JSON.parseObject(responseBody);
        Long repairId = json.getJSONObject("data").getLong("id");
        
        mockMvc.perform(get("/repair/" + repairId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    // ==================== 安全测试 ====================

    @Test
    @DisplayName("测试SQL注入防护 - 登录接口")
    public void testSqlInjectionInLoginApi() throws Exception {
        mockMvc.perform(post("/user/login")
                .param("username", "admin' OR '1'='1")
                .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    @DisplayName("测试获取不存在的用户")
    public void testGetNonExistentUser() throws Exception {
        mockMvc.perform(get("/user/999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    @DisplayName("测试获取不存在的公告")
    public void testGetNonExistentNotice() throws Exception {
        mockMvc.perform(get("/notice/999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    // ==================== 边界测试 ====================

    @Test
    @DisplayName("测试空参数登录")
    public void testLoginWithEmptyParams() throws Exception {
        mockMvc.perform(post("/user/login")
                .param("username", "")
                .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    @DisplayName("测试中文参数查询")
    public void testChineseQueryParam() throws Exception {
        mockMvc.perform(get("/house/building/1号楼"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
