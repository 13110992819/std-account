package com.std.account.api.impl;

import com.std.account.ao.IBankcardAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802010Req;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 绑定银行卡
 * @author: asus 
 * @since: 2016年12月22日 下午5:04:53 
 * @history:
 */
public class XN802010 extends AProcessor {
    private IBankcardAO bankCardAO = SpringContextHolder
        .getBean(IBankcardAO.class);

    private XN802010Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        return bankCardAO.addBankcard(req);
    }

    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802010Req.class);
        StringValidater.validateBlank(req.getSystemCode(),
            req.getBankcardNumber(), req.getBankCode(), req.getBankName(),
            req.getUserId(), req.getRealName(), req.getType());
    }
}
