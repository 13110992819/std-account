package com.std.account.dto.req;

import java.util.List;

public class XN802701Req {

    // 系统编号(必填)
    private String systemCode;

    // 取现订单编号(必填)
    private List<String> codeList;

    // 支付回录人(必填)
    private String payUser;

    // 审核结果(必填)
    private String payResult;

    // 支付渠道的说明(必填)
    private String payNote;

    // 支付渠道的订单编号（支付渠道代表）(选填)
    private String payCode;

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    public String getPayUser() {
        return payUser;
    }

    public void setPayUser(String payUser) {
        this.payUser = payUser;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

    public String getPayNote() {
        return payNote;
    }

    public void setPayNote(String payNote) {
        this.payNote = payNote;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

}
