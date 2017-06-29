package com.std.account.api.impl;

import com.std.account.ao.IExchangeCurrencyAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802418Req;
import com.std.account.dto.res.BooleanRes;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * @author: myb858 
 * @since: 2017年5月11日 下午2:42:31 
 * @history:
 */

public class XN802418 extends AProcessor {
    private IExchangeCurrencyAO exchangeCurrencyAO = SpringContextHolder
        .getBean(IExchangeCurrencyAO.class);

    private XN802418Req req = null;

    /** 
     * @see com.xnjr.mall.api.IProcessor#doBusiness()
     */
    @Override
    public Object doBusiness() throws BizException {
        exchangeCurrencyAO.updateGdStatus(req.getCode());
        return new BooleanRes(true);
    }

    /** 
     * @see com.xnjr.mall.api.IProcessor#doCheck(java.lang.String)
     */
    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802418Req.class);
        StringValidater.validateBlank(req.getCode());
    }

}
