package org.example.utilization;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

public class HostInfoProvider {
    public String getHostName() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        return localHost.getHostName();
    }

    public long getCurrentTimestamp() {
        return Instant.now().getEpochSecond();
    }
}
