package com.std.account.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.std.account.bo.IAccountBO;
import com.std.account.bo.IExchangeCurrencyBO;
import com.std.account.bo.IJourBO;
import com.std.account.bo.base.PaginableBOImpl;
import com.std.account.core.AccountUtil;
import com.std.account.core.OrderNoGenerater;
import com.std.account.dao.IAccountDAO;
import com.std.account.domain.Account;
import com.std.account.domain.HLOrder;
import com.std.account.enums.EAccountStatus;
import com.std.account.enums.EAccountType;
import com.std.account.enums.EChannelType;
import com.std.account.enums.ECurrency;
import com.std.account.enums.EGeneratePrefix;
import com.std.account.enums.EJourBizType;
import com.std.account.enums.ESysUser;
import com.std.account.exception.BizException;
import com.std.account.util.AmountUtil;

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

    @Autowired
    private IExchangeCurrencyBO exchangeCurrencyBO;

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
            data.setAddAmount(0L);
            data.setInAmount(0L);
            data.setOutAmount(0L);
            data.setCreateDatetime(new Date());
            accountDAO.insert(data);
        }
        return accountNumber;
    }

    @Override
    public void changeAmount(String accountNumber, EChannelType channelType,
            String channelOrder, String payGroup, String refNo,
            EJourBizType bizType, String bizNote, Long transAmount) {
        Account dbAccount = this.getAccount(accountNumber);
        Long nowAmount = dbAccount.getAmount() + transAmount;
        // 特定账户余额可为负
        if (!dbAccount.getUserId().contains(ESysUser.SYS_USER.getCode())
                && nowAmount < 0) {
            throw new BizException("xn000000", "账户余额不足");
        }
        // 记录流水
        String lastOrder = jourBO.addJour(dbAccount, channelType, channelOrder,
            payGroup, refNo, bizType, bizNote, transAmount);
        // 更改余额
        Account data = new Account();
        data.setAccountNumber(accountNumber);
        data.setAmount(nowAmount);
        data.setMd5(AccountUtil.md5(dbAccount.getMd5(), dbAccount.getAmount(),
            nowAmount));
        // 修改累计增加金额
        data.setAddAmount(dbAccount.getAddAmount());
        if (transAmount > 0) {
            data.setAddAmount(dbAccount.getAddAmount() + transAmount);
        }
        data.setLastOrder(lastOrder);
        accountDAO.updateAmount(data);
    }

    @Override
    public void changeAmountNotJour(String accountNumber, Long transAmount,
            String lastOrder) {
        Account dbAccount = this.getAccount(accountNumber);
        Long nowAmount = dbAccount.getAmount() + transAmount;
        if (!dbAccount.getUserId().contains(ESysUser.SYS_USER.getCode())
                && nowAmount < 0) {
            throw new BizException("xn000000", "账户余额不足");
        }
        // 更改余额
        Account data = new Account();
        data.setAccountNumber(accountNumber);
        data.setAmount(nowAmount);
        data.setMd5(AccountUtil.md5(dbAccount.getMd5(), dbAccount.getAmount(),
            nowAmount));
        // 修改累计增加金额
        data.setAddAmount(dbAccount.getAddAmount());
        if (transAmount > 0) {
            data.setAddAmount(dbAccount.getAddAmount() + transAmount);
        }
        data.setLastOrder(lastOrder);
        accountDAO.updateAmount(data);
    }

    @Override
    public void changeAmountForHL(HLOrder order) {
        Account dbAccount = this.getAccount(order.getAccountNumber());
        Long nowAmount = dbAccount.getAmount() + order.getAmount();
        // 记录流水
        String lastOrder = jourBO.addJourForHL(dbAccount, order);
        // 更改余额
        Account data = new Account();
        data.setAccountNumber(dbAccount.getAccountNumber());
        data.setAmount(nowAmount);
        data.setMd5(AccountUtil.md5(dbAccount.getMd5(), dbAccount.getAmount(),
            nowAmount));
        data.setLastOrder(lastOrder);
        accountDAO.updateAmount(data);

    }

    @Override
    public void frozenAmount(Account dbAccount, Long freezeAmount,
            String withdrawCode) {
        if (freezeAmount <= 0) {
            throw new BizException("xn000000", "冻结金额需大于0");
        }
        Long nowAmount = dbAccount.getAmount() - freezeAmount;
        if (nowAmount < 0) {
            throw new BizException("xn000000", "账户余额不足");
        }
        // 记录流水
        String lastOrder = jourBO.addJour(dbAccount, EChannelType.Offline,
            withdrawCode, EJourBizType.AJ_QX, "线下取现冻结金额", -freezeAmount);
        Long nowFrozenAmount = dbAccount.getFrozenAmount() + freezeAmount;
        Account data = new Account();
        data.setAccountNumber(dbAccount.getAccountNumber());
        data.setAmount(nowAmount);
        data.setFrozenAmount(nowFrozenAmount);
        data.setMd5(AccountUtil.md5(dbAccount.getMd5(), dbAccount.getAmount(),
            nowAmount));
        data.setLastOrder(lastOrder);
        accountDAO.frozenAmount(data);
    }

    @Override
    public void unfrozenAmount(Account dbAccount, Long freezeAmount,
            String withdrawCode) {
        if (freezeAmount <= 0) {
            throw new BizException("xn000000", "解冻金额需大于0");
        }
        Long nowFrozenAmount = dbAccount.getFrozenAmount() - freezeAmount;
        if (nowFrozenAmount < 0) {
            throw new BizException("xn000000", "本次解冻会使账户冻结金额小于0");
        }

        // 记录流水
        String lastOrder = jourBO.addJour(dbAccount, EChannelType.Offline,
            withdrawCode, EJourBizType.AJ_QX, "线下取现解冻金额", freezeAmount);
        Account data = new Account();
        data.setAccountNumber(dbAccount.getAccountNumber());
        data.setAmount(dbAccount.getAmount() + freezeAmount);
        data.setFrozenAmount(nowFrozenAmount);
        data.setMd5(AccountUtil.md5(dbAccount.getMd5(), dbAccount.getAmount(),
            dbAccount.getAmount() + freezeAmount));
        data.setLastOrder(lastOrder);
        accountDAO.unfrozenAmount(data);
    }

    @Override
    public void cutFrozenAmount(Account dbAccount, Long freezeAmount) {
        if (freezeAmount <= 0) {
            throw new BizException("xn000000", "解冻金额需大于0");
        }
        Long nowFrozenAmount = dbAccount.getFrozenAmount() - freezeAmount;
        if (nowFrozenAmount < 0) {
            throw new BizException("xn000000", "本次扣减会使账户冻结金额小于0");
        }
        Account data = new Account();
        data.setAccountNumber(dbAccount.getAccountNumber());
        data.setFrozenAmount(nowFrozenAmount);
        accountDAO.cutFrozenAmount(data);
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
    public Account getAccount(String accountNumber) {
        Account data = null;
        if (StringUtils.isNotBlank(accountNumber)) {
            Account condition = new Account();
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
    public Account getAccountByUser(String userId, String currency) {
        Account data = null;
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(currency)) {
            Account condition = new Account();
            condition.setUserId(userId);
            condition.setCurrency(currency);
            data = accountDAO.select(condition);
            if (data == null) {
                throw new BizException("xn702502", "用户[" + userId + ";"
                        + currency + "]无此类型账户");
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

    /** 
     * @see com.std.account.bo.IAccountBO#getSysAccount(java.lang.String, java.lang.String)
     */
    @Override
    public Account getSysAccount(String sysUser, String currency) {
        Account condition = new Account();
        condition.setSysUser(sysUser);
        condition.setCurrency(currency);
        return accountDAO.select(condition);
    }

    @Override
    public void transAmountCZB(String fromUserId, String fromCurrency,
            String toUserId, String toCurrency, Long transAmount,
            EJourBizType bizType, String fromBizNote, String toBizNote) {
        Account fromAccount = this.getAccountByUser(fromUserId, fromCurrency);
        Account toAccount = this.getAccountByUser(toUserId, toCurrency);
        transAmountCZB(fromAccount, toAccount, transAmount, bizType,
            fromBizNote, toBizNote);
    }

    @Override
    public void transAmountCZB(String fromAccountNumber,
            String toAccountNumber, Long transAmount, EJourBizType bizType,
            String fromBizNote, String toBizNote) {
        Account fromAccount = this.getAccount(fromAccountNumber);
        Account toAccount = this.getAccount(toAccountNumber);
        transAmountCZB(fromAccount, toAccount, transAmount, bizType,
            fromBizNote, toBizNote);
    }

    private void transAmountCZB(Account fromAccount, Account toAccount,
            Long transAmount, EJourBizType bizType, String fromBizNote,
            String toBizNote) {
        String fromAccountNumber = fromAccount.getAccountNumber();
        String toAccountNumber = toAccount.getAccountNumber();
        if (fromAccountNumber.equals(toAccountNumber)) {
            new BizException("XN0000", "来去双方账号一致，无需内部划转");
        }
        Double rate = exchangeCurrencyBO.getExchangeRate(
            fromAccount.getCurrency(), toAccount.getCurrency());
        this.changeAmount(fromAccountNumber, EChannelType.NBZ, toAccountNumber,
            -transAmount, bizType, fromBizNote);
        this.changeAmount(toAccountNumber, EChannelType.NBZ, fromAccountNumber,
            AmountUtil.mul(transAmount, rate), bizType, toBizNote);

    }

    @Override
    public String getSysAccountNumber(String systemCode, ECurrency currency) {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * @see com.std.account.bo.IAccountBO#refreshOutAmount(java.lang.String, java.lang.Long)
     */
    @Override
    public void refreshOutAmount(Account account, Long transAmount) {
        Long outAmount = account.getOutAmount() + transAmount;
        account.setOutAmount(outAmount);
        accountDAO.updateOutAmount(account);
    }

}
