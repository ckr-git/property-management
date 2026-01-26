package com.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.entity.Community;
import com.community.mapper.CommunityMapper;
import com.community.service.CommunityService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community>
        implements CommunityService {

    @Override
    public Community getCommunityInfo() {
        return getById(1L);
    }

    @Override
    public boolean updateCommunityInfo(Community community) {
        community.setUpdateTime(LocalDateTime.now());
        return updateById(community);
    }
}
