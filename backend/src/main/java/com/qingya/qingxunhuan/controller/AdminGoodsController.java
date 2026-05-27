package com.qingya.qingxunhuan.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingya.qingxunhuan.common.Result;
import com.qingya.qingxunhuan.entity.Goods;
import com.qingya.qingxunhuan.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/goods")
@RequiredArgsConstructor
public class AdminGoodsController {

    private final GoodsMapper goodsMapper;

    @GetMapping
    public Result<IPage<Goods>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<Goods>()
                .orderByDesc(Goods::getCreateTime);
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Goods::getTitle, keyword);
        }
        return Result.success(goodsMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @PutMapping("/{id}/off-shelf")
    public Result<Void> offShelf(@PathVariable Long id) {
        Goods goods = goodsMapper.selectById(id);
        if (goods != null) {
            goods.setStatus(0);
            goodsMapper.updateById(goods);
        }
        return Result.success();
    }
}
