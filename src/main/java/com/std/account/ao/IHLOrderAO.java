/**
 * @Title IHLOrderAO.java 
 * @Package com.ibis.account.ao 
 * @Description 
 * @author miyb  
 * @date 2015-3-17 下午7:24:27 
 * @version V1.0   
 */
package com.std.account.ao;

import com.std.account.annotation.ServiceModule;
import com.std.account.bo.base.Paginable;
import com.std.account.domain.HLOrder;

/** 
 * @author: miyb 
 * @since: 2015-3-17 下午7:24:27 
 * @history:
 */
@ServiceModule
public interface IHLOrderAO {
    String DEFAULT_ORDER_COLUMN = "code";

    /** 
     * @param start
     * @param limit
     * @param condition
     * @return 
     * @create: 2015-5-10 上午10:44:02 miyb
     * @history: 
     */
    public Paginable<HLOrder> queryHLOrderPage(int start, int limit,
            HLOrder condition);

    /**
     * 红冲蓝补申请
     * @param accountNumber
     * @param direction
     * @param amount
     * @param applyUser
     * @param applyNote
     * @return 
     * @create: 2016年5月26日 下午4:19:10 myb858
     * @history:
     */

    public String doBalance(String accountNumber, String direction,
            Long amount, String applyUser, String applyNote);

    /**
     * 红冲蓝补审批
     * @param hlNo
     * @param approveUser
     * @param approveResult
     * @param approveNote 
     * @create: 2016年1月16日 下午7:29:28 myb858
     * @history:
     */
    public void doApprove(String hlNo, String approveUser,
            String approveResult, String approveNote);

}
