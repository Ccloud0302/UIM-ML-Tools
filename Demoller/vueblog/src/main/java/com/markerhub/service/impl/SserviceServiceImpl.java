package com.markerhub.service.impl;

import com.markerhub.entity.Sservice;
import com.markerhub.mapper.SserviceMapper;
import com.markerhub.mapper.knowledgeGraphMapper;
import com.markerhub.service.SserviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SserviceServiceImpl extends ServiceImpl<SserviceMapper, Sservice> implements SserviceService {
    @Autowired
    private SserviceMapper sserviceMapper;
    @Override
    public void addSservice(Sservice sservice) {
        sserviceMapper.addSservice(sservice);
    }

    @Override
    public void deleteSservice(Integer id) {
        sserviceMapper.deleteSservice(id);
    }
}
