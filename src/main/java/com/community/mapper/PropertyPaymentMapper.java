package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.PropertyPayment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物业缴费记录数据访问层
 * 
 * @author system
 */
@Mapper
public interface PropertyPaymentMapper extends BaseMapper<PropertyPayment> {
    
}