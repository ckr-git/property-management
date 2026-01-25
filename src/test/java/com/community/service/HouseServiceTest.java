package com.community.service;

import com.community.entity.House;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 房屋服务单元测试
 */
@SpringBootTest
@Transactional
public class HouseServiceTest {

    @Autowired
    private HouseService houseService;

    @Test
    @DisplayName("测试获取所有房屋列表")
    public void testGetAllHouses() {
        // 先创建测试数据
        House testHouse = new House();
        testHouse.setBuildingNo("TestAll");
        testHouse.setUnitNo("1Unit");
        testHouse.setRoomNo("001");
        testHouse.setArea(new BigDecimal("88.0"));
        testHouse.setHouseType(1);
        testHouse.setOccupancyStatus(0);
        testHouse.setStatus(0);
        houseService.save(testHouse);
        
        List<House> houses = houseService.list();
        assertNotNull(houses, "房屋列表不应为null");
        assertTrue(houses.size() > 0, "应该有房屋数据");
    }

    @Test
    @DisplayName("测试根据楼栋号查询房屋")
    public void testGetHousesByBuilding() {
        // 先创建测试数据
        String buildingNo = "B" + (System.currentTimeMillis() % 10000);
        House testHouse = new House();
        testHouse.setBuildingNo(buildingNo);
        testHouse.setUnitNo("1Unit");
        testHouse.setRoomNo("101");
        testHouse.setArea(new BigDecimal("100.0"));
        testHouse.setHouseType(1);
        testHouse.setOccupancyStatus(0);
        testHouse.setStatus(0);
        houseService.save(testHouse);
        
        List<House> houses = houseService.getHousesByBuilding(buildingNo);
        assertNotNull(houses, "房屋列表不应为null");
        assertTrue(houses.size() > 0, "应该能查到房屋");
        
        // 验证返回的都是指定楼栋
        for (House house : houses) {
            assertEquals(buildingNo, house.getBuildingNo(), "楼栋号应该匹配");
        }
    }

    @Test
    @DisplayName("测试根据业主姓名查询房屋")
    public void testGetHousesByOwnerName() {
        // 先创建测试数据
        String ownerName = "Owner" + (System.currentTimeMillis() % 100000);
        House testHouse = new House();
        testHouse.setBuildingNo("TestB");
        testHouse.setUnitNo("1Unit");
        testHouse.setRoomNo("102");
        testHouse.setArea(new BigDecimal("100.0"));
        testHouse.setHouseType(1);
        testHouse.setOwnerName(ownerName);
        testHouse.setOwnerPhone("13800000000");
        testHouse.setOccupancyStatus(0);
        testHouse.setStatus(0);
        houseService.save(testHouse);
        
        List<House> houses = houseService.getHousesByOwnerName(ownerName);
        assertNotNull(houses, "房屋列表不应为null");
        assertTrue(houses.size() > 0, "应该能查到房屋");
        
        // 验证返回的包含指定业主
        for (House house : houses) {
            assertTrue(house.getOwnerName().contains(ownerName), "业主姓名应该匹配");
        }
    }

    @Test
    @DisplayName("测试获取空置房屋列表")
    public void testGetVacantHouses() {
        List<House> vacantHouses = houseService.getVacantHouses();
        assertNotNull(vacantHouses, "空置房屋列表不应为null");
        
        // 验证返回的都是空置状态
        for (House house : vacantHouses) {
            assertEquals(2, house.getStatus(), "状态应该为空置(2)");
        }
    }

    @Test
    @DisplayName("测试根据ID获取房屋")
    public void testGetById() {
        // 先创建测试数据
        House testHouse = new House();
        testHouse.setBuildingNo("GetById");
        testHouse.setUnitNo("1Unit");
        testHouse.setRoomNo("002");
        testHouse.setArea(new BigDecimal("95.0"));
        testHouse.setHouseType(1);
        testHouse.setOccupancyStatus(0);
        testHouse.setStatus(0);
        houseService.save(testHouse);
        
        House house = houseService.getById(testHouse.getId());
        assertNotNull(house, "应该能根据ID找到房屋");
        assertNotNull(house.getBuildingNo(), "楼栋号不应为null");
    }

