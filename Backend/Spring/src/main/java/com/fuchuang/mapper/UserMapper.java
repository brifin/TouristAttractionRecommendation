package com.fuchuang.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.fuchuang.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}