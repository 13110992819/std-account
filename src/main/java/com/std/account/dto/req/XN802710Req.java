/**
 * @Title XN802710Req.java 
 * @Package com.std.account.dto.req 
 * @Description 
 * @author leo(haiqing)  
 * @date 2017年5月18日 上午9:49:57 
 * @version V1.0   
 */
package com.std.account.dto.req;

/** 
 * @author: haiqingzheng 
 * @since: 2017年5月18日 上午9:49:57 
 * @history:
 */
public class XN802710Req {

    // 发起人(必填)
    private String applyUser;

    // openId（H5充值时必填）
    private String openId;

    // 支付资金(必填)
    private String amount;

    // 线上充值渠道
    private String channelType;

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

}
