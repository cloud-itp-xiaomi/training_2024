package org.example.work2.service;

import org.example.work2.pojo.Cpu;

import java.util.List;

public interface CupService {

    boolean add(Cpu cpu);

    List<Cpu> query(String metric, String endpoint, int startTs, int endTs);
}
