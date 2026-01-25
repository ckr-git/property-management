package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 * 
 * @author system
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
}