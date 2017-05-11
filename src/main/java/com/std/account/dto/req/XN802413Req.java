package com.std.account.dto.req;

/**
 * 平台对加盟商划转资金
 * @author: xieyj 
 * @since: 2017年4月14日 下午1:00:41 
 * @history:
 */
public class XN802413Req {
    // 来方用户编号
    private String fromUserId;

    // 来方币种(必填)
    private String fromCurrency;

    // 去方用户编号
    private String toUserId;

    // 去方币种(必填)
    private String toCurrency;

    // 来方划转金额(必填)
    private String amount;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
