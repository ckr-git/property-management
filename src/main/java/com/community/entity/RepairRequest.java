package com.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报修申请实体类
 * 
 * @author system
 */
@Data
@TableName("repair_request")
public class RepairRequest {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 申请人ID
     */
    private Long applicantId;
    
    /**
     * 申请人姓名
     */
    private String applicantName;
    
    /**
     * 申请人电话
     */
    private String applicantPhone;
    
    /**
     * 房屋ID
     */
    private Long houseId;
    
    /**
     * 报修类型：1-水电维修，2-门窗维修，3-电梯维修，4-公共设施，5-其他
     */
    private Integer repairType;
    
    /**
     * 问题描述
     */
    private String description;
    
    /**
     * 报修图片（多个图片用逗号分隔）
     */
    private String images;
    
    /**
     * 处理状态：0-待处理，1-处理中，2-已完成，3-已关闭
     */
    private Integer status;
    
    /**
     * 处理人ID
     */
    private Long handlerId;
    
    /**
     * 处理人姓名
     */
    private String handlerName;
    
    /**
     * 处理备注
     */
    private String handleRemark;
    
    /**
     * 完成时间
     */
    private LocalDateTime completeTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}