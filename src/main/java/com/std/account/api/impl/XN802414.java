package com.std.account.api.impl;

import com.std.account.ao.IExchangeCurrencyAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802414Req;
import com.std.account.dto.res.BooleanRes;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * C端用户互转资金,适用正汇
 * @author: xieyj 
 * @since: 2017年6月5日 下午1:22:48 
 * @history:
 */
public class XN802414 extends AProcessor {
    private IExchangeCurrencyAO exchangeCurrencyAO = SpringContextHolder
        .getBean(IExchangeCurrencyAO.class);

    private XN802414Req req = null;

    /** 
     * @see com.xnjr.mall.api.IProcessor#doBusiness()
     */
    @Override
    public Object doBusiness() throws BizException {
        Long amount = StringValidater.toLong(req.getAmount());
        exchangeCurrencyAO.doTransferC2CByZhFR(req.getFromUserId(),
            req.getToMobile(), amount, req.getTradePwd());
        return new BooleanRes(true);
    }

    /** 
     * @see com.xnjr.mall.api.IProcessor#doCheck(java.lang.String)
     */
    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802414Req.class);
        StringValidater.validateBlank(req.getFromUserId(), req.getToMobile(),
            req.getAmount(), req.getTradePwd());
    }
}
