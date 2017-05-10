package com.std.account.enums;

public enum EWithdrawStatus {
    toApprove("1", "待审批"), Approved_NO("2", "审批不通过"), Approved_YES("3",
            "审批通过待支付"), Pay_NO("4", "支付失败"), Pay_YES("5", "支付成功");
    EWithdrawStatus(String code, String value) {
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
