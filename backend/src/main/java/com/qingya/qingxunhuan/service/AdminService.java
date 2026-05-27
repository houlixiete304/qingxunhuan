package com.qingya.qingxunhuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingya.qingxunhuan.entity.Admin;
import com.qingya.qingxunhuan.vo.LoginVO;

public interface AdminService extends IService<Admin> {
    LoginVO login(String username, String password);
}
