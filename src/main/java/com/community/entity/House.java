package com.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房屋信息实体类
 * 
 * @author system
 */
@Data
@TableName("house_info")
public class House {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 楼栋号
     */
    private String buildingNo;
    
    /**
     * 单元号
     */
    private String unitNo;
    
    /**
     * 房间号
     */
    private String roomNo;
    
    /**
     * 房屋面积（平方米）
     */
    private BigDecimal area;
    
    /**
     * 房屋类型：1-住宅，2-商铺，3-车位
     */
    private Integer houseType;
    
    /**
     * 业主姓名
     */
    private String ownerName;
    
    /**
     * 业主电话
     */
    private String ownerPhone;
    
    /**
     * 入住状态：0-未入住，1-已入住
     */
    private Integer occupancyStatus;
    
    /**
     * 房屋状态：0-正常，1-出租，2-空置
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}