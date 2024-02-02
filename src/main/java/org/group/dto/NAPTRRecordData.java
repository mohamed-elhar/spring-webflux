package org.group.dto;

import java.io.Serializable;

public class NAPTRRecordData implements Serializable {
    private Integer order;
    private Integer preference;
    private String service;
    private String regex;
    private Integer resCode;

    public NAPTRRecordData() {

    }
    public NAPTRRecordData(Integer order, Integer preference, String service, String regex) {
        this.order = order;
        this.preference = preference;
        this.service = service;
        this.regex = regex;
    }
    public Integer getOrder() {
        return order;
    }
    public Integer getPreference() {
        return preference;
    }

    public String getService() {
        return service;
    }

    public String getRegex() {
        return regex;
    }

    public Integer getResCode() {
        return resCode;
    }

    public void setResCode(Integer resCode) {
        this.resCode = resCode;
    }

    @Override
    public String toString() {
        return "order: " + order + ", preference: " + preference + ", service: " + service + ", regex: " + regex;
    }
}
