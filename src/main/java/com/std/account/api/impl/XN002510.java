/**
 * @Title XN802184.java 
 * @Package com.std.account.api.impl 
 * @Description 
 * @author haiqingzheng  
 * @date 2016年12月23日 上午9:23:43 
 * @version V1.0   
 */
package com.std.account.api.impl;

import com.std.account.ao.IAlipayAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN002510Req;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/** 
 * 支付宝APP支付请求接口，返回签名后的订单信息
 * @author: haiqingzheng 
 * @since: 2016年12月23日 上午9:23:43 
 * @history:
 */
public class XN002510 extends AProcessor {

    private IAlipayAO alipayAO = SpringContextHolder.getBean(IAlipayAO.class);

    private XN002510Req req = null;

    /** 
     * @see com.std.account.api.IProcessor#doBusiness()
     */
    @Override
    public Object doBusiness() throws BizException {
        Long transAmount = StringValidater.toLong(req.getAmount());
        return alipayAO.getSignedOrder(req.getApplyUser(), req.getToUser(),
            req.getPayGroup(), req.getRefNo(), req.getBizType(),
            req.getBizNote(), transAmount, req.getBackUrl());
    }

    /** 
     * @see com.std.account.api.IProcessor#doCheck(java.lang.String)
     */
    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN002510Req.class);
        StringValidater.validateAmount(req.getAmount());
        StringValidater.validateBlank(req.getApplyUser(), req.getToUser(),
            req.getPayGroup(), req.getRefNo(), req.getBizType(),
            req.getBizNote(), req.getBackUrl());

    }
}
