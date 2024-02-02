package org.group.dto;

import java.util.List;

public class NAPTRQueryDto {
    private String reqId;
    private String called;
    private List<DnsServerDto> dnsServers;

    public String getReqId() {
        return reqId;
    }

    public String getCalled() {
        return called;
    }

    public List<DnsServerDto> getDnsServers() {
        return dnsServers;
    }

    @Override
    public String toString() {
        return "Request ID: ["+reqId+"] , Called: ["+called+"] , Dns servers: "+ dnsServers;
    }
}
