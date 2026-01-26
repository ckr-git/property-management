package com.community.controller;

import com.community.common.Result;
import com.community.entity.PropertyPayment;
import com.community.service.PropertyPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PropertyPaymentController {

    @Autowired
    private PropertyPaymentService paymentService;

    @GetMapping("/list")
    public Result<List<PropertyPayment>> list() {
        return Result.success(paymentService.list());
    }

    @GetMapping("/house/{houseId}")
    public Result<List<PropertyPayment>> getByHouse(@PathVariable Long houseId) {
        return Result.success(paymentService.getPaymentsByHouseId(houseId));
    }

    @GetMapping("/owner/{ownerId}")
    public Result<List<PropertyPayment>> getByOwner(@PathVariable Long ownerId) {
        return Result.success(paymentService.getPaymentsByOwnerId(ownerId));
    }

    @GetMapping("/unpaid/{ownerId}")
    public Result<List<PropertyPayment>> getUnpaid(@PathVariable Long ownerId) {
        return Result.success(paymentService.getUnpaidByOwnerId(ownerId));
    }

    @PostMapping("/generate")
    public Result<String> generate(@RequestBody Map<String, Object> params) {
        Long houseId = Long.valueOf(params.get("houseId").toString());
        Long ownerId = Long.valueOf(params.get("ownerId").toString());
        String month = params.get("month").toString();
        boolean success = paymentService.generateMonthlyBill(houseId, ownerId, month);
        return success ? Result.success("生成成功") : Result.error("生成失败");
    }

    @PutMapping("/{id}/pay")
    public Result<String> pay(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        BigDecimal amount = new BigDecimal(params.get("amount").toString());
        boolean success = paymentService.payBill(id, amount);
        return success ? Result.success("缴费成功") : Result.error("缴费失败");
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        return Result.success(paymentService.getPaymentStatistics());
    }
}