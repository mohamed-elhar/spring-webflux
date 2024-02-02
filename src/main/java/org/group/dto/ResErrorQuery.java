package org.group.dto;

public class ResErrorQuery {

    private Integer errorCode;
    private String errorMsg;

    public ResErrorQuery(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return "{errorCode : "+errorCode+ " , errorMsg : "+errorMsg+ "}";
    }
}
