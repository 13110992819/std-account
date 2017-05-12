package com.std.account.ao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.std.account.ao.IAccountAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.ICompanyChannelBO;
import com.std.account.bo.IJourBO;
import com.std.account.bo.IUserBO;
import com.std.account.bo.base.Paginable;
import com.std.account.domain.Account;
import com.std.account.enums.EAccountType;
import com.std.account.enums.EJourBizType;
import com.std.account.exception.BizException;

@Service
public class AccountAOImpl implements IAccountAO {
    @Autowired
    private IAccountBO accountBO;

    @Autowired
    private ICompanyChannelBO companyChannelBO;

    @Autowired
    private IJourBO jourBO;

    @Autowired
    private IUserBO userBO;

    /** 
     * @see com.std.account.ao.IAccountAO#distributeAccount(java.lang.String, java.lang.String, com.std.account.enums.EAccountType, java.util.List, java.lang.String)
     */
    @Override
    @Transactional
    public void distributeAccount(String userId, String realName,
            String accountType, List<String> currencyList, String systemCode) {
        if (CollectionUtils.isNotEmpty(currencyList)) {
            Map<String, EAccountType> map = EAccountType
                .getAccountTypeResultMap();
            EAccountType eAccountType = map.get(accountType);
            if (null == eAccountType) {
                new BizException("XN0000", "账户类型不存在");
            }
            for (String currency : currencyList) {
                accountBO.distributeAccount(userId, realName, eAccountType,
                    currency, systemCode);
            }
        }
    }

    @Override
    public void editAccountName(String userId, String realName,
            String systemCode) {
        // 验证用户名和系统编号
        Account data = new Account();
        data.setUserId(userId);
        data.setRealName(realName);
        List<Account> accountList = accountBO.queryAccountList(data);
        if (CollectionUtils.isEmpty(accountList)) {
            new BizException("XN0000", "该用户无对应账号");
        }
        accountBO.refreshAccountName(userId, realName);
    }

    @Override
    @Transactional
    public void transAmountCZB(String fromUserId, String toUserId,
            String currency, Long transAmount, String bizType,
            String fromBizNote, String toBizNote) {
        EJourBizType a = EJourBizType.getBizType(bizType);
        accountBO.transAmountCZB(fromUserId, currency, toUserId, currency,
            transAmount, a, fromBizNote, toBizNote);
    }

    @Override
    public Account getAccount(String accountNumber) {
        return accountBO.getAccount(accountNumber);
    }

    @Override
    public Paginable<Account> queryAccountPage(int start, int limit,
            Account condition) {
        return accountBO.getPaginable(start, limit, condition);
    }

    @Override
    public List<Account> queryAccountList(Account condition) {
        return accountBO.queryAccountList(condition);
    }

    @Override
    public List<Account> getAccountByUserId(String userId, String currency) {
        Account condition = new Account();
        condition.setUserId(userId);
        condition.setCurrency(currency);
        return accountBO.queryAccountList(condition);
    }

}
