package com.fuchuang.service.impl;

import com.fuchuang.entity.Message;
import com.fuchuang.mapper.MessageMapper;
import com.fuchuang.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
