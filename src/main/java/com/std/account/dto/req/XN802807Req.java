package com.std.account.dto.req;

public class XN802807Req {
    // 针对账号
    private String accountNumber;

    // 针对户名（手机号或其他）
    private String accountName;

    // 币种
    private String currency;

    // 流水号
    private String jourCode;

    // 渠道类型
    private String channelType;

    // 红冲还是蓝补
    private String direction;

    // 状态（待审批/审批通过/审批不通过）
    private String status;

    // 对账人
    private String applyUser;

    // 对账时间起
    private String applyDateStart;

    // 对账时间止
    private String applyDateEnd;

    // 调账人
    private String approveUser;

    // 调账时间起
    private String approveDateStart;

    // 调账时间止
    private String approveDateEnd;

    // 系统编号
    private String systemCode;

    // 公司编号
    private String companyCode;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public String getJourCode() {
        return jourCode;
    }

    public void setJourCode(String jourCode) {
        this.jourCode = jourCode;
    }

    public String getApplyDateStart() {
        return applyDateStart;
    }

    public void setApplyDateStart(String applyDateStart) {
        this.applyDateStart = applyDateStart;
    }

    public String getApplyDateEnd() {
        return applyDateEnd;
    }

    public void setApplyDateEnd(String applyDateEnd) {
        this.applyDateEnd = applyDateEnd;
    }

    public String getApproveDateStart() {
        return approveDateStart;
    }

    public void setApproveDateStart(String approveDateStart) {
        this.approveDateStart = approveDateStart;
    }

    public String getApproveDateEnd() {
        return approveDateEnd;
    }

    public void setApproveDateEnd(String approveDateEnd) {
        this.approveDateEnd = approveDateEnd;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
