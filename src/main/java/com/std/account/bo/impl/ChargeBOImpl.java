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
import com.std.account.domain.User;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EChargeStatus;
import com.std.account.enums.EGeneratePrefix;
import com.std.account.exception.BizException;

@Component
public class ChargeBOImpl extends PaginableBOImpl<Charge> implements IChargeBO {
    @Autowired
    private IChargeDAO chargeDAO;

    @Override
    public String applyOrder(Account account, Long amount, String payCardInfo,
            String payCardNo, String applyUser, String applyNote) {
        if (amount == 0) {
            throw new BizException("xn000000", "充值金额不能为0");
        }
        String code = OrderNoGenerater.generate(EGeneratePrefix.Charge
            .getCode());
        Charge data = new Charge();
        data.setCode(code);
        data.setAccountNumber(account.getAccountNumber());
        data.setAccountName(account.getRealName());
        data.setAmount(amount);
        data.setChannelType(EChannelType.Offline.getCode());

        data.setPayCardInfo(payCardInfo);
        data.setPayCardNo(payCardNo);
        data.setPayGroup(null);
        data.setStatus(EChargeStatus.toPay.getCode());
        data.setApplyUser(applyUser);

        data.setApplyNote(applyNote);
        data.setApplyDatetime(new Date());
        data.setSystemCode(account.getSystemCode());
        chargeDAO.insert(data);
        return code;
    }

    @Override
    public void payOrder(Charge data, boolean booleanFlag, String payUser,
            String payNote, String payCode) {
        if (booleanFlag) {
            data.setStatus(EChargeStatus.Pay_YES.getCode());
        } else {
            data.setStatus(EChargeStatus.Pay_NO.getCode());
        }
        data.setPayUser(payUser);
        data.setPayNote(payNote);
        data.setPayCode(payCode);
        data.setPayDatetime(new Date());
        chargeDAO.payOrder(data);
    }

    @Override
    public String onlineOrder(Account dbAccount, User dbUser, Long amount,
            EChannelType channelType) {
        if (amount == 0) {
            throw new BizException("xn000000", "充值金额不能为0");
        }
        String code = OrderNoGenerater.generate(EGeneratePrefix.Charge
            .getCode());
        Charge data = new Charge();
        data.setCode(code);
        data.setAccountNumber(dbAccount.getAccountNumber());
        data.setAccountName(dbAccount.getRealName());
        data.setAmount(amount);
        data.setChannelType(channelType.getCode());

        data.setPayCardInfo(null);
        data.setPayCardNo(null);
        data.setPayGroup(code);
        data.setStatus(EChargeStatus.toPay.getCode());
        data.setApplyUser(dbUser.getUserId());

        data.setApplyNote("在线充值");
        data.setApplyDatetime(new Date());
        data.setSystemCode(dbAccount.getSystemCode());
        chargeDAO.insert(data);
        return code;
    }

    @Override
    public void callBackChange(Charge dbCharge, boolean booleanFlag,
            String payCode) {
        if (booleanFlag) {
            dbCharge.setStatus(EChargeStatus.Pay_YES.getCode());
        } else {
            dbCharge.setStatus(EChargeStatus.Pay_NO.getCode());
        }
        dbCharge.setPayUser(null);
        dbCharge.setPayNote("在线充值自动回调");
        dbCharge.setPayCode(payCode);
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
