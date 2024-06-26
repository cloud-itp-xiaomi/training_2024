package com.lx.collector.service;

public interface CollectService {
    Double collectCPU(String proc_stat_path);
    Double collectMem(String proc_mem_path);
}
