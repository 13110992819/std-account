package com.std.account.dto.req;

/**
 * 内部转账(指定用户编号和币种进行转账，备注分开)比517接口详细
 * @author: xieyj 
 * @since: 2016年12月25日 下午3:25:44 
 * @history:
 */
public class XN002100Req {

    // 来方用户编号(必填)
    private String fromUserId;

    // 来方币种(必填)
    private String fromCurrency;

    // 接收方用户编号(必填)
    private String toUserId;

    // 去方币种(必填)
    private String toCurrency;

    // 划转资金(必填)
    private String transAmount;

    // 业务类型(必填)
    private String bizType;

    // 来方业务说明(必填)
    private String fromBizNote;

    // 去方业务说明(必填)
    private String toBizNote;

    // 参考订单号(必填)
    private String refNo;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getFromBizNote() {
        return fromBizNote;
    }

    public void setFromBizNote(String fromBizNote) {
        this.fromBizNote = fromBizNote;
    }

    public String getToBizNote() {
        return toBizNote;
    }

    public void setToBizNote(String toBizNote) {
        this.toBizNote = toBizNote;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }
}
