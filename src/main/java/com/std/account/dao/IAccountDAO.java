/**
 * @Title IAccountDAO.java 
 * @Package com.ibis.account.dao 
 * @Description 
 * @author miyb  
 * @date 2015-2-14 下午2:07:37 
 * @version V1.0   
 */
package com.std.account.dao;

import com.std.account.dao.base.IBaseDAO;
import com.std.account.domain.Account;

/** 
 * @author: miyb 
 * @since: 2015-2-14 下午2:07:37 
 * @history:
 */
public interface IAccountDAO extends IBaseDAO<Account> {
    String NAMESPACE = IAccountDAO.class.getName().concat(".");

    /**
     * 修改账户余额,并统计充值，取现金额
     * @param data
     * @return 
     * @create: 2017年5月16日 下午2:23:19 xieyj
     * @history:
     */
    public int updateAmount(Account data);

    /**
     * 更新户名
     * @param data
     * @return 
     * @create: 2016年12月26日 下午5:23:13 xieyj
     * @history:
     */
    public int updateRealName(Account data);

    /** 
     * 修改账户状态
     * @param data
     * @return 
     * @create: 2015-2-23 下午4:28:41 miyb
     * @history: 
     */
    public int updateStatus(Account data);

    public int frozenAmount(Account data);

    public int unfrozenAmount(Account data);

    public int cutFrozenAmount(Account data);
}
