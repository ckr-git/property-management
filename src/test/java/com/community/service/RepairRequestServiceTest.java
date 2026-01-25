package com.community.service;

import com.community.entity.RepairRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 报修申请服务单元测试
 */
@SpringBootTest
@Transactional
public class RepairRequestServiceTest {

    @Autowired
    private RepairRequestService repairRequestService;

    @Test
    @DisplayName("测试获取所有报修列表")
    public void testGetAllRepairs() {
        // 先创建测试数据
        RepairRequest testRepair = new RepairRequest();
        testRepair.setApplicantId(1L);
        testRepair.setApplicantName("测试");
        testRepair.setApplicantPhone("13800000000");
        testRepair.setHouseId(1L);
        testRepair.setRepairType(1);
        testRepair.setDescription("测试报修");
        testRepair.setStatus(0);
        testRepair.setCreateTime(LocalDateTime.now());
        testRepair.setUpdateTime(LocalDateTime.now());
        repairRequestService.save(testRepair);
        
        List<RepairRequest> repairs = repairRequestService.list();
        assertNotNull(repairs, "报修列表不应为null");
        assertTrue(repairs.size() > 0, "应该有报修数据");
    }

    @Test
    @DisplayName("测试获取待处理报修")
    public void testGetPendingRepairs() {
        List<RepairRequest> pendingRepairs = repairRequestService.getPendingRepairs();
        assertNotNull(pendingRepairs, "待处理报修列表不应为null");
        
        // 验证返回的都是待处理状态
        for (RepairRequest repair : pendingRepairs) {
            assertEquals(0, repair.getStatus(), "状态应该为待处理(0)");
        }
    }

    @Test
    @DisplayName("测试根据申请人ID获取报修")
    public void testGetRepairsByApplicant() {
        List<RepairRequest> repairs = repairRequestService.getRepairsByApplicant(2L);
        assertNotNull(repairs, "报修列表不应为null");
        
        // 验证返回的都是指定申请人
        for (RepairRequest repair : repairs) {
            assertEquals(2L, repair.getApplicantId(), "申请人ID应该匹配");
        }
    }

    @Test
    @DisplayName("测试根据ID获取报修详情")
    public void testGetById() {
        // 先创建测试数据
        RepairRequest testRepair = new RepairRequest();
        testRepair.setApplicantId(1L);
        testRepair.setApplicantName("测试");
        testRepair.setApplicantPhone("13800000000");
        testRepair.setHouseId(1L);
        testRepair.setRepairType(1);
        testRepair.setDescription("GetById测试报修");
        testRepair.setStatus(0);
        testRepair.setCreateTime(LocalDateTime.now());
        testRepair.setUpdateTime(LocalDateTime.now());
        repairRequestService.save(testRepair);
        
        RepairRequest repair = repairRequestService.getById(testRepair.getId());
        assertNotNull(repair, "应该能根据ID找到报修记录");
        assertNotNull(repair.getDescription(), "问题描述不应为null");
    }

    @Test
    @DisplayName("测试提交报修申请")
    public void testSubmitRepair() {
        RepairRequest repair = new RepairRequest();
        repair.setApplicantId(2L);
        repair.setApplicantName("测试申请人");
        repair.setApplicantPhone("13800000000");
        repair.setHouseId(1L);
        repair.setRepairType(1);
        repair.setDescription("测试报修问题描述");
        repair.setStatus(0);
        repair.setCreateTime(LocalDateTime.now());
        repair.setUpdateTime(LocalDateTime.now());

        boolean result = repairRequestService.save(repair);
        assertTrue(result, "提交报修应该成功");
        assertNotNull(repair.getId(), "报修ID不应为null");
    }

    @Test
    @DisplayName("测试处理报修申请")
    public void testHandleRepair() {
        // 先创建一个测试报修
        RepairRequest repair = new RepairRequest();
        repair.setApplicantId(2L);
        repair.setApplicantName("测试");
        repair.setApplicantPhone("13800000000");
        repair.setHouseId(1L);
        repair.setRepairType(1);
        repair.setDescription("待处理测试");
        repair.setStatus(0);
        repair.setCreateTime(LocalDateTime.now());
        repair.setUpdateTime(LocalDateTime.now());
        repairRequestService.save(repair);
        
        // 处理报修
        boolean result = repairRequestService.handleRepairRequest(
            repair.getId(), 1L, "管理员", "已安排维修");
        assertTrue(result, "处理报修应该成功");
        
        // 验证状态已更新
        RepairRequest updated = repairRequestService.getById(repair.getId());
        assertEquals(1, updated.getStatus(), "状态应该更新为处理中(1)");
        assertEquals(1L, updated.getHandlerId(), "处理人ID应该正确");
        assertEquals("管理员", updated.getHandlerName(), "处理人姓名应该正确");
    }

