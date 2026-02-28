package com.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.community.entity.House;

import java.util.List;

/**
 * 房屋管理服务接口
 * 
 * @author system
 */
public interface HouseService extends IService<House> {
    
    /**
     * 根据楼栋号查询房屋列表
     * @param buildingNo 楼栋号
     * @return 房屋列表
     */
    List<House> getHousesByBuilding(String buildingNo);
    
    /**
     * 根据业主姓名查询房屋
     * @param ownerName 业主姓名
     * @return 房屋列表
     */
    List<House> getHousesByOwnerName(String ownerName);
    
    /**
     * 获取空置房屋列表
     * @return 空置房屋列表
     */
    List<House> getVacantHouses();

    /**
     * 根据业主用户ID获取关联房屋
     * @param ownerId 业主用户ID
     * @return 房屋信息
     */
    House getHouseByOwnerId(Long ownerId);

}
