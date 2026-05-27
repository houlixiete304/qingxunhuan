package com.qingya.qingxunhuan.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingya.qingxunhuan.entity.Goods;
import com.qingya.qingxunhuan.entity.Order;
import com.qingya.qingxunhuan.mapper.GoodsMapper;
import com.qingya.qingxunhuan.mapper.OrderMapper;
import com.qingya.qingxunhuan.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final GoodsMapper goodsMapper;

    @Override
    @Transactional
    public Order create(Order order, Long buyerId) {
        Goods goods = goodsMapper.selectById(order.getGoodsId());
        if (goods == null || goods.getStatus() == 0) {
            throw new IllegalArgumentException("商品不存在或已下架");
        }
        if (goods.getUserId().equals(buyerId)) {
            throw new IllegalArgumentException("不能购买自己的商品");
        }
        order.setBuyerId(buyerId);
        order.setSellerId(goods.getUserId());
        order.setAmount(goods.getPrice());
        order.setOrderNo(DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(6));
        order.setStatus("PENDING");
        this.save(order);
        return order;
    }

    @Override
    public IPage<Order> pageUserOrders(int page, int size, Long userId, String status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .and(w -> w.eq(Order::getBuyerId, userId).or().eq(Order::getSellerId, userId))
                .orderByDesc(Order::getCreateTime);
        if (StrUtil.isNotBlank(status)) {
            wrapper.eq(Order::getStatus, status);
        }
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public IPage<Order> pageAllOrders(int page, int size, String status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .orderByDesc(Order::getCreateTime);
        if (StrUtil.isNotBlank(status)) {
            wrapper.eq(Order::getStatus, status);
        }
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public void cancel(Long orderId, Long userId) {
        Order order = this.getById(orderId);
        if (order == null || !order.getBuyerId().equals(userId)) {
            throw new IllegalArgumentException("无权操作");
        }
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalArgumentException("仅可取消待付款订单");
        }
        order.setStatus("CANCELLED");
        this.updateById(order);
    }

    @Override
    public void complete(Long orderId, Long userId) {
        Order order = this.getById(orderId);
        if (order == null || !order.getBuyerId().equals(userId)) {
            throw new IllegalArgumentException("无权操作");
        }
        order.setStatus("COMPLETED");
        order.setCompleteTime(LocalDateTime.now());
        this.updateById(order);
    }
}