    @Test
    @DisplayName("测试新增房屋")
    public void testAddHouse() {
        House house = new House();
        house.setBuildingNo("测试楼");
        house.setUnitNo("1单元");
        house.setRoomNo("888");
        house.setArea(new BigDecimal("100.5"));
        house.setHouseType(1);
        house.setOwnerName("测试业主");
        house.setOwnerPhone("13800000000");
        house.setOccupancyStatus(0);
        house.setStatus(0);

        boolean result = houseService.save(house);
        assertTrue(result, "新增房屋应该成功");
        assertNotNull(house.getId(), "房屋ID不应为null");
    }

    @Test
    @DisplayName("测试更新房屋信息")
    public void testUpdateHouse() {
        // 先创建测试数据
        House testHouse = new House();
        testHouse.setBuildingNo("UpdateTest");
        testHouse.setUnitNo("1Unit");
        testHouse.setRoomNo("003");
        testHouse.setArea(new BigDecimal("90.0"));
        testHouse.setHouseType(1);
        testHouse.setOwnerPhone("13800000001");
        testHouse.setOccupancyStatus(0);
        testHouse.setStatus(0);
        houseService.save(testHouse);
        
        House house = houseService.getById(testHouse.getId());
        assertNotNull(house);
        
        house.setOwnerPhone("13900000000");
        
        boolean result = houseService.updateById(house);
        assertTrue(result, "更新房屋应该成功");
        
        House updatedHouse = houseService.getById(testHouse.getId());
        assertEquals("13900000000", updatedHouse.getOwnerPhone(), "业主电话应该已更新");
    }

    @Test
    @DisplayName("测试删除房屋")
    public void testDeleteHouse() {
        // 先创建一个测试房屋
        House house = new House();
        house.setBuildingNo("待删除楼");
        house.setUnitNo("1单元");
        house.setRoomNo("999");
        house.setArea(new BigDecimal("80.0"));
        house.setHouseType(1);
        house.setOccupancyStatus(0);
        house.setStatus(0);
        houseService.save(house);
        
        Long houseId = house.getId();
        assertNotNull(houseId);
        
        // 删除房屋
        boolean result = houseService.removeById(houseId);
        assertTrue(result, "删除房屋应该成功");
        
        // 验证已删除
        House deleted = houseService.getById(houseId);
        assertNull(deleted, "删除后应该查不到该房屋");
    }

    @Test
    @DisplayName("测试房屋类型")
    public void testHouseTypes() {
        // 测试三种房屋类型: 1-住宅, 2-商铺, 3-车位
        int[] types = {1, 2, 3};
        
        for (int type : types) {
            House house = new House();
            house.setBuildingNo("类型测试楼");
            house.setUnitNo("1单元");
            house.setRoomNo("T" + type);
            house.setArea(new BigDecimal("50.0"));
            house.setHouseType(type);
            house.setOccupancyStatus(0);
            house.setStatus(0);
            
            boolean result = houseService.save(house);
            assertTrue(result, "类型" + type + "房屋保存应该成功");
        }
    }

    @Test
    @DisplayName("测试房屋状态")
    public void testHouseStatus() {
        // 测试三种状态: 0-正常, 1-出租, 2-空置
        int[] statuses = {0, 1, 2};
        
        for (int status : statuses) {
            House house = new House();
            house.setBuildingNo("状态测试楼");
            house.setUnitNo("1单元");
            house.setRoomNo("S" + status);
            house.setArea(new BigDecimal("60.0"));
            house.setHouseType(1);
            house.setOccupancyStatus(0);
            house.setStatus(status);
            
            boolean result = houseService.save(house);
            assertTrue(result, "状态" + status + "房屋保存应该成功");
        }
    }

    @Test
    @DisplayName("测试不存在的楼栋查询")
    public void testGetHousesByNonExistentBuilding() {
        List<House> houses = houseService.getHousesByBuilding("不存在的楼栋");
        assertNotNull(houses, "房屋列表不应为null");
        assertEquals(0, houses.size(), "不存在的楼栋应该返回空列表");
    }
}
