/**
 * @Title XN802900Req.java 
 * @Package com.std.account.dto.req 
 * @Description 
 * @author leo(haiqing)  
 * @date 2017年5月18日 下午7:25:29 
 * @version V1.0   
 */
package com.std.account.dto.req;

/** 
 * @author: haiqingzheng 
 * @since: 2017年5月18日 下午7:25:29 
 * @history:
 */
public class XN802900Req {

    // 账户名称（必填）
    private String accountNumber;

    // 业务类型（必填）
    private String bizType;

    // 渠道（选填）
    private String channelType;

    public String getBizType() {
        return bizType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

}
