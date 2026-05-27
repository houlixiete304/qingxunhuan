package com.qingya.qingxunhuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingya.qingxunhuan.entity.Category;
import java.util.List;
import java.util.Map;

public interface CategoryService extends IService<Category> {
    List<Map<String, Object>> getCategoryTree();
}
