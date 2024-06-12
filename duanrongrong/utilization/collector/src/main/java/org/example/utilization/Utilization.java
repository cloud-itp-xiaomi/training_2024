package org.example.utilization;

import org.example.rest.DataSender;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimerTask;

public class Utilization extends TimerTask {
    private final CPUUtilizationReader cpuReader;
    private final MemUtilizationReader memReader;
    private final HostInfoProvider hostInfoProvider;
    private final DataSender dataSender;

    private final String cpuName = "cpu.used.percent";
    private final String memName = "mem.used.percent";
    private final long step = 60L;

    @Autowired
    public Utilization(CPUUtilizationReader cpuReader, MemUtilizationReader memReader,
                       HostInfoProvider hostInfoProvider, DataSender dataSender) {
        this.cpuReader = cpuReader;
        this.memReader = memReader;
        this.hostInfoProvider = hostInfoProvider;
        this.dataSender = dataSender;
    }

    @Override
    public void run() {
        try {
            Float cpuUtilization = cpuReader.getCpuUtilization();
            Float memUtilization = memReader.getMemUtilization();
            String hostName = hostInfoProvider.getHostName();
            long timestamp = hostInfoProvider.getCurrentTimestamp();

            UtilizationDocument cpuDocument = new UtilizationDocument();
            cpuDocument.setEndpoint(hostName);
            cpuDocument.setMetric(cpuName);
            cpuDocument.setTimestamp(timestamp);
            cpuDocument.setStep(step);
            cpuDocument.setValue(cpuUtilization);

            UtilizationDocument memDocument = new UtilizationDocument();
            memDocument.setEndpoint(hostName);
            memDocument.setMetric(memName);
            memDocument.setTimestamp(timestamp);
            memDocument.setStep(step);
            memDocument.setValue(memUtilization);

            dataSender.sendUtilizationData(cpuDocument);
            dataSender.sendUtilizationData(memDocument);
        } catch (Exception e) {
            System.out.println("Failed to send data to server: " + e.getMessage());
        }
    }
}
