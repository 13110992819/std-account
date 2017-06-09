package com.std.account.dto.req;

/**
 * 充值订单分页查询
 * @author: xieyj 
 * @since: 2017年5月12日 上午9:59:55 
 * @history:
 */
public class XN802705Req extends APageReq {

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */
    private static final long serialVersionUID = -8656333710276407945L;

    // 支付组号（信息流代表）
    private String payGroup;

    // 流水分组组号（橙账本代表）---核心字段2
    private String refNo;

    // 针对账号
    private String accountNumber;

    // 针对户名（手机号或其他）
    private String accountName;

    // 类型
    private String type;

    // 币种
    private String currency;

    // 业务类型编号（因为什么业务类型而充值）
    private String bizType;

    // 支付渠道账号（如银行卡号）
    private String payCardNo;

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

    // 支付渠道
    private String channelType;

    // 系统编号
    private String systemCode;

    // 公司编号
    private String companyCode;

    public String getPayGroup() {
        return payGroup;
    }

    public void setPayGroup(String payGroup) {
        this.payGroup = payGroup;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getPayCardNo() {
        return payCardNo;
    }

    public void setPayCardNo(String payCardNo) {
        this.payCardNo = payCardNo;
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

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
