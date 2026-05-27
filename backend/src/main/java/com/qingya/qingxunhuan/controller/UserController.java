package com.qingya.qingxunhuan.controller;

import com.qingya.qingxunhuan.common.Result;
import com.qingya.qingxunhuan.dto.WxLoginDTO;
import com.qingya.qingxunhuan.service.UserService;
import com.qingya.qingxunhuan.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/wx-login")
    public Result<LoginVO> wxLogin(@Valid @RequestBody WxLoginDTO dto) {
        LoginVO vo = userService.wxLogin(dto.getCode(), dto.getNickname(), dto.getAvatar());
        return Result.success(vo);
    }
}
