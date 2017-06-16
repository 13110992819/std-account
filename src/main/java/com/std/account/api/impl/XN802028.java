package com.std.account.api.impl;

import org.apache.commons.collections.CollectionUtils;

import com.std.account.ao.ISYSConfigAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802028Req;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 根据key列表获取value值列表
 * @author: xieyj 
 * @since: 2017年6月16日 上午11:01:47 
 * @history:
 */
public class XN802028 extends AProcessor {
    private ISYSConfigAO sysConfigAO = SpringContextHolder
        .getBean(ISYSConfigAO.class);

    private XN802028Req req = null;

    /** 
     * @see com.xnjr.mall.api.IProcessor#doBusiness()
     */
    @Override
    public Object doBusiness() throws BizException {
        return sysConfigAO.getSYSConfig(req.getKeyList(), req.getCompanyCode(),
            req.getSystemCode());
    }

    /** 
     * @see com.xnjr.mall.api.IProcessor#doCheck(java.lang.String)
     */
    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802028Req.class);
        StringValidater
            .validateBlank(req.getCompanyCode(), req.getSystemCode());
        if (CollectionUtils.isEmpty(req.getKeyList())) {
            throw new ParaException("xn0000", "key列表不能为空");
        }
    }

}
