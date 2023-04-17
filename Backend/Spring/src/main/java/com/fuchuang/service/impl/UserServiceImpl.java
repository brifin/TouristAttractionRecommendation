package com.fuchuang.service.impl;

import com.fuchuang.entity.User;
import com.fuchuang.mapper.UserMapper;
import com.fuchuang.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
