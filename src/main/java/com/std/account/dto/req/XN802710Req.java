package com.std.account.dto.req;

public class XN802710Req {
    // 用户编号(必填)
    private String userId;

    // 充值金额(必填)
    private String amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
