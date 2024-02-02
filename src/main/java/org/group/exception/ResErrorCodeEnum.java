package org.group.exception;

public enum ResErrorCodeEnum {
    INTERNAL_ERROR_CODE(10),
    NO_RESPONSE_CODE(11),
    UNKNOWN_HOST_CODE(12),
    TEXT_PARSE_CODE(13),
    TIMEOUT_CODE(14);

    private Integer code;

    private ResErrorCodeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
