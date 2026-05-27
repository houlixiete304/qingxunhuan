package com.qingya.qingxunhuan.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingya.qingxunhuan.entity.User;
import com.qingya.qingxunhuan.mapper.UserMapper;
import com.qingya.qingxunhuan.service.UserService;
import com.qingya.qingxunhuan.utils.JwtUtil;
import com.qingya.qingxunhuan.utils.WechatUtil;
import com.qingya.qingxunhuan.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final WechatUtil wechatUtil;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public LoginVO wxLogin(String code, String nickname, String avatar) {
        String openid = wechatUtil.getOpenid(code);

        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname(StrUtil.isNotBlank(nickname) ? nickname : "用户" + RandomUtil.randomNumbers(6));
            user.setAvatar(StrUtil.isNotBlank(avatar) ? avatar : "");
            this.save(user);
        } else {
            if (StrUtil.isNotBlank(nickname)) user.setNickname(nickname);
            if (StrUtil.isNotBlank(avatar)) user.setAvatar(avatar);
            this.updateById(user);
        }

        String token = jwtUtil.generateToken(user.getId(), "user");
        redisTemplate.opsForValue().set("token:user:" + user.getId(), token, 7, TimeUnit.DAYS);

        return LoginVO.builder().token(token).userInfo(user).build();
    }
}
