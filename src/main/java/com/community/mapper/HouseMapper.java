package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.House;
import org.apache.ibatis.annotations.Mapper;

/**
 * 房屋信息数据访问层
 * 
 * @author system
 */
@Mapper
public interface HouseMapper extends BaseMapper<House> {
    
}