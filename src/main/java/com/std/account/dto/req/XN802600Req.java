package com.std.account.dto.req;

public class XN802600Req {
    // 系统编号(必填)
    private String systemCode;

    // 户名(必填)
    private String accountName;

    // 账号(必填)
    private String accountNumber;

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

}