package com.std.account.dto.req;

public class XN802414Req {
    // 来方用户编号
    private String fromUserId;

    // 去方手机号
    private String toMobile;

    // 来方划转金额(必填)
    private String amount;

    // 交易密码(必填)
    private String tradePwd;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToMobile() {
        return toMobile;
    }

    public void setToMobile(String toMobile) {
        this.toMobile = toMobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }
}
