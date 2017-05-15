package com.std.account.dto.req;

/**
 * 取现分页查询
 * @author: xieyj 
 * @since: 2017年5月12日 上午10:04:01 
 * @history:
 */
public class XN802757Req {
    // 针对账号
    private String accountNumber;

    // 针对户名（手机号或其他）
    private String accountName;

    // 支付渠道
    private String channelType;

    // 状态（待审核/审核不通过/审核通过待支付/支付成功/支付失败）
    private String status;

    // 申请人
    private String applyUser;

    // 审核人
    private String approveUser;

    // 支付回录人
    private String payUser;

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

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public String getPayUser() {
        return payUser;
    }

    public void setPayUser(String payUser) {
        this.payUser = payUser;
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
