package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.entity.RepairRequest;
import com.community.mapper.RepairRequestMapper;
import com.community.service.RepairRequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修申请服务实现类
 * 
 * @author system
 */
@Service
public class RepairRequestServiceImpl extends ServiceImpl<RepairRequestMapper, RepairRequest> implements RepairRequestService {

    @Override
    public List<RepairRequest> getPendingRepairs() {
        QueryWrapper<RepairRequest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0) // 待处理状态
                   .orderByDesc("create_time");
        return list(queryWrapper);
    }

    @Override
    public List<RepairRequest> getRepairsByApplicant(Long applicantId) {
        QueryWrapper<RepairRequest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("applicant_id", applicantId)
                   .orderByDesc("create_time");
        return list(queryWrapper);
    }

    @Override
    public boolean handleRepairRequest(Long id, Long handlerId, String handlerName, String remark) {
        RepairRequest repairRequest = getById(id);
        if (repairRequest != null) {
            repairRequest.setStatus(1); // 设置为处理中
            repairRequest.setHandlerId(handlerId);
            repairRequest.setHandlerName(handlerName);
            repairRequest.setHandleRemark(remark);
            repairRequest.setUpdateTime(LocalDateTime.now());
            return updateById(repairRequest);
        }
        return false;
    }

    @Override
    public boolean completeRepairRequest(Long id, String remark) {
        RepairRequest repairRequest = getById(id);
        if (repairRequest != null) {
            repairRequest.setStatus(2); // 设置为已完成
            repairRequest.setHandleRemark(remark);
            repairRequest.setCompleteTime(LocalDateTime.now());
            repairRequest.setUpdateTime(LocalDateTime.now());
            return updateById(repairRequest);
        }
        return false;
    }
}