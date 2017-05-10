package com.std.account.bo;

import java.util.List;

import com.std.account.bo.base.IPaginableBO;
import com.std.account.domain.Account;
import com.std.account.domain.HLOrder;
import com.std.account.enums.EAccountStatus;
import com.std.account.enums.EAccountType;
import com.std.account.enums.EChannelType;
import com.std.account.enums.ECurrency;
import com.std.account.enums.EJourBizType;

/**
 * @author: xieyj
 * @since: 2016年11月11日 上午11:23:06 
 * @history:
 */
public interface IAccountBO extends IPaginableBO<Account> {

    /**
     * 分配账户
     * @param userId
     * @param realName
     * @param accountType
     * @param currency
     * @param systemCode
     * @return 
     * @create: 2016年12月23日 下午12:35:22 xieyj
     * @history:
     */
    public String distributeAccount(String userId, String realName,
            EAccountType accountType, String currency, String systemCode);

    // 变更账户余额：流水落地
    public void changeAmount(String accountNumber, EChannelType channelType,
            String refNo, Long transAmount, EJourBizType bizType, String bizNote);

    // 仅变更账户余额：流水不落地
    public void changeAmountNotJour(String accountNumber, Long transAmount,
            String lastOrder);

    // 红冲蓝补导致的资金变动（落地流水不需要对账）
    public void changeAmountForHL(HLOrder order);

    /**
     * 更新户名
     * @param userId
     * @param realName 
     * @create: 2017年1月4日 上午11:34:18 xieyj
     * @history:
     */
    public void refreshAccountName(String userId, String realName);

    // 冻结金额（余额变动）
    public void frozenAmount(Account dbAccount, Long freezeAmount,
            String withdrawCode);

    // 解冻账户(冻结金额原路返回)
    public void unfrozenAmount(Account dbAccount, Long freezeAmount,
            String withdrawCode);

    // 扣减冻结金额
    public void cutFrozenAmount(Account dbAccount, Long amount);

    /**
     * 更新账户状态
     * @param systemCode
     * @param accountNumber
     * @param status 
     * @create: 2016年12月23日 下午5:27:04 xieyj
     * @history:
     */
    public void refreshStatus(String systemCode, String accountNumber,
            EAccountStatus status);

    /**
     * 获取账户
     * @param accountNumber
     * @return 
     * @create: 2016年12月23日 下午5:27:22 xieyj
     * @history:
     */
    public Account getAccount(String accountNumber);

    /**
     * 通过用户编号和币种获取币种
     * @param userId
     * @param currency
     * @return 
     * @create: 2016年12月28日 下午1:55:21 xieyj
     * @history:
     */
    public Account getAccountByUser(String userId, String currency);

    /**
     * 获取系统账户
     * @param sysUser
     * @param currency
     * @return 
     * @create: 2017年4月5日 下午9:19:34 xieyj
     * @history:
     */
    public Account getSysAccount(String sysUser, String currency);

    /**
     * 获取账户列表
     * @param data
     * @return 
     * @create: 2016年11月11日 上午10:52:08 xieyj
     * @history:
     */
    public List<Account> queryAccountList(Account data);

    // 内部转账
    public void transAmountCZB(String fromUserId, String fromCurrency,
            String toUserId, String toCurrency, Long transAmount,
            EJourBizType bizType, String fromBizNote, String toBizNote);

    // 内部转账
    public void transAmountCZB(String fromAccountNumber,
            String toAccountNumber, Long transAmount, EJourBizType bizType,
            String fromBizNote, String toBizNote);

    // 根据系统编号和币种获取对应的系统账户编号
    public String getSysAccountNumber(String systemCode, ECurrency currency);

}
