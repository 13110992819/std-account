/**
 * @Title IAJourBO.java 
 * @Package com.ibis.account.bo 
 * @Description 
 * @author miyb  
 * @date 2015-3-15 下午3:19:46 
 * @version V1.0   
 */
package com.std.account.bo;

import com.std.account.bo.base.IPaginableBO;
import com.std.account.domain.AccountJour;
import com.std.account.enums.EBoolean;

/** 
 * @author: miyb 
 * @since: 2015-3-15 下午3:19:46 
 * @history:
 */
public interface IAJourBO extends IPaginableBO<AccountJour> {
    /**
     * 获取单条账户流水记录
     * @param ajNo
     * @return 
     * @create: 2015-5-5 上午11:21:13 miyb
     * @history:
     */
    public AccountJour getAccountJour(Long ajNo);

    /**
     * 对账结果录入
     * @param aJNo
     * @param checkUser
     * @param checkResult
     * @return 
     * @create: 2016年1月15日 下午2:44:53 myb858
     * @history:
     */
    public void doCheckAccount(Long aJNo, String checkUser, EBoolean checkResult);

    /**
     * 单独新增流水
     * @param accountNumber
     * @param preAmount
     * @param amount
     * @param bizType
     * @param refNo
     * @param remark 
     * @create: 2016年7月6日 下午8:32:54 xieyj
     * @history:
     */
    public void addJour(String accountNumber, Long preAmount, Long amount,
            String bizType, String refNo, String remark);

}
