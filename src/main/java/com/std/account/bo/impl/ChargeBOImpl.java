package com.std.account.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.std.account.bo.IChargeBO;
import com.std.account.bo.base.PaginableBOImpl;
import com.std.account.core.OrderNoGenerater;
import com.std.account.dao.IChargeDAO;
import com.std.account.domain.Account;
import com.std.account.domain.Charge;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EChargeStatus;
import com.std.account.enums.EGeneratePrefix;
import com.std.account.enums.EJourBizType;
import com.std.account.exception.BizException;

@Component
public class ChargeBOImpl extends PaginableBOImpl<Charge> implements IChargeBO {
    @Autowired
    private IChargeDAO chargeDAO;

    @Override
    public String applyOrderOffline(Account account, EJourBizType bizType,
            Long amount, String payCardInfo, String payCardNo,
            String applyUser, String applyNote) {
        if (amount == 0) {
            throw new BizException("xn000000", "充值金额不能为0");
        }
        String code = OrderNoGenerater.generate(EGeneratePrefix.Charge
            .getCode());
        Charge data = new Charge();
        data.setCode(code);
        data.setPayGroup(null);
        data.setRefNo(null);
        data.setAccountNumber(account.getAccountNumber());
        data.setAmount(amount);

        data.setAccountName(account.getRealName());
        data.setBizType(bizType.getCode());
        if (StringUtils.isBlank(applyNote)) {
            data.setBizNote(bizType.getValue());
        } else {
            data.setBizNote(applyNote);
        }
        data.setPayCardInfo(payCardInfo);
        data.setPayCardNo(payCardNo);

        data.setStatus(EChargeStatus.toPay.getCode());
        data.setApplyUser(applyUser);
        data.setApplyDatetime(new Date());
        data.setChannelType(EChannelType.Offline.getCode());
        data.setSystemCode(account.getSystemCode());
        data.setCompanyCode(account.getCompanyCode());
        chargeDAO.insert(data);
        return code;
    }

    @Override
    public String applyOrderOnline(Account account, String payGroup,
            String refNo, EJourBizType bizType, String bizNote,
            Long transAmount, EChannelType channelType, String applyUser) {
        if (transAmount == 0) {
            throw new BizException("xn000000", "充值金额不能为0");
        }
        String code = OrderNoGenerater.generate(EGeneratePrefix.Charge
            .getCode());
        Charge data = new Charge();
        data.setCode(code);
        data.setPayGroup(payGroup);
        data.setRefNo(refNo);
        data.setAccountNumber(account.getAccountNumber());
        data.setAmount(transAmount);

        data.setAccountName(account.getRealName());
        data.setBizType(bizType.getCode());
        data.setBizNote(bizNote);
        data.setPayCardInfo(null);
        data.setPayCardNo(null);

        data.setStatus(EChargeStatus.toPay.getCode());
        data.setApplyUser(applyUser);
        data.setApplyDatetime(new Date());
        data.setChannelType(channelType.getCode());
        data.setSystemCode(account.getSystemCode());

        data.setCompanyCode(account.getCompanyCode());
        chargeDAO.insert(data);
        return code;
    }

    @Override
    public void payOrder(Charge data, boolean booleanFlag, String payUser,
            String payNote) {
        if (booleanFlag) {
            data.setStatus(EChargeStatus.Pay_YES.getCode());
        } else {
            data.setStatus(EChargeStatus.Pay_NO.getCode());
        }
        data.setPayUser(payUser);
        data.setPayNote(payNote);
        data.setPayDatetime(new Date());
        chargeDAO.payOrder(data);
    }

    @Override
    public void callBackChange(Charge dbCharge, boolean booleanFlag) {
        if (booleanFlag) {
            dbCharge.setStatus(EChargeStatus.Pay_YES.getCode());
        } else {
            dbCharge.setStatus(EChargeStatus.Pay_NO.getCode());
        }
        dbCharge.setPayUser(null);
        dbCharge.setPayNote("在线充值自动回调");
        dbCharge.setPayDatetime(new Date());
        chargeDAO.payOrder(dbCharge);

    }

    @Override
    public List<Charge> queryChargeList(Charge condition) {
        return chargeDAO.selectList(condition);
    }

    @Override
    public Charge getCharge(String code, String systemCode) {
        Charge order = null;
        if (StringUtils.isNotBlank(code)) {
            Charge condition = new Charge();
            condition.setCode(code);
            condition.setSystemCode(systemCode);
            order = chargeDAO.select(condition);
            if (null == order) {
                throw new BizException("xn000000", "订单号[" + code + "]不存在");
            }
        }
        return order;
    }

}
