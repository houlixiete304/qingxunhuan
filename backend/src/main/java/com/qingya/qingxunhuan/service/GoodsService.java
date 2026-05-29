package com.qingya.qingxunhuan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingya.qingxunhuan.entity.Goods;

public interface GoodsService extends IService<Goods> {
    IPage<Goods> pageGoods(int page, int size, String categoryIdStr, String keyword, String school);
    Goods publish(Goods goods, Long userId);
    Goods updateGoods(Goods goods, Long userId);
    void deleteGoods(Long id, Long userId);
    void collect(Long goodsId, Long userId);
    void uncollect(Long goodsId, Long userId);
    boolean isCollected(Long goodsId, Long userId);
}
