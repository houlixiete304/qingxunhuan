package com.qingya.qingxunhuan.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.qingya.qingxunhuan.mapper")
public class MyBatisPlusConfig {
}
