package org.example.rest;

import org.example.log.Log;
import org.example.utilization.UtilizationDocument;
import org.springframework.web.client.RestTemplate;

public class DataSender {
    private final RestTemplate restTemplate;
    private final RestConfig restConfig;

    public DataSender(RestTemplate restTemplate, RestConfig restConfig) {
        this.restTemplate = restTemplate;
        this.restConfig = restConfig;
    }

    public void sendUtilizationData(UtilizationDocument document) {
        try {
            restTemplate.postForLocation(restConfig.getUtilizationUrl(), document);
            System.out.println("Utilization Data sent to server successfully");
        } catch (Exception e) {
            System.out.println("Failed to send utilization data to server: " + e.getMessage());
        }
    }

    public void sendLogData(Log log) {
        try {
            restTemplate.postForLocation(restConfig.getLogUrl(), log);
            System.out.println("Log Data sent to server successfully");
        } catch (Exception e) {
            System.out.println("Failed to send log data to server: " + e.getMessage());
        }
    }
}
