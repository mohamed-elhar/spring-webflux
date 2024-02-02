package org.group.dto;

public class DnsServerDto {

    private String ipAddress;
    private String zone;

    private Integer timeout;

    public String getIpAddress() {
        return ipAddress;
    }
    public String getZone() {
        return zone;
    }

    public Integer getTimeout() {
        return timeout;
    }

    @Override
    public String toString() {
        return String.format("{ ip address : [%s] , zone : [%s] , timeout : [%d] }", ipAddress, zone, timeout);
    }
}
