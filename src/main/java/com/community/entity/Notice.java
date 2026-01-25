package com.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公告实体类
 * 
 * @author system
 */
@Data
@TableName("notice")
public class Notice {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 公告标题
     */
    private String title;
    
    /**
     * 公告内容
     */
    private String content;
    
    /**
     * 公告类型：1-通知公告，2-停水停电，3-活动通知，4-温馨提示
     */
    private Integer type;
    
    /**
     * 发布人ID
     */
    private Long publisherId;
    
    /**
     * 发布人姓名
     */
    private String publisherName;
    
    /**
     * 发布状态：0-草稿，1-已发布
     */
    private Integer status;
    
    /**
     * 是否置顶：0-否，1-是
     */
    private Integer isTop;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}