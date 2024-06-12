package com.jiuth.sysmonitorserver.service.impl;

import com.jiuth.sysmonitorserver.dao.SysInfoCaptureRepository;
import com.jiuth.sysmonitorserver.dao.enity.SysInfoCapture;
import com.jiuth.sysmonitorserver.service.SysInfoCaptureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysInfoCaptureServiceImpl implements SysInfoCaptureService {
    @Autowired
    private SysInfoCaptureRepository sysInfoCaptureRepository;

    @Override
    public List<SysInfoCapture> findAll() {
        return sysInfoCaptureRepository.findAll();
    }

    public SysInfoCapture save(SysInfoCapture sysInfoCapture) {
        return sysInfoCaptureRepository.save(sysInfoCapture);
    }

    @Override
    public List<SysInfoCapture> saveAll(List<SysInfoCapture> sysInfoCaptures) {
        return sysInfoCaptureRepository.saveAll(sysInfoCaptures);
    }

    @Override
    public void deleteById(Long id) {
        sysInfoCaptureRepository.deleteById(id);
    }

    @Override
    public List<SysInfoCapture> query(String endpoint, String metric, long start_ts, long end_ts) {

        return sysInfoCaptureRepository.findByEndpointAndMetricAndTimestampBetween(endpoint, metric, start_ts, end_ts);
    }

    @Override
    public SysInfoCapture findById(Long id) {
        return sysInfoCaptureRepository.findById(id).orElse(null);
    }


}
