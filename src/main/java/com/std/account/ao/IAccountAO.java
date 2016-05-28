/**
 * @Title IAccountAO.java 
 * @Package com.ibis.account.ao 
 * @Description 
 * @author miyb  
 * @date 2015-3-17 下午6:21:02 
 * @version V1.0   
 */
package com.std.account.ao;

import com.std.account.annotation.ServiceModule;
import com.std.account.bo.base.Paginable;
import com.std.account.domain.Account;
import com.std.account.enums.ECurrency;

/** 
 * @author: miyb 
 * @since: 2015-3-17 下午6:21:02 
 * @history:
 */
@ServiceModule
public interface IAccountAO {
    String DEFAULT_ORDER_COLUMN = "account_number";

    /** 
     * 分配账户
     * @param userId
     * @param realName
     * @param currency
     * @return 
     * @create: 2016年5月26日 上午3:23:56 myb858
     * @history:
     */
    public String distributeAccount(String userId, String realName,
            ECurrency currency);

    /**
     * 分页查询账户
     * @param start
     * @param limit
     * @param condition
     * @return 
     * @create: 2015年6月26日 下午4:04:11 myb858
     * @history:
     */
    public Paginable<Account> queryAccountPage(int start, int limit,
            Account condition);

    /** 
     * 根据accountNumber查询账户
     * @param accountNumber
     * @return 
     * @create: 2015-3-17 下午6:28:56 miyb
     * @history: 
     */
    public Account getAccount(String accountNumber);

}
