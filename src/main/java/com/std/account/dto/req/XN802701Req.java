package com.std.account.dto.req;

import java.util.List;

/**
 * 批量审批线下充值订单
 * @author: xieyj 
 * @since: 2017年5月12日 上午9:58:24 
 * @history:
 */
public class XN802701Req {

    // 系统编号(必填)
    private String systemCode;

    // 充值订单编号(必填)
    private List<String> codeList;

    // 支付回录人(必填)
    private String payUser;

    // 审核结果1 通过 0 不通过(必填)
    private String payResult;

    // 支付渠道的说明(必填)
    private String payNote;

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
}
