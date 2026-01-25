package com.community.controller;

import com.community.common.Result;
import com.community.entity.House;
import com.community.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 房屋管理控制器
 * 
 * @author system
 */
@RestController
@RequestMapping("/house")
@CrossOrigin(origins = "*")
public class HouseController {
    
    @Autowired
    private HouseService houseService;
    
    /**
     * 获取所有房屋列表
     */
    @GetMapping("/list")
    public Result<List<House>> getHouseList() {
        List<House> houses = houseService.list();
        return Result.success(houses);
    }
    
    /**
     * 根据楼栋号查询房屋
     */
    @GetMapping("/building/{buildingNo}")
    public Result<List<House>> getHousesByBuilding(@PathVariable String buildingNo) {
        List<House> houses = houseService.getHousesByBuilding(buildingNo);
        return Result.success(houses);
    }
    
    /**
     * 根据业主姓名查询房屋
     */
    @GetMapping("/owner/{ownerName}")
    public Result<List<House>> getHousesByOwnerName(@PathVariable String ownerName) {
        List<House> houses = houseService.getHousesByOwnerName(ownerName);
        return Result.success(houses);
    }
    
    /**
     * 获取空置房屋列表
     */
    @GetMapping("/vacant")
    public Result<List<House>> getVacantHouses() {
        List<House> houses = houseService.getVacantHouses();
        return Result.success(houses);
    }
    
    /**
     * 根据ID获取房屋详情
     */
    @GetMapping("/{id}")
    public Result<House> getHouseById(@PathVariable Long id) {
        House house = houseService.getById(id);
        if (house != null) {
            return Result.success(house);
        } else {
            return Result.error("房屋不存在");
        }
    }
    
    /**
     * 新增房屋信息（管理员功能）
     */
    @PostMapping("/add")
    public Result<String> addHouse(@RequestBody House house) {
        boolean success = houseService.save(house);
        if (success) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }
    
    /**
     * 更新房屋信息（管理员功能）
     */
    @PutMapping("/{id}")
    public Result<String> updateHouse(@PathVariable Long id, @RequestBody House house) {
        house.setId(id);
        boolean success = houseService.updateById(house);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    /**
     * 删除房屋信息（管理员功能）
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteHouse(@PathVariable Long id) {
        boolean success = houseService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}