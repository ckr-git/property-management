package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.entity.PropertyPayment;
import com.community.mapper.PropertyPaymentMapper;
import com.community.service.PropertyPaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PropertyPaymentServiceImpl extends ServiceImpl<PropertyPaymentMapper, PropertyPayment>
        implements PropertyPaymentService {

    @Override
    public List<PropertyPayment> getPaymentsByHouseId(Long houseId) {
        return list(new LambdaQueryWrapper<PropertyPayment>()
                .eq(PropertyPayment::getHouseId, houseId)
                .orderByDesc(PropertyPayment::getCreateTime));
    }

    @Override
    public List<PropertyPayment> getPaymentsByOwnerId(Long ownerId) {
        return list(new LambdaQueryWrapper<PropertyPayment>()
                .eq(PropertyPayment::getOwnerId, ownerId)
                .orderByDesc(PropertyPayment::getCreateTime));
    }

    @Override
    public List<PropertyPayment> getUnpaidByOwnerId(Long ownerId) {
        return list(new LambdaQueryWrapper<PropertyPayment>()
                .eq(PropertyPayment::getOwnerId, ownerId)
                .eq(PropertyPayment::getStatus, 0)
                .orderByAsc(PropertyPayment::getPaymentMonth));
    }

    @Override
    public boolean generateMonthlyBill(Long houseId, Long ownerId, String month) {
        PropertyPayment payment = new PropertyPayment();
        payment.setHouseId(houseId);
        payment.setOwnerId(ownerId);
        payment.setPaymentMonth(month);
        payment.setPaymentType(1);
        payment.setShouldPayAmount(new BigDecimal("500.00"));
        payment.setStatus(0);
        payment.setCreateTime(LocalDateTime.now());
        return save(payment);
    }

    @Override
    public boolean payBill(Long id, BigDecimal amount) {
        PropertyPayment payment = getById(id);
        if (payment == null) return false;

        payment.setActualPayAmount(amount);
        payment.setStatus(1);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setUpdateTime(LocalDateTime.now());
        return updateById(payment);
    }

    @Override
    public Map<String, Object> getPaymentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<PropertyPayment> all = list();

        long total = all.size();
        long paid = all.stream().filter(p -> p.getStatus() == 1).count();
        BigDecimal totalAmount = all.stream()
                .filter(p -> p.getActualPayAmount() != null)
                .map(PropertyPayment::getActualPayAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        stats.put("total", total);
        stats.put("paid", paid);
        stats.put("unpaid", total - paid);
        stats.put("totalAmount", totalAmount);
        return stats;
    }
}
