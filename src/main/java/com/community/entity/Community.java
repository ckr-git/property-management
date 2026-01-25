package com.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 小区信息实体类
 * 
 * @author system
 */
@Data
@TableName("community_info")
public class Community {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 小区名称
     */
    private String name;
    
    /**
     * 小区地址
     */
    private String address;
    
    /**
     * 物业公司
     */
    private String propertyCompany;
    
    /**
     * 物业费标准（元/月/平方米）
     */
    private BigDecimal propertyFee;
    
    /**
     * 停车费标准（元/月）
     */
    private BigDecimal parkingFee;
    
    /**
     * 小区介绍
     */
    private String description;
    
    /**
     * 联系电话
     */
    private String contactPhone;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}