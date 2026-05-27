package com.qingya.qingxunhuan.controller;

import com.qingya.qingxunhuan.common.Result;
import com.qingya.qingxunhuan.mapper.GoodsMapper;
import com.qingya.qingxunhuan.mapper.OrderMapper;
import com.qingya.qingxunhuan.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
public class AdminStatsController {

    private final UserMapper userMapper;
    private final GoodsMapper goodsMapper;
    private final OrderMapper orderMapper;

    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userMapper.selectCount(null));
        stats.put("goodsCount", goodsMapper.selectCount(null));
        stats.put("orderCount", orderMapper.selectCount(null));
        return Result.success(stats);
    }
}
