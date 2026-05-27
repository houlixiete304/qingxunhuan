package com.qingya.qingxunhuan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingya.qingxunhuan.entity.Order;

public interface OrderService extends IService<Order> {
    Order create(Order order, Long buyerId);
    IPage<Order> pageUserOrders(int page, int size, Long userId, String status);
    IPage<Order> pageAllOrders(int page, int size, String status);
    void cancel(Long orderId, Long userId);
    void complete(Long orderId, Long userId);
}
