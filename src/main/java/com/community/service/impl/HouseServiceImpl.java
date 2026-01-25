package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.entity.House;
import com.community.mapper.HouseMapper;
import com.community.service.HouseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 房屋管理服务实现类
 * 
 * @author system
 */
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {

    @Override
    public List<House> getHousesByBuilding(String buildingNo) {
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("building_no", buildingNo);
        return list(queryWrapper);
    }

    @Override
    public List<House> getHousesByOwnerName(String ownerName) {
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("owner_name", ownerName);
        return list(queryWrapper);
    }

    @Override
    public List<House> getVacantHouses() {
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 2); // 空置状态
        return list(queryWrapper);
    }
}