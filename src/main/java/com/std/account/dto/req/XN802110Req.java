package com.std.account.dto.req;

public class XN802110Req {
    // 用户账号
    private String accountNumber;

    // 充值金额（精确到厘）
    private String amount;

    // 来方类型
    private String fromType;

    // 来方编号
    private String fromCode;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getFromCode() {
        return fromCode;
    }

    public void setFromCode(String fromCode) {
        this.fromCode = fromCode;
    }

}
