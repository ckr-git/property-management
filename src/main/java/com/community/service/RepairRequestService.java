package com.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.community.entity.RepairRequest;

import java.util.List;

/**
 * 报修申请服务接口
 * 
 * @author system
 */
public interface RepairRequestService extends IService<RepairRequest> {
    
    /**
     * 获取待处理的报修申请
     * @return 待处理报修列表
     */
    List<RepairRequest> getPendingRepairs();
    
    /**
     * 根据申请人ID获取报修记录
     * @param applicantId 申请人ID
     * @return 报修记录列表
     */
    List<RepairRequest> getRepairsByApplicant(Long applicantId);
    
    /**
     * 处理报修申请
     * @param id 报修ID
     * @param handlerId 处理人ID
     * @param handlerName 处理人姓名
     * @param remark 处理备注
     * @return 是否处理成功
     */
    boolean handleRepairRequest(Long id, Long handlerId, String handlerName, String remark);
    
    /**
     * 完成报修申请
     * @param id 报修ID
     * @param remark 完成备注
     * @return 是否完成成功
     */
    boolean completeRepairRequest(Long id, String remark);
    
}