package com.qingya.qingxunhuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingya.qingxunhuan.entity.Message;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

public interface MessageMapper extends BaseMapper<Message> {

    @Select("SELECT m.*, u.nickname, u.avatar FROM message m " +
            "LEFT JOIN user u ON u.id = IF(m.from_user_id = #{userId}, m.to_user_id, m.from_user_id) " +
            "WHERE m.id IN (SELECT MAX(id) FROM message " +
            "WHERE from_user_id = #{userId} OR to_user_id = #{userId} GROUP BY " +
            "IF(from_user_id = #{userId}, to_user_id, from_user_id)) " +
            "ORDER BY m.create_time DESC")
    List<Map<String, Object>> selectConversations(Long userId);
}
