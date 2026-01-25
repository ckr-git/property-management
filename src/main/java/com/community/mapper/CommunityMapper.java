package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.Community;
import org.apache.ibatis.annotations.Mapper;

/**
 * 小区信息数据访问层
 * 
 * @author system
 */
@Mapper
public interface CommunityMapper extends BaseMapper<Community> {
    
}