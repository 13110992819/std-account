package com.std.account.dto.req;

/**
 * 线下充值申请
 * @author: xieyj 
 * @since: 2017年5月12日 上午9:58:02 
 * @history:
 */
public class XN802700Req {

    // 针对账号（必填）
    private String accountNumber;

    // 业务类型
    private String bizType;

    // 充值金额（必填）
    private String amount;

    // 支付渠道账号信息（如开户支行）（选填）
    private String payCardInfo;

    // 支付渠道账号（如银行卡号）（选填）
    private String payCardNo;

    // 申请人（必填）
    private String applyUser;

    // 申请说明（选填）
    private String applyNote;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayCardInfo() {
        return payCardInfo;
    }

    public void setPayCardInfo(String payCardInfo) {
        this.payCardInfo = payCardInfo;
    }

    public String getPayCardNo() {
        return payCardNo;
    }

    public void setPayCardNo(String payCardNo) {
        this.payCardNo = payCardNo;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getApplyNote() {
        return applyNote;
    }

    public void setApplyNote(String applyNote) {
        this.applyNote = applyNote;
    }

}
