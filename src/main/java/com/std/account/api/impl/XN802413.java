package com.std.account.api.impl;

import com.std.account.ao.IExchangeCurrencyAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802413Req;
import com.std.account.dto.res.BooleanRes;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 不同或者同种币种间同个用户或者不同用户的资金划转   
 * 申请虚拟币转化，不需要审核，目前支持转化的有：菜狗币转积分（菜狗），人民币余额转菜狗币（菜狗）
 * @author: myb858 
 * @since: 2017年5月11日 下午2:42:31 
 * @history:
 */

public class XN802413 extends AProcessor {
    private IExchangeCurrencyAO exchangeCurrencyAO = SpringContextHolder
        .getBean(IExchangeCurrencyAO.class);

    private XN802413Req req = null;

    /** 
     * @see com.xnjr.mall.api.IProcessor#doBusiness()
     */
    @Override
    public Object doBusiness() throws BizException {
        Long amount = StringValidater.toLong(req.getAmount());
        exchangeCurrencyAO.doTransfer(req.getFromUserId(),
            req.getFromCurrency(), req.getToUserId(), req.getToCurrency(),
            amount);
        return new BooleanRes(true);
    }

    /** 
     * @see com.xnjr.mall.api.IProcessor#doCheck(java.lang.String)
     */
    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802413Req.class);
        StringValidater.validateBlank(req.getFromUserId(),
            req.getFromCurrency(), req.getToUserId(), req.getToCurrency(),
            req.getAmount());
    }

}
