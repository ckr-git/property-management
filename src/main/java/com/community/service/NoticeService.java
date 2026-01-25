package com.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.community.entity.Notice;

import java.util.List;

/**
 * 公告服务接口
 * 
 * @author system
 */
public interface NoticeService extends IService<Notice> {
    
    /**
     * 获取已发布的公告列表
     * @return 公告列表
     */
    List<Notice> getPublishedNotices();
    
    /**
     * 获取置顶公告
     * @return 置顶公告列表
     */
    List<Notice> getTopNotices();
    
    /**
     * 发布公告
     * @param notice 公告信息
     * @return 是否发布成功
     */
    boolean publishNotice(Notice notice);
    
}