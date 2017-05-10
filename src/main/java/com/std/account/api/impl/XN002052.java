package com.std.account.api.impl;

import com.std.account.ao.IAccountAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.domain.Account;
import com.std.account.dto.req.XN002052Req;
import com.std.account.dto.res.XN002052Res;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 获取账户信息（账户余额：账本的余额理论上应该保证绝对正确性，供客户放篡改用）
 * @author: myb858 
 * @since: 2016年11月5日 下午12:45:24 
 * @history:
 */
public class XN002052 extends AProcessor {
    private IAccountAO accountAO = SpringContextHolder
        .getBean(IAccountAO.class);

    private XN002052Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        Account account = accountAO.getAccount(req.getAccountNumber());
        XN002052Res res = new XN002052Res();
        res.setSystemCode(account.getSystemCode());
        res.setAccountName(account.getRealName());
        res.setAccountNumber(account.getAccountNumber());
        res.setStatus(account.getStatus());
        res.setCurrency(account.getCurrency());
        res.setAmount(account.getAmount());
        res.setFrozenAmount(account.getFrozenAmount());
        return res;
    }

    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN002052Req.class);
        StringValidater.validateBlank(req.getAccountNumber());
    }
}
