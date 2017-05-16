/**
 * @Title IWeChatAO.java 
 * @Package com.std.account.ao.impl 
 * @Description 
 * @author haiqingzheng  
 * @date 2016年12月23日 上午11:23:39 
 * @version V1.0   
 */
package com.std.account.ao;

import com.std.account.domain.CallbackResult;
import com.std.account.dto.res.XN002500Res;
import com.std.account.dto.res.XN002501Res;

/** 
 * @author: haiqingzheng 
 * @since: 2016年12月23日 上午11:23:39 
 * @history:
 */
public interface IWeChatAO {

    public XN002500Res getPrepayIdApp(String applyUser, String toUser,
            String payGroup, String refNo, String bizType, String bizNote,
            Long transAmount, String backUrl);

    public XN002501Res getPrepayIdH5(String applyUser, String openId,
            String toUser, String payGroup, String refNo, String bizType,
            String bizNote, Long transAmount, String backUrl);

    public String getPrepayIdNative(String applyUser, String toUser,
            String payGroup, String refNo, String bizType, String bizNote,
            Long transAmount, String backUrl);

    public void doCallbackAPP(String result);

    public void doCallbackH5(String result);

    public void doCallbackNative(String result);

    public String getAccessToken(String appId, String appSecret);

    public void doBizCallback(CallbackResult callbackResult);

}
