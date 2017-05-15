/**
 * @Title IAlipayAO.java 
 * @Package com.std.account.ao 
 * @Description 
 * @author haiqingzheng  
 * @date 2017年1月11日 下午8:55:34 
 * @version V1.0   
 */
package com.std.account.ao;

/** 
 * @author: haiqingzheng 
 * @since: 2017年1月11日 下午8:55:34 
 * @history:
 */
public interface IAlipayAO {
    // 支付宝APP支付，第一步：获取签名后的订单信息。
    public Object getSignedOrder(String applyUser, String toUser,
            String payGroup, String refNo, String bizType, String bizNote,
            Long transAmount, String backUrl);

    public void doCallbackAPP(String result);

}
