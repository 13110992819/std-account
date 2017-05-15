/**
 * @Title XN802184Req.java 
 * @Package com.std.account.dto.req 
 * @Description 
 * @author haiqingzheng  
 * @date 2016年12月23日 上午9:25:46 
 * @version V1.0   
 */
package com.std.account.dto.req;

/** 
 * 支付宝APP支付请求接口，返回签名后的订单信息
 * @author: haiqingzheng 
 * @since: 2016年12月23日 上午9:25:46 
 * @history:
 */
public class XN002510Req {
    // 发起人(必填)
    private String applyUser;

    // 收款人(必填)
    private String toUser;

    // 支付组号(必填)
    private String payGroup;

    // 订单号(必填)
    private String refNo;

    // 业务类型编号（必填）
    private String bizType;

    // 业务类型说明（必填）
    private String bizNote;

    // 支付资金(必填)
    private String amount;

    // 回调地址(必填)
    private String backUrl;

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizNote() {
        return bizNote;
    }

    public void setBizNote(String bizNote) {
        this.bizNote = bizNote;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

}
