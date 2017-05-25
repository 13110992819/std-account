/**
 * @Title XN802400.java 
 * @Package com.std.account.api.impl 
 * @Description 
 * @author leo(haiqing)  
 * @date 2017年5月24日 下午9:45:00 
 * @version V1.0   
 */
package com.std.account.api.impl;

import com.std.account.ao.IExchangeCurrencyAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802400Req;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/** 
 * @author: haiqingzheng 
 * @since: 2017年5月24日 下午9:45:00 
 * @history:
 */
public class XN802400 extends AProcessor {
    private IExchangeCurrencyAO exchangeCurrencyAO = SpringContextHolder
        .getBean(IExchangeCurrencyAO.class);

    private XN802400Req req = null;

    /** 
     * @see com.std.account.api.IProcessor#doBusiness()
     */
    @Override
    public Object doBusiness() throws BizException {
        Long amount = StringValidater.toLong(req.getAmount());
        return exchangeCurrencyAO.payExchange(req.getFromUserId(),
            req.getToUserId(), amount, req.getCurrency(), req.getPayType());
    }

    /** 
     * @see com.std.account.api.IProcessor#doCheck(java.lang.String)
     */
    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802400Req.class);
        StringValidater.validateBlank(req.getFromUserId(), req.getToUserId(),
            req.getAmount(), req.getCurrency(), req.getPayType());
    }

}
