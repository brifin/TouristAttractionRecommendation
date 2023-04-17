package com.fuchuang.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.fuchuang.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
