package com.std.account.dto.req;

/**
 * 列表查询充值订单
 * @author: xieyj 
 * @since: 2017年5月12日 上午10:01:08 
 * @history:
 */
public class XN802707Req {
    // 针对账号
    private String accountNumber;

    // 针对户名（手机号或其他）
    private String accountName;

    // 支付渠道
    private String channelType;

    // 状态（待审核/审核不通过/审核通过）
    private String status;

    // 申请人
    private String applyUser;

    // 申请时间起
    private String applyDateStart;

    // 申请时间止
    private String applyDateEnd;

    // 支付回录人
    private String payUser;

    // 支付时间起
    private String payDateStart;

    // 支付时间止
    private String payDateEnd;

    // 支付组号（信息流代表）
    private String payGroup;

    // 支付渠道的订单编号（支付渠道代表）
    private String payCode;

    // 系统编号
    private String systemCode;

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

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
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

    public String getPayUser() {
        return payUser;
    }

    public void setPayUser(String payUser) {
        this.payUser = payUser;
    }

    public String getPayDateStart() {
        return payDateStart;
    }

    public void setPayDateStart(String payDateStart) {
        this.payDateStart = payDateStart;
    }

    public String getPayDateEnd() {
        return payDateEnd;
    }

    public void setPayDateEnd(String payDateEnd) {
        this.payDateEnd = payDateEnd;
    }

    public String getPayGroup() {
        return payGroup;
    }

    public void setPayGroup(String payGroup) {
        this.payGroup = payGroup;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}
