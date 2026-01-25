package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.entity.Notice;
import com.community.mapper.NoticeMapper;
import com.community.service.NoticeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告服务实现类
 * 
 * @author system
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public List<Notice> getPublishedNotices() {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1) // 已发布
                   .orderByDesc("is_top") // 置顶优先
                   .orderByDesc("create_time"); // 按创建时间倒序
        return list(queryWrapper);
    }

    @Override
    public List<Notice> getTopNotices() {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1)
                   .eq("is_top", 1)
                   .orderByDesc("create_time");
        return list(queryWrapper);
    }

    @Override
    public boolean publishNotice(Notice notice) {
        notice.setStatus(1); // 设置为已发布
        notice.setCreateTime(LocalDateTime.now());
        notice.setUpdateTime(LocalDateTime.now());
        return save(notice);
    }
}