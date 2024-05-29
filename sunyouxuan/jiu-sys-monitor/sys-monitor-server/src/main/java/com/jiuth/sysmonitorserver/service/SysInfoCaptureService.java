package com.jiuth.sysmonitorserver.service;

import com.jiuth.sysmonitorserver.dao.enity.SysInfoCapture;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SysInfoCaptureService {

//    //捕获信息的增加
//     Long addNewSysInfoCapture(SysInfoCapture sysInfoCapture);

    List<SysInfoCapture> findAll();

    List<SysInfoCapture> saveAll(List<SysInfoCapture> sysInfoCaptures);

    SysInfoCapture findById(Long id);

    void deleteById(Long id);


    //捕获信息的获取


}
