package com.qingya.qingxunhuan.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qingya.qingxunhuan.common.Result;
import com.qingya.qingxunhuan.entity.Order;
import com.qingya.qingxunhuan.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public Result<IPage<Order>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        return Result.success(orderService.pageAllOrders(page, size, status));
    }
}
