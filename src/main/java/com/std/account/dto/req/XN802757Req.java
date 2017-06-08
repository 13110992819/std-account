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

    // 类型
    private String type;

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
    private String channelOrder;

    // 系统编号
    private String systemCode;

    // 公司编号
    private String companyCode;

    // 申请时间起
    private String applyDateStart;

    // 申请时间止
    private String applyDateEnd;

    // 审批时间起
    private String approveDateStart;

    // 审批时间止
    private String approveDateEnd;

    // 支付时间起
    private String payDateStart;

    // 支付时间止
    private String payDateEnd;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getChannelOrder() {
        return channelOrder;
    }

    public void setChannelOrder(String channelOrder) {
        this.channelOrder = channelOrder;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
