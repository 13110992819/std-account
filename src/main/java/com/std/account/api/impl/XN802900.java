/**
 * @Title XN802900.java 
 * @Package com.std.account.api.impl 
 * @Description 
 * @author leo(haiqing)  
 * @date 2017年5月18日 下午3:32:00 
 * @version V1.0   
 */
package com.std.account.api.impl;

import com.std.account.ao.IJourAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802900Req;
import com.std.account.dto.res.XN802900Res;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/** 
 * 统计托管账户下，某种业务类型，某种渠道 收入总额
 * @author: haiqingzheng 
 * @since: 2017年5月18日 下午3:32:00 
 * @history:
 */
public class XN802900 extends AProcessor {
    private IJourAO jourAO = SpringContextHolder.getBean(IJourAO.class);

    private XN802900Req req = null;

    /** 
     * @see com.std.account.api.IProcessor#doBusiness()
     */
    @Override
    public Object doBusiness() throws BizException {
        return new XN802900Res(jourAO.getTotalAmount(req.getBizType(),
            req.getChannelType(), req.getAccountNumber()));
    }

    /** 
     * @see com.std.account.api.IProcessor#doCheck(java.lang.String)
     */
    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802900Req.class);
        StringValidater.validateBlank(req.getBizType(), req.getAccountNumber());
    }

}
