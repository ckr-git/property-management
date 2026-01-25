package com.community.controller;

import com.community.common.Result;
import com.community.entity.RepairRequest;
import com.community.service.RepairRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修申请控制器
 * 
 * @author system
 */
@RestController
@RequestMapping("/repair")
@CrossOrigin(origins = "*")
public class RepairRequestController {
    
    @Autowired
    private RepairRequestService repairRequestService;
    
    /**
     * 获取所有报修申请列表（管理员功能）
     */
    @GetMapping("/list")
    public Result<List<RepairRequest>> getRepairList() {
        List<RepairRequest> repairs = repairRequestService.list();
        return Result.success(repairs);
    }
    
    /**
     * 获取待处理报修申请（管理员功能）
     */
    @GetMapping("/pending")
    public Result<List<RepairRequest>> getPendingRepairs() {
        List<RepairRequest> repairs = repairRequestService.getPendingRepairs();
        return Result.success(repairs);
    }
    
    /**
     * 根据申请人ID获取报修记录（业主功能）
     */
    @GetMapping("/applicant/{applicantId}")
    public Result<List<RepairRequest>> getRepairsByApplicant(@PathVariable Long applicantId) {
        List<RepairRequest> repairs = repairRequestService.getRepairsByApplicant(applicantId);
        return Result.success(repairs);
    }
    
    /**
     * 根据ID获取报修详情
     */
    @GetMapping("/{id}")
    public Result<RepairRequest> getRepairById(@PathVariable Long id) {
        RepairRequest repair = repairRequestService.getById(id);
        if (repair != null) {
            return Result.success(repair);
        } else {
            return Result.error("报修记录不存在");
        }
    }
    
    /**
     * 提交报修申请（业主功能）
     */
    @PostMapping("/submit")
    public Result<RepairRequest> submitRepair(@RequestBody RepairRequest repairRequest) {
        repairRequest.setStatus(0); // 设置为待处理
        repairRequest.setCreateTime(LocalDateTime.now());
        repairRequest.setUpdateTime(LocalDateTime.now());
        
        boolean success = repairRequestService.save(repairRequest);
        if (success) {
            return Result.success("提交成功", repairRequest);
        } else {
            return Result.error("提交失败");
        }
    }
    
    /**
     * 处理报修申请（管理员功能）
     */
    @PutMapping("/{id}/handle")
    public Result<String> handleRepair(
            @PathVariable Long id,
            @RequestParam Long handlerId,
            @RequestParam String handlerName,
            @RequestParam String remark) {
        
        boolean success = repairRequestService.handleRepairRequest(id, handlerId, handlerName, remark);
        if (success) {
            return Result.success("处理成功");
        } else {
            return Result.error("处理失败");
        }
    }
    
    /**
     * 完成报修申请（管理员功能）
     */
    @PutMapping("/{id}/complete")
    public Result<String> completeRepair(@PathVariable Long id, @RequestParam String remark) {
        boolean success = repairRequestService.completeRepairRequest(id, remark);
        if (success) {
            return Result.success("完成成功");
        } else {
            return Result.error("完成失败");
        }
    }
    
    /**
     * 更新报修申请
     */
    @PutMapping("/{id}")
    public Result<String> updateRepair(@PathVariable Long id, @RequestBody RepairRequest repairRequest) {
        repairRequest.setId(id);
        repairRequest.setUpdateTime(LocalDateTime.now());
        boolean success = repairRequestService.updateById(repairRequest);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    /**
     * 删除报修申请（管理员功能）
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteRepair(@PathVariable Long id) {
        boolean success = repairRequestService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}