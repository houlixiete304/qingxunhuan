package com.qingya.qingxunhuan.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qingya.qingxunhuan.common.Result;
import com.qingya.qingxunhuan.entity.Goods;
import com.qingya.qingxunhuan.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    @GetMapping
    public Result<IPage<Goods>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String categoryIdStr,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String school) {
        return Result.success(goodsService.pageGoods(page, size, categoryIdStr, keyword, school));
    }

    @GetMapping("/{id}")
    public Result<Goods> detail(@PathVariable Long id) {
        Goods goods = goodsService.getById(id);
        if (goods != null) {
            goods.setViewCount((goods.getViewCount() == null ? 0 : goods.getViewCount()) + 1);
            goodsService.updateById(goods);
        }
        return Result.success(goods);
    }

    @PostMapping
    public Result<Goods> publish(@RequestBody Goods goods) {
        Long userId = getCurrentUserId();
        return Result.success(goodsService.publish(goods, userId));
    }

    @PutMapping("/{id}")
    public Result<Goods> update(@PathVariable Long id, @RequestBody Goods goods) {
        goods.setId(id);
        return Result.success(goodsService.updateGoods(goods, getCurrentUserId()));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        goodsService.deleteGoods(id, getCurrentUserId());
        return Result.success();
    }

    @PostMapping("/{id}/collect")
    public Result<Void> collect(@PathVariable Long id) {
        goodsService.collect(id, getCurrentUserId());
        return Result.success();
    }

    @DeleteMapping("/{id}/collect")
    public Result<Void> uncollect(@PathVariable Long id) {
        goodsService.uncollect(id, getCurrentUserId());
        return Result.success();
    }

    @GetMapping("/{id}/collected")
    public Result<Map<String, Boolean>> isCollected(@PathVariable Long id) {
        boolean collected = goodsService.isCollected(id, getCurrentUserId());
        return Result.success(Map.of("collected", collected));
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
