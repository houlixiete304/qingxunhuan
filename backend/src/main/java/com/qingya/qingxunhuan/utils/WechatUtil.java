package com.qingya.qingxunhuan.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WechatUtil {

    @Value("${wechat.appid:}")
    private String appid;

    @Value("${wechat.secret:}")
    private String secret;

    private static final String CODE2SESSION_URL =
            "https://api.weixin.qq.com/sns/jscode2session?appid={}&secret={}&js_code={}&grant_type=authorization_code";

    public String getOpenid(String code) {
        if (StrUtil.isBlank(appid) || StrUtil.isBlank(secret)) {
            log.warn("微信配置未设置，使用模拟openid");
            return "mock_openid_" + code;
        }
        String url = StrUtil.format(CODE2SESSION_URL, appid, secret, code);
        String result = HttpUtil.get(url);
        JSONObject json = JSONUtil.parseObj(result);
        String openid = json.getStr("openid");
        if (StrUtil.isBlank(openid)) {
            log.error("微信登录失败: {}", result);
            throw new IllegalArgumentException("微信登录失败: " + json.getStr("errmsg", "未知错误"));
        }
        return openid;
    }
}
