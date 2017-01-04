package com.std.account.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.std.account.bo.IAccountBO;
import com.std.account.bo.IJourBO;
import com.std.account.bo.base.PaginableBOImpl;
import com.std.account.core.AccountUtil;
import com.std.account.core.OrderNoGenerater;
import com.std.account.dao.IAccountDAO;
import com.std.account.domain.Account;
import com.std.account.enums.EAccountStatus;
import com.std.account.enums.EAccountType;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EGeneratePrefix;
import com.std.account.exception.BizException;

/**
 * @author: xieyj 
 * @since: 2016年12月23日 下午5:24:53 
 * @history:
 */
@Component
public class AccountBOImpl extends PaginableBOImpl<Account> implements
        IAccountBO {
    @Autowired
    private IAccountDAO accountDAO;

    @Autowired
    private IJourBO jourBO;

    @Override
    public String distributeAccount(String userId, String realName,
            EAccountType accountType, String currency, String systemCode) {
        String accountNumber = null;
        if (StringUtils.isNotBlank(systemCode)
                && StringUtils.isNotBlank(userId)) {
            accountNumber = OrderNoGenerater.generate(EGeneratePrefix.Account
                .getCode());
            Account data = new Account();
            data.setAccountNumber(accountNumber);
            data.setUserId(userId);
            data.setRealName(realName);
            data.setType(accountType.getCode());
            data.setCurrency(currency);
            data.setSystemCode(systemCode);
            data.setStatus(EAccountStatus.NORMAL.getCode());
            data.setAmount(0L);
            data.setFrozenAmount(0L);
            data.setMd5(AccountUtil.md5(data.getAmount()));
            data.setCreateDatetime(new Date());
            accountDAO.insert(data);
        }
        return accountNumber;
    }

    @Override
    public void transAmount(String systemCode, String accountNumber,
            EChannelType channelType, String channelOrder, Long transAmount,
            String bizType, String bizNote) {
        Account dbAccount = this.getAccount(systemCode, accountNumber);
        Long nowAmount = dbAccount.getAmount() + transAmount;
        if (nowAmount < 0) {
            throw new BizException("xn000000", "账户余额不足");
        }
        // 记录流水
        String lastOrder = jourBO.addChangedJour(systemCode, accountNumber,
            channelType, channelOrder, bizType, bizNote, dbAccount.getAmount(),
            transAmount);
        // 更改余额
        Account data = new Account();
        data.setAccountNumber(accountNumber);
        data.setAmount(nowAmount);
        data.setMd5(AccountUtil.md5(dbAccount.getMd5(), dbAccount.getAmount(),
            nowAmount));
        data.setLastOrder(lastOrder);
        accountDAO.updateAmount(data);
    }

    @Override
    public void transAmountNotJour(String systemCode, String accountNumber,
            Long transAmount, String lastOrder) {
        Account dbAccount = this.getAccount(systemCode, accountNumber);
        Long nowAmount = dbAccount.getAmount() + transAmount;
        if (nowAmount < 0) {
            throw new BizException("xn000000", "账户余额不足");
        }
        // 更改余额
        Account data = new Account();
        data.setAccountNumber(accountNumber);
        data.setAmount(nowAmount);
        data.setMd5(AccountUtil.md5(dbAccount.getMd5(), dbAccount.getAmount(),
            nowAmount));
        data.setLastOrder(lastOrder);
        accountDAO.updateAmount(data);
    }

    @Override
    public void frozenAmount(String systemCode, String accountNumber,
            Long freezeAmount, String lastOrder) {
        if (freezeAmount <= 0) {
            throw new BizException("xn000000", "冻结金额需大于0");
        }
        Account dbAccount = this.getAccount(systemCode, accountNumber);
        Long nowAmount = dbAccount.getAmount() - freezeAmount;
        if (nowAmount < 0) {
            throw new BizException("xn000000", "账户余额不足");
        }
        Long nowFrozenAmount = dbAccount.getFrozenAmount() + freezeAmount;
        Account data = new Account();
        data.setAccountNumber(accountNumber);
        data.setAmount(nowAmount);
        data.setFrozenAmount(nowFrozenAmount);
        data.setMd5(AccountUtil.md5(dbAccount.getMd5(), dbAccount.getAmount(),
            nowAmount));
        data.setLastOrder(lastOrder);
        accountDAO.updateFrozenAmount(data);
    }

    @Override
    public void unfrozenAmount(String systemCode, String unfrozenResult,
            String accountNumber, Long unfreezeAmount, String lastOrder) {
        if (unfreezeAmount <= 0) {
            throw new BizException("xn000000", "解冻金额需大于0");
        }
        Account dbAccount = this.getAccount(systemCode, accountNumber);
        // 审核通过，扣除冻结金额，审核不通过冻结资金原路返回
        Long nowAmount = dbAccount.getAmount();
        if (EBoolean.NO.getCode().equals(unfrozenResult)) {
            nowAmount = nowAmount + unfreezeAmount;
        }
        Long nowFrozenAmount = dbAccount.getFrozenAmount() - unfreezeAmount;
        if (nowFrozenAmount < 0) {
            throw new BizException("xn000000", "本次解冻会使账户冻结金额小于0");
        }
        Account data = new Account();
        data.setAccountNumber(accountNumber);
        data.setAmount(nowAmount);
        data.setFrozenAmount(nowFrozenAmount);
        data.setMd5(AccountUtil.md5(dbAccount.getMd5(), dbAccount.getAmount(),
            nowAmount));
        data.setLastOrder(lastOrder);
        accountDAO.updateFrozenAmount(data);
    }

    @Override
    public void refreshStatus(String systemCode, String accountNumber,
            EAccountStatus status) {
        if (StringUtils.isNotBlank(accountNumber)) {
            Account data = new Account();
            data.setAccountNumber(accountNumber);
            data.setStatus(status.getCode());
            accountDAO.updateStatus(data);
        }
    }

    @Override
    public Account getAccount(String systemCode, String accountNumber) {
        Account data = null;
        if (StringUtils.isNotBlank(accountNumber)) {
            Account condition = new Account();
            condition.setSystemCode(systemCode);
            condition.setAccountNumber(accountNumber);
            data = accountDAO.select(condition);
            if (data == null) {
                throw new BizException("xn702502", "无对应账户，请检查账号正确性");
            }
        }
        return data;
    }

    @Override
    public List<Account> queryAccountList(Account data) {
        return accountDAO.selectList(data);
    }

    /** 
     * @see com.std.account.bo.IAccountBO#getAccountByUser(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Account getAccountByUser(String systemCode, String userId,
            String currency) {
        Account data = null;
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(currency)) {
            Account condition = new Account();
            condition.setSystemCode(systemCode);
            condition.setUserId(userId);
            condition.setCurrency(currency);
            data = accountDAO.select(condition);
            if (data == null) {
                throw new BizException("xn702502", "该用户无此类型账户");
            }
        }
        return data;
    }

    /** 
     * @see com.std.account.bo.IAccountBO#refreshAccountName(java.lang.String, java.lang.String)
     */
    @Override
    public void refreshAccountName(String userId, String realName) {
        Account data = new Account();
        data.setUserId(userId);
        data.setRealName(realName);
        accountDAO.updateRealName(data);
    }
}
