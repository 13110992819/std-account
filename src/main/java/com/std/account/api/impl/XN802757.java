package com.std.account.api.impl;

import com.std.account.ao.IWithdrawAO;
import com.std.account.api.AProcessor;
import com.std.account.common.DateUtil;
import com.std.account.common.JsonUtil;
import com.std.account.core.StringValidater;
import com.std.account.domain.Withdraw;
import com.std.account.dto.req.XN802757Req;
import com.std.account.exception.BizException;
import com.std.account.exception.ParaException;
import com.std.account.spring.SpringContextHolder;

/**
 * 取现列表查询
 * @author: xieyj 
 * @since: 2017年5月18日 下午4:26:11 
 * @history:
 */
public class XN802757 extends AProcessor {
    private IWithdrawAO withdrawAO = SpringContextHolder
        .getBean(IWithdrawAO.class);

    private XN802757Req req = null;

    @Override
    public Object doBusiness() throws BizException {
        Withdraw condition = new Withdraw();
        condition.setAccountNumber(req.getAccountNumber());
        condition.setAccountName(req.getAccountName());
        condition.setChannelType(req.getChannelType());
        condition.setStatus(req.getStatus());
        condition.setApplyUser(req.getApplyUser());

        condition.setApproveUser(req.getApproveUser());
        condition.setPayUser(req.getPayUser());
        condition.setPayGroup(req.getPayGroup());
        condition.setChannelOrder(req.getChannelOrder());
        condition.setSystemCode(req.getSystemCode());
        condition.setCompanyCode(req.getCompanyCode());

        condition.setApplyDatetimeStart(DateUtil.getFrontDate(
            req.getApplyDateStart(), false));
        condition.setApplyDatetimeEnd(DateUtil.getFrontDate(
            req.getApplyDateEnd(), true));
        condition.setApproveDatetimeStart(DateUtil.getFrontDate(
            req.getApproveDateStart(), false));
        condition.setApproveDatetimeEnd(DateUtil.getFrontDate(
            req.getApproveDateEnd(), true));
        condition.setPayDatetimeStart(DateUtil.getFrontDate(
            req.getPayDateStart(), false));
        condition.setPayDatetimeEnd(DateUtil.getFrontDate(req.getPayDateEnd(),
            true));
        return withdrawAO.queryWithdrawList(condition);
    }

    @Override
    public void doCheck(String inputparams) throws ParaException {
        req = JsonUtil.json2Bean(inputparams, XN802757Req.class);
        StringValidater
            .validateBlank(req.getSystemCode(), req.getCompanyCode());
    }
}
