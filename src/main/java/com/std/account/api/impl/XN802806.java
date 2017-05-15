package com.std.account.api.impl;

import com.std.account.ao.IHLOrderAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802806Req;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 详情查询红冲蓝补订单
 * @author: xieyj 
 * @since: 2017年5月15日 下午4:07:42 
 * @history:
 */
public class XN802806 extends AProcessor {
    private IHLOrderAO hlOrderAO = SpringContextHolder
        .getBean(IHLOrderAO.class);

    private XN802806Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        return hlOrderAO.getHLOrder(req.getCode(), req.getSystemCode());
    }

    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802806Req.class);
        StringValidater.validateBlank(req.getCode(), req.getSystemCode());
    }
}
