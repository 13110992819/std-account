package com.std.account.api.impl;

import com.std.account.ao.IHLOrderAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.domain.HLOrder;
import com.std.account.dto.req.XN802807Req;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

public class XN802807 extends AProcessor {
    private IHLOrderAO hlOrderAO = SpringContextHolder
        .getBean(IHLOrderAO.class);

    private XN802807Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        HLOrder condition = new HLOrder();
        condition.setAccountNumber(req.getAccountNumber());
        condition.setAccountName(req.getAccountName());
        condition.setDirection(req.getDirection());
        condition.setStatus(req.getStatus());
        condition.setApplyUser(req.getApplyUser());
        condition.setApproveUser(req.getApproveUser());
        condition.setSystemCode(req.getSystemCode());

        return hlOrderAO.queryHLOrderList(condition);
    }

    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802807Req.class);
        StringValidater.validateBlank(req.getSystemCode());
    }

}
