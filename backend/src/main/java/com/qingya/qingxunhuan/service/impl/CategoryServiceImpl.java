package com.qingya.qingxunhuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingya.qingxunhuan.entity.Category;
import com.qingya.qingxunhuan.mapper.CategoryMapper;
import com.qingya.qingxunhuan.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Map<String, Object>> getCategoryTree() {
        List<Category> all = this.list(new LambdaQueryWrapper<Category>().orderByAsc(Category::getSort));
        Map<Long, List<Category>> childrenMap = all.stream()
                .filter(c -> c.getParentId() != null && c.getParentId() > 0)
                .collect(Collectors.groupingBy(Category::getParentId));

        return all.stream()
                .filter(c -> c.getParentId() == null || c.getParentId() == 0)
                .map(root -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", root.getId());
                    map.put("name", root.getName());
                    map.put("icon", root.getIcon());
                    List<Category> children = childrenMap.getOrDefault(root.getId(), Collections.emptyList());
                    map.put("children", children.stream().map(child -> {
                        Map<String, Object> cm = new HashMap<>();
                        cm.put("id", child.getId());
                        cm.put("name", child.getName());
                        cm.put("icon", child.getIcon());
                        return cm;
                    }).collect(Collectors.toList()));
                    return map;
                }).collect(Collectors.toList());
    }
}
