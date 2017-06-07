package com.std.account.dto.req;

/**
 * @author: xieyj 
 * @since: 2017年1月18日 上午11:09:57 
 * @history:
 */
public class XN802013Req {
    // 编号(必填)
    private String code;

    // 户名(选填，有传就修改，没传不修改)
    private String realName;

    // 卡号(必填)
    private String bankcardNumber;

    // 银行行别（必填）
    private String bankCode;

    // 银行名称（必填）
    private String bankName;

    // 支行名称（选填）
    private String subbranch;

    // 绑定手机号（选填）
    private String bindMobile;

    // 状态(必填)
    private String status;

    // 备注（选填）
    private String remark;

    // 交易密码
    private String tradePwd;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBankcardNumber() {
        return bankcardNumber;
    }

    public void setBankcardNumber(String bankcardNumber) {
        this.bankcardNumber = bankcardNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSubbranch() {
        return subbranch;
    }

    public void setSubbranch(String subbranch) {
        this.subbranch = subbranch;
    }

    public String getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(String bindMobile) {
        this.bindMobile = bindMobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }
}
