package com.qingya.qingxunhuan.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qingya.qingxunhuan.common.Result;
import com.qingya.qingxunhuan.entity.Message;
import com.qingya.qingxunhuan.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageMapper messageMapper;

    @PostMapping
    public Result<Message> send(@RequestBody Message message) {
        message.setFromUserId(getUserId());
        message.setIsRead(0);
        messageMapper.insert(message);
        return Result.success(message);
    }

    @GetMapping("/list")
    public Result<List<?>> conversations() {
        return Result.success(messageMapper.selectConversations(getUserId()));
    }

    @GetMapping("/{userId}")
    public Result<List<Message>> chatHistory(@PathVariable Long userId) {
        Long me = getUserId();
        List<Message> list = messageMapper.selectList(
                new LambdaQueryWrapper<Message>()
                        .and(w -> w.eq(Message::getFromUserId, me).eq(Message::getToUserId, userId))
                        .or(w -> w.eq(Message::getFromUserId, userId).eq(Message::getToUserId, me))
                        .orderByAsc(Message::getCreateTime));
        return Result.success(list);
    }

    @PutMapping("/read/{userId}")
    public Result<Void> markRead(@PathVariable Long userId) {
        Message update = new Message();
        update.setIsRead(1);
        messageMapper.update(update, new LambdaQueryWrapper<Message>()
                .eq(Message::getFromUserId, userId)
                .eq(Message::getToUserId, getUserId())
                .eq(Message::getIsRead, 0));
        return Result.success();
    }

    private Long getUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
