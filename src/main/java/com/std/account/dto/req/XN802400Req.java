/**
 * @Title XN802400Req.java 
 * @Package com.std.account.dto.req 
 * @Description 
 * @author leo(haiqing)  
 * @date 2017年5月24日 下午9:46:04 
 * @version V1.0   
 */
package com.std.account.dto.req;

/** 
 * @author: haiqingzheng 
 * @since: 2017年5月24日 下午9:46:04 
 * @history:
 */
public class XN802400Req {

    // 谁要购买
    private String fromUserId;

    // 向谁购买
    private String toUserId;

    // 购买多少数量
    private String amount;

    // 购买什么币种
    private String currency;

    // 支付方式
    private String payType;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

}
