package com.jiuth.sysmonitorserver.service;

import com.jiuth.sysmonitorserver.dao.SysInfoCaptureRepository;
import com.jiuth.sysmonitorserver.dao.enity.SysInfoCapture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysInfoCaptureServiceImpl implements SysInfoCaptureService {
    @Autowired
    private SysInfoCaptureRepository sysInfoCaptureRepository;

    public List<SysInfoCapture> findAll() {
        return sysInfoCaptureRepository.findAll();
    }

    public SysInfoCapture save(SysInfoCapture sysInfoCapture) {
        return sysInfoCaptureRepository.save(sysInfoCapture);
    }

    public List<SysInfoCapture> saveAll(List<SysInfoCapture> sysInfoCaptures) {
        return sysInfoCaptureRepository.saveAll(sysInfoCaptures);
    }

    public void deleteById(Long id) {
        sysInfoCaptureRepository.deleteById(id);
    }

    public SysInfoCapture findById(Long id) {
        return sysInfoCaptureRepository.findById(id).orElse(null);
    }
}
