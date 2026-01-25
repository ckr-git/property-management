package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告数据访问层
 * 
 * @author system
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
    
}