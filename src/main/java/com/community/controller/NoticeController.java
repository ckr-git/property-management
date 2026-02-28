package com.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.common.Result;
import com.community.entity.Notice;
import com.community.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告控制器
 * 
 * @author system
 */
@RestController
@RequestMapping("/notice")
@CrossOrigin(origins = "*")
public class NoticeController {
    
    @Autowired
    private NoticeService noticeService;
    
    /**
     * 获取所有已发布公告
     */
    @GetMapping("/list")
    public Result<IPage<Notice>> getNoticeList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<Notice> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getStatus, 1); // 只查已发布
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Notice::getTitle, keyword)
                    .or().like(Notice::getContent, keyword));
        }
        wrapper.orderByDesc(Notice::getIsTop).orderByDesc(Notice::getCreateTime);
        IPage<Notice> result = noticeService.page(page, wrapper);
        return Result.success(result);
    }
    
    /**
     * 获取置顶公告
     */
    @GetMapping("/top")
    public Result<List<Notice>> getTopNotices() {
        List<Notice> notices = noticeService.getTopNotices();
        return Result.success(notices);
    }
    
    /**
     * 根据ID获取公告详情
     */
    @GetMapping("/{id}")
    public Result<Notice> getNoticeById(@PathVariable Long id) {
        Notice notice = noticeService.getById(id);
        if (notice != null) {
            return Result.success(notice);
        } else {
            return Result.error("公告不存在");
        }
    }
    
    /**
     * 发布公告（管理员功能）
     */
    @PostMapping("/publish")
    public Result<Notice> publishNotice(@RequestBody Notice notice) {
        boolean success = noticeService.publishNotice(notice);
        if (success) {
            return Result.success("发布成功", notice);
        } else {
            return Result.error("发布失败");
        }
    }
    
    /**
     * 更新公告（管理员功能）
     */
    @PutMapping("/{id}")
    public Result<String> updateNotice(@PathVariable Long id, @RequestBody Notice notice) {
        notice.setId(id);
        boolean success = noticeService.updateById(notice);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    /**
     * 删除公告（管理员功能）
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteNotice(@PathVariable Long id) {
        boolean success = noticeService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
    
}