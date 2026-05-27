package com.qingya.qingxunhuan.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {
    private String token;
    private Object userInfo;
}
