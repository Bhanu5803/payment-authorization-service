package com.bridgeline.authorize.enums;

public enum AuthorizationStatusEnum {
    APPROVED("100"),
    DECLINED("101"),
    HELD("102");


    private final String code;

    AuthorizationStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