    @Test
    @DisplayName("测试完成报修申请")
    public void testCompleteRepair() {
        // 先创建一个处理中的报修
        RepairRequest repair = new RepairRequest();
        repair.setApplicantId(2L);
        repair.setApplicantName("测试");
        repair.setApplicantPhone("13800000000");
        repair.setHouseId(1L);
        repair.setRepairType(1);
        repair.setDescription("处理中测试");
        repair.setStatus(1);  // 处理中
        repair.setHandlerId(1L);
        repair.setHandlerName("管理员");
        repair.setCreateTime(LocalDateTime.now());
        repair.setUpdateTime(LocalDateTime.now());
        repairRequestService.save(repair);
        
        // 完成报修
        boolean result = repairRequestService.completeRepairRequest(
            repair.getId(), "已维修完成");
        assertTrue(result, "完成报修应该成功");
        
        // 验证状态已更新
        RepairRequest updated = repairRequestService.getById(repair.getId());
        assertEquals(2, updated.getStatus(), "状态应该更新为已完成(2)");
        assertNotNull(updated.getCompleteTime(), "完成时间不应为null");
    }

    @Test
    @DisplayName("测试删除报修申请")
    public void testDeleteRepair() {
        // 先创建一个测试报修
        RepairRequest repair = new RepairRequest();
        repair.setApplicantId(2L);
        repair.setApplicantName("待删除");
        repair.setApplicantPhone("13800000000");
        repair.setHouseId(1L);
        repair.setRepairType(1);
        repair.setDescription("待删除测试");
        repair.setStatus(0);
        repair.setCreateTime(LocalDateTime.now());
        repair.setUpdateTime(LocalDateTime.now());
        repairRequestService.save(repair);
        
        Long repairId = repair.getId();
        assertNotNull(repairId);
        
        // 删除报修
        boolean result = repairRequestService.removeById(repairId);
        assertTrue(result, "删除报修应该成功");
        
        // 验证已删除
        RepairRequest deleted = repairRequestService.getById(repairId);
        assertNull(deleted, "删除后应该查不到该报修");
    }

    @Test
    @DisplayName("测试报修类型")
    public void testRepairTypes() {
        // 测试五种报修类型
        int[] types = {1, 2, 3, 4, 5}; // 水电、门窗、电梯、公共设施、其他
        
        for (int type : types) {
            RepairRequest repair = new RepairRequest();
            repair.setApplicantId(2L);
            repair.setApplicantName("类型测试");
            repair.setApplicantPhone("13800000000");
            repair.setHouseId(1L);
            repair.setRepairType(type);
            repair.setDescription("类型" + type + "测试");
            repair.setStatus(0);
            repair.setCreateTime(LocalDateTime.now());
            repair.setUpdateTime(LocalDateTime.now());
            
            boolean result = repairRequestService.save(repair);
            assertTrue(result, "类型" + type + "报修保存应该成功");
        }
    }

    @Test
    @DisplayName("测试报修状态流转")
    public void testRepairStatusFlow() {
        // 创建报修 - 状态0(待处理)
        RepairRequest repair = new RepairRequest();
        repair.setApplicantId(2L);
        repair.setApplicantName("状态流转测试");
        repair.setApplicantPhone("13800000000");
        repair.setHouseId(1L);
        repair.setRepairType(1);
        repair.setDescription("状态流转测试");
        repair.setStatus(0);
        repair.setCreateTime(LocalDateTime.now());
        repair.setUpdateTime(LocalDateTime.now());
        repairRequestService.save(repair);
        assertEquals(0, repair.getStatus(), "初始状态应该为待处理(0)");
        
        // 处理报修 - 状态变为1(处理中)
        repairRequestService.handleRepairRequest(repair.getId(), 1L, "管理员", "正在处理");
        RepairRequest step1 = repairRequestService.getById(repair.getId());
        assertEquals(1, step1.getStatus(), "处理后状态应该为处理中(1)");
        
        // 完成报修 - 状态变为2(已完成)
        repairRequestService.completeRepairRequest(repair.getId(), "已完成");
        RepairRequest step2 = repairRequestService.getById(repair.getId());
        assertEquals(2, step2.getStatus(), "完成后状态应该为已完成(2)");
    }

    @Test
    @DisplayName("测试处理不存在的报修")
    public void testHandleNonExistentRepair() {
        boolean result = repairRequestService.handleRepairRequest(
            999999L, 1L, "管理员", "测试");
        assertFalse(result, "处理不存在的报修应该失败");
    }
}
