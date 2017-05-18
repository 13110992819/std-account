package com.std.account.api.impl;

import com.std.account.ao.IChargeAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802700Req;
import com.std.account.dto.res.PKCodeRes;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 线下充值申请
 * @author: myb858 
 * @since: 2017年5月3日 上午9:23:51 
 * @history:
 */
public class XN802700 extends AProcessor {
    private IChargeAO chargeAO = SpringContextHolder.getBean(IChargeAO.class);

    private XN802700Req req = null;

    /** 
    * @see com.xnjr.base.api.IProcessor#doBusiness()
    */
    @Override
    public synchronized Object doBusiness() throws BizException {
        Long amount = StringValidater.toLong(req.getAmount());
        String code = chargeAO.applyOrder(req.getAccountNumber(),
            req.getBizType(), amount, req.getPayCardInfo(), req.getPayCardNo(),
            req.getApplyUser(), req.getApplyNote());
        return new PKCodeRes(code);
    }

    /** 
    * @see com.xnjr.base.api.IProcessor#doCheck(java.lang.String)
    */
    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802700Req.class);
        StringValidater.validateBlank(req.getAccountNumber(),
            req.getApplyUser());
        StringValidater.validateAmount(req.getAmount());
    }
}
