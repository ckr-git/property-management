package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.RepairRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报修申请数据访问层
 * 
 * @author system
 */
@Mapper
public interface RepairRequestMapper extends BaseMapper<RepairRequest> {
    
}