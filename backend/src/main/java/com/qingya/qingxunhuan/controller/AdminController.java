package com.qingya.qingxunhuan.controller;

import com.qingya.qingxunhuan.common.Result;
import com.qingya.qingxunhuan.dto.AdminLoginDTO;
import com.qingya.qingxunhuan.service.AdminService;
import com.qingya.qingxunhuan.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody AdminLoginDTO dto) {
        LoginVO vo = adminService.login(dto.getUsername(), dto.getPassword());
        return Result.success(vo);
    }
}
