package com.qingya.qingxunhuan.controller;

import com.qingya.qingxunhuan.common.Result;
import com.qingya.qingxunhuan.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Map<String, Object>>> list() {
        return Result.success(categoryService.getCategoryTree());
    }
}
