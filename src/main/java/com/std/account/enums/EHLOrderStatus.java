package com.std.account.enums;

public enum EHLOrderStatus {
    toApprove("1", "待审批"), Approved_NO("2", "审批不通过"), Approved_YES("3", "审批通过");

    EHLOrderStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
