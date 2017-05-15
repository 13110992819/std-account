/**
 * @Title IJourAO.java 
 * @Package com.std.account.ao 
 * @Description 
 * @author xieyj  
 * @date 2016年12月23日 下午9:05:07 
 * @version V1.0   
 */
package com.std.account.ao;

import java.util.List;

import com.std.account.annotation.ServiceModule;
import com.std.account.bo.base.Paginable;
import com.std.account.domain.Jour;

/** 
 * @author: xieyj 
 * @since: 2016年12月23日 下午9:05:07 
 * @history:
 */
@ServiceModule
public interface IJourAO {
    String DEFAULT_ORDER_COLUMN = "code";

    // 未调用
    public Object doRechargeOnline(String userId, String payType, Long amount);

    // 未调用
    public void doChangeAmountList(List<String> accountNumberList,
            String bankcardNumber, Long transAmount, String bizType,
            String bizNote, String systemCode);

    // 未调用
    public void doOfflineWith(String accountNumber, String bankcardNumber,
            Long transAmount, String systemCode, String tradePwd);

    /**
     * 回调支付
     * @param code
     * @param rollbackResult
     * @param rollbackUser
     * @param rollbackNote
     * @param systemCode 
     * @create: 2016年12月24日 上午8:21:37 xieyj
     * @history:
     */
    public void doCallBackOffChange(String code, String rollbackResult,
            String rollbackUser, String rollbackNote, String systemCode);

    // 未调用
    public void doCallBackChangeList(List<String> codeList,
            String rollbackResult, String rollbackUser, String rollbackNote,
            String systemCode);

    /**
     * 人工调账
     * @param code
     * @param checkAmount
     * @param checkUser
     * @param checkNote
     * @param systemCode 
     * @create: 2016年12月25日 下午3:58:53 xieyj
     * @history:
     */
    public void checkJour(String code, Long checkAmount, String checkUser,
            String checkNote, String systemCode);

    public Paginable<Jour> queryJourPage(int start, int limit, Jour condition);

    public List<Jour> queryJourList(Jour condition);

    public Jour getJour(String code, String systemCode);
}
