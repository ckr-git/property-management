package com.community.service;

import com.community.entity.Notice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 公告服务单元测试
 */
@SpringBootTest
@Transactional
public class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @Test
    @DisplayName("测试获取已发布公告列表")
    public void testGetPublishedNotices() {
        // 先创建测试数据
        Notice testNotice = new Notice();
        testNotice.setTitle("已发布测试公告");
        testNotice.setContent("测试内容");
        testNotice.setType(1);
        testNotice.setPublisherId(1L);
        testNotice.setPublisherName("测试");
        testNotice.setIsTop(0);
        noticeService.publishNotice(testNotice);
        
        List<Notice> notices = noticeService.getPublishedNotices();
        assertNotNull(notices, "公告列表不应为null");
        assertTrue(notices.size() > 0, "应该有已发布的公告");
        
        // 验证返回的都是已发布状态
        for (Notice notice : notices) {
            assertEquals(1, notice.getStatus(), "公告状态应该为已发布(1)");
        }
    }

    @Test
    @DisplayName("测试获取置顶公告")
    public void testGetTopNotices() {
        List<Notice> topNotices = noticeService.getTopNotices();
        assertNotNull(topNotices, "置顶公告列表不应为null");
        
        // 验证返回的都是置顶公告
        for (Notice notice : topNotices) {
            assertEquals(1, notice.getIsTop(), "应该都是置顶公告");
        }
    }

    @Test
    @DisplayName("测试发布公告")
    public void testPublishNotice() {
        Notice notice = new Notice();
        notice.setTitle("测试公告标题_" + System.currentTimeMillis());
        notice.setContent("这是测试公告内容");
        notice.setType(1);
        notice.setPublisherId(1L);
        notice.setPublisherName("测试发布人");
        notice.setIsTop(0);

        boolean result = noticeService.publishNotice(notice);
        assertTrue(result, "发布公告应该成功");
        assertNotNull(notice.getId(), "公告ID不应为null");
    }

    @Test
    @DisplayName("测试根据ID获取公告")
    public void testGetById() {
        // 先创建测试数据
        Notice testNotice = new Notice();
        testNotice.setTitle("GetById测试公告");
        testNotice.setContent("测试内容");
        testNotice.setType(1);
        testNotice.setPublisherId(1L);
        testNotice.setPublisherName("测试");
        testNotice.setIsTop(0);
        noticeService.publishNotice(testNotice);
        
        Notice notice = noticeService.getById(testNotice.getId());
        assertNotNull(notice, "应该能根据ID找到公告");
        assertNotNull(notice.getTitle(), "公告标题不应为null");
    }

    @Test
    @DisplayName("测试更新公告")
    public void testUpdateNotice() {
        // 先创建测试数据
        Notice testNotice = new Notice();
        testNotice.setTitle("Update测试公告");
        testNotice.setContent("测试内容");
        testNotice.setType(1);
        testNotice.setPublisherId(1L);
        testNotice.setPublisherName("测试");
        testNotice.setIsTop(0);
        noticeService.publishNotice(testNotice);
        
        Notice notice = noticeService.getById(testNotice.getId());
        assertNotNull(notice);
        
        notice.setTitle("更新后的标题_" + System.currentTimeMillis());
        
        boolean result = noticeService.updateById(notice);
        assertTrue(result, "更新公告应该成功");
    }

    @Test
    @DisplayName("测试删除公告")
    public void testDeleteNotice() {
        // 先创建一个测试公告
        Notice notice = new Notice();
        notice.setTitle("待删除测试公告");
        notice.setContent("测试内容");
        notice.setType(1);
        notice.setPublisherId(1L);
        notice.setPublisherName("测试");
        notice.setIsTop(0);
        noticeService.publishNotice(notice);
        
        Long noticeId = notice.getId();
        assertNotNull(noticeId);
        
        // 删除公告
        boolean result = noticeService.removeById(noticeId);
        assertTrue(result, "删除公告应该成功");
        
        // 验证已删除
        Notice deleted = noticeService.getById(noticeId);
        assertNull(deleted, "删除后应该查不到该公告");
    }

    @Test
    @DisplayName("测试公告类型")
    public void testNoticeTypes() {
        // 测试四种公告类型
        int[] types = {1, 2, 3, 4}; // 通知公告、停水停电、活动通知、温馨提示
        
        for (int type : types) {
            Notice notice = new Notice();
            notice.setTitle("类型测试_" + type);
            notice.setContent("测试内容");
            notice.setType(type);
            notice.setPublisherId(1L);
            notice.setPublisherName("测试");
            notice.setIsTop(0);
            
            boolean result = noticeService.publishNotice(notice);
            assertTrue(result, "类型" + type + "公告发布应该成功");
        }
    }

    @Test
    @DisplayName("测试置顶公告功能")
    public void testTopNoticeFunctionality() {
        // 创建置顶公告
        Notice topNotice = new Notice();
        topNotice.setTitle("置顶测试公告");
        topNotice.setContent("测试内容");
        topNotice.setType(1);
        topNotice.setPublisherId(1L);
        topNotice.setPublisherName("测试");
        topNotice.setIsTop(1); // 置顶
        
        boolean result = noticeService.publishNotice(topNotice);
        assertTrue(result, "置顶公告发布应该成功");
        
        // 验证置顶公告能被查到
        List<Notice> topNotices = noticeService.getTopNotices();
        boolean found = topNotices.stream().anyMatch(n -> n.getId().equals(topNotice.getId()));
        assertTrue(found, "新发布的置顶公告应该在置顶列表中");
    }
}
