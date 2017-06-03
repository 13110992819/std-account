package com.std.account.api.impl;

import com.std.account.ao.IJourAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802901Req;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 单个账户某段时间统计金额，例如今日收入，今日取现
 * @author: xieyj 
 * @since: 2017年5月31日 下午7:49:44 
 * @history:
 */
public class XN802901 extends AProcessor {
    private IJourAO jourAO = SpringContextHolder.getBean(IJourAO.class);

    private XN802901Req req = null;

    /** 
     * @see com.std.account.api.IProcessor#doBusiness()
     */
    @Override
    public Object doBusiness() throws BizException {
        return jourAO.getTotalAmountByDate(req.getAccountNumber(),
            req.getDateStart(), req.getDateEnd());
    }

    /** 
     * @see com.std.account.api.IProcessor#doCheck(java.lang.String)
     */
    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802901Req.class);
        StringValidater.validateBlank(req.getAccountNumber(),
            req.getDateStart(), req.getDateEnd());
    }

}
