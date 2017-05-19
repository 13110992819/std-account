/**
 * @Title XN802710.java 
 * @Package com.std.account.api.impl 
 * @Description 
 * @author leo(haiqing)  
 * @date 2017年5月17日 下午8:42:43 
 * @version V1.0   
 */
package com.std.account.api.impl;

import com.std.account.ao.IWeChatAO;
import com.std.account.api.AProcessor;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.dto.req.XN802710Req;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EJourBizType;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/** 
 * 微信充值
 * @author: haiqingzheng 
 * @since: 2017年5月17日 下午8:42:43 
 * @history:
 */
public class XN802710 extends AProcessor {

    private IWeChatAO weChatAO = SpringContextHolder.getBean(IWeChatAO.class);

    private XN802710Req req = null;

    /** 
     * @see com.std.account.api.IProcessor#doBusiness()
     */
    @Override
    public Object doBusiness() throws BizException {
        Long transAmount = StringValidater.toLong(req.getAmount());
        if (EChannelType.WeChat_H5.getCode().equals(req.getChannelType())) {
            return weChatAO.getPrepayIdH5(req.getApplyUser(), req.getOpenId(),
                req.getApplyUser(), EJourBizType.AJ_CZ.getValue(),
                EJourBizType.AJ_CZ.getValue(), EJourBizType.AJ_CZ.getCode(),
                "微信H5支付充值", transAmount, null);
        } else {
            throw new BizException("xn000000", "暂时不支持该渠道");
        }
    }

    /** 
     * @see com.std.account.api.IProcessor#doCheck(java.lang.String)
     */
    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802710Req.class);
        StringValidater.validateBlank(req.getApplyUser(), req.getOpenId(),
            req.getAmount(), req.getChannelType());
        // H5支付时 openID 必传
        if (EChannelType.WeChat_H5.getCode().equals(req.getChannelType())) {
            StringValidater.validateBlank(req.getOpenId());
        }
    }

}
