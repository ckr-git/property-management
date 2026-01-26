package com.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.community.entity.PropertyPayment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 物业缴费服务接口
 *
 * @author system
 */
public interface PropertyPaymentService extends IService<PropertyPayment> {

    /**
     * 根据房屋ID获取缴费记录
     */
    List<PropertyPayment> getPaymentsByHouseId(Long houseId);

    /**
     * 根据业主ID获取缴费记录
     */
    List<PropertyPayment> getPaymentsByOwnerId(Long ownerId);

    /**
     * 获取业主未缴费账单
     */
    List<PropertyPayment> getUnpaidByOwnerId(Long ownerId);

    /**
     * 生成月度账单
     */
    boolean generateMonthlyBill(Long houseId, Long ownerId, String month);

    /**
     * 缴费操作
     */
    boolean payBill(Long id, BigDecimal amount);

    /**
     * 获取缴费统计
     */
    Map<String, Object> getPaymentStatistics();
}
