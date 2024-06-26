package org.example.work2.service.impl;

import org.example.work2.mapper.CupMapper;
import org.example.work2.pojo.Cpu;
import org.example.work2.service.CupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CupServiceimpl implements CupService {
    @Autowired
     private   CupMapper cupMapper;


    @Override
    public boolean add(Cpu cpu) {
        cupMapper.add(cpu);
        return true;
    }

    @Override
    public List<Cpu> query(String metric, String endpoint, int startTs, int endTs) {
        return cupMapper.list(metric,endpoint,startTs,endTs);
    }
}
