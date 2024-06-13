package com.example.hostcollector.service;

import java.io.IOException;
import java.net.UnknownHostException;

public interface ScheduleService {

    void timedCollection() throws IOException;
}
