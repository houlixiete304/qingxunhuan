package com.qingya.qingxunhuan.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingya.qingxunhuan.entity.Collect;
import com.qingya.qingxunhuan.entity.Goods;
import com.qingya.qingxunhuan.mapper.CollectMapper;
import com.qingya.qingxunhuan.mapper.GoodsMapper;
import com.qingya.qingxunhuan.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private final CollectMapper collectMapper;

    @Override
    public IPage<Goods> pageGoods(int page, int size, String categoryIdStr, String keyword, String school) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<Goods>()
                .eq(Goods::getStatus, 1)
                .orderByDesc(Goods::getCreateTime);
        Long categoryId = null;
        if (categoryIdStr != null && !"undefined".equals(categoryIdStr) && !"null".equals(categoryIdStr) && !categoryIdStr.isEmpty()) {
            categoryId = Long.valueOf(categoryIdStr);
        }
        if (categoryId != null) {
            wrapper.eq(Goods::getCategoryId, categoryId);
        }
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Goods::getTitle, keyword);
        }
        if (StrUtil.isNotBlank(school)) {
            wrapper.eq(Goods::getSchool, school);
        }
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public Goods publish(Goods goods, Long userId) {
        goods.setUserId(userId);
        goods.setStatus(1);
        goods.setViewCount(0);
        goods.setCollectCount(0);
        this.save(goods);
        return goods;
    }

    @Override
    public Goods updateGoods(Goods goods, Long userId) {
        Goods exist = this.getById(goods.getId());
        if (exist == null || !exist.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权操作");
        }
        goods.setUserId(userId);
        this.updateById(goods);
        return this.getById(goods.getId());
    }

    @Override
    public void deleteGoods(Long id, Long userId) {
        Goods exist = this.getById(id);
        if (exist == null || !exist.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权操作");
        }
        this.removeById(id);
    }

    @Override
    public void collect(Long goodsId, Long userId) {
        Long count = collectMapper.selectCount(
                new LambdaQueryWrapper<Collect>()
                        .eq(Collect::getGoodsId, goodsId)
                        .eq(Collect::getUserId, userId));
        if (count > 0) return;
        Collect collect = new Collect();
        collect.setGoodsId(goodsId);
        collect.setUserId(userId);
        collectMapper.insert(collect);
        Goods goods = this.getById(goodsId);
        if (goods != null) {
            goods.setCollectCount((goods.getCollectCount() == null ? 0 : goods.getCollectCount()) + 1);
            this.updateById(goods);
        }
    }

    @Override
    public void uncollect(Long goodsId, Long userId) {
        collectMapper.delete(new LambdaQueryWrapper<Collect>()
                .eq(Collect::getGoodsId, goodsId)
                .eq(Collect::getUserId, userId));
    }

    @Override
    public boolean isCollected(Long goodsId, Long userId) {
        return collectMapper.selectCount(
                new LambdaQueryWrapper<Collect>()
                        .eq(Collect::getGoodsId, goodsId)
                        .eq(Collect::getUserId, userId)) > 0;
    }
}
