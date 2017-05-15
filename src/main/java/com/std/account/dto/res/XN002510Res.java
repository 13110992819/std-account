/**
 * @Title XN802182Res.java 
 * @Package com.std.account.dto.res 
 * @Description 
 * @author haiqingzheng  
 * @date 2016年12月26日 下午4:46:41 
 * @version V1.0   
 */
package com.std.account.dto.res;

/** 
 * @author: haiqingzheng 
 * @since: 2016年12月26日 下午4:46:41 
 * @history:
 */
public class XN002510Res {

    // 橙账本流水编号
    private String payCode;

    // 签名后的订单信息
    private String signOrder;

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getSignOrder() {
        return signOrder;
    }

    public void setSignOrder(String signOrder) {
        this.signOrder = signOrder;
    }

}
