package com.fuchuang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuchuang.entity.Attractions;
import com.fuchuang.mapper.AttractionsMapper;
import com.fuchuang.service.AttractionsService;
import org.springframework.stereotype.Service;

@Service
public class AttractionsServiceImpl extends ServiceImpl<AttractionsMapper, Attractions> implements AttractionsService {
}
