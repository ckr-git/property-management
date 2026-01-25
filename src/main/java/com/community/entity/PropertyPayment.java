package com.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物业缴费记录实体类
 * 
 * @author system
 */
@Data
@TableName("property_payment")
public class PropertyPayment {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 房屋ID
     */
    private Long houseId;
    
    /**
     * 业主ID
     */
    private Long ownerId;
    
    /**
     * 缴费类型：1-物业费，2-停车费，3-水费，4-电费，5-燃气费
     */
    private Integer paymentType;
    
    /**
     * 费用月份（格式：2023-01）
     */
    private String paymentMonth;
    
    /**
     * 应缴金额
     */
    private BigDecimal shouldPayAmount;
    
    /**
     * 实缴金额
     */
    private BigDecimal actualPayAmount;
    
    /**
     * 缴费状态：0-未缴费，1-已缴费，2-部分缴费
     */
    private Integer status;
    
    /**
     * 缴费时间
     */
    private LocalDateTime paymentTime;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}