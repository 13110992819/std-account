package com.std.account.dto.req;

public class XN802211Req {
    // 账号
    private String accountNumber;

    // 行别（alipay支付宝）
    private String toType;

    // 开户支行
    private String toCode;

    // 金额（精确到厘）
    private String amount;

    // 交易密码(必填)
    private String tradePwd;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public String getToCode() {
        return toCode;
    }

    public void setToCode(String toCode) {
        this.toCode = toCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }

}
