package com.qingya.qingxunhuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingya.qingxunhuan.entity.User;
import com.qingya.qingxunhuan.vo.LoginVO;

public interface UserService extends IService<User> {
    LoginVO wxLogin(String code, String nickname, String avatar);
}
