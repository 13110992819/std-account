package com.std.account.api.impl;

import com.std.account.ao.IJourHistoryAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802532Req;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 历史流水详情查询
 * @author: xieyj 
 * @since: 2016年12月24日 上午7:59:19 
 * @history:
 */
public class XN802532 extends AProcessor {
    private IJourHistoryAO jourHistoryAO = SpringContextHolder
        .getBean(IJourHistoryAO.class);

    private XN802532Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        return jourHistoryAO.getJour(req.getCode(), req.getSystemCode());
    }

    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802532Req.class);
        StringValidater.validateBlank(req.getCode(), req.getSystemCode());
    }
}
