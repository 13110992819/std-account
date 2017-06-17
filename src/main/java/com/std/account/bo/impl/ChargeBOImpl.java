package com.std.account.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.std.account.bo.IChargeBO;
import com.std.account.bo.ISYSConfigBO;
import com.std.account.bo.base.PaginableBOImpl;
import com.std.account.common.DateUtil;
import com.std.account.common.SysConstant;
import com.std.account.core.OrderNoGenerater;
import com.std.account.dao.IChargeDAO;
import com.std.account.domain.Account;
import com.std.account.domain.Charge;
import com.std.account.domain.SYSConfig;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EChargeStatus;
import com.std.account.enums.EGeneratePrefix;
import com.std.account.enums.EJourBizType;
import com.std.account.exception.BizException;
import com.std.account.util.AmountUtil;

@Component
public class ChargeBOImpl extends PaginableBOImpl<Charge> implements IChargeBO {
    @Autowired
    private IChargeDAO chargeDAO;

    @Autowired
    private ISYSConfigBO sysConfigBO;

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
        data.setType(account.getType());
        data.setCurrency(account.getCurrency());
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
        data.setType(account.getType());
        data.setCurrency(account.getCurrency());
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

    @Override
    public void doCheckTodayPayAmount(String applyUser, Long payAmount,
            EChannelType channelType, String companyCode, String systemCode) {
        String dayMaxAmountKey = null;
        if (EChannelType.Alipay.getCode().equals(channelType.getCode())) {
            dayMaxAmountKey = SysConstant.ZFB_DAY_MAX_AMOUNT;
        } else if (EChannelType.WeChat_APP.getCode().equals(
            channelType.getCode())) {
            dayMaxAmountKey = SysConstant.WX_DAY_MAX_AMOUNT;
        }
        SYSConfig sysConfig = sysConfigBO.getSYSConfigNotException(
            dayMaxAmountKey, companyCode, systemCode);
        if (null != sysConfig) {// 系统参数未配置，不做判断
            String dayMaxAmountValue = sysConfig.getCvalue();
            Long dayMaxAmount = AmountUtil.mul(1000L,
                Double.valueOf(dayMaxAmountValue));
            if (dayMaxAmount.longValue() <= 0) {
                throw new BizException("xn0000", "当前渠道维护中，请使用其他支付方式。");
            }
            Charge condition = new Charge();
            condition.setChannelType(channelType.getCode());
            condition.setStatus(EChargeStatus.Pay_YES.getCode());
            condition.setApplyUser(applyUser);
            condition.setPayDatetimeStart(DateUtil.getTodayStart());
            condition.setPayDatetimeEnd(DateUtil.getTodayEnd());
            Long todayAmount = chargeDAO.selectTotalAmount(condition);
            Long nowTodayAmount = todayAmount + payAmount;// 今日+本次支付是否超日限额
            if (dayMaxAmount < nowTodayAmount) {
                throw new BizException("xn0000", "该渠道每日限额" + dayMaxAmountValue
                        + ",本次支付将超额，请明天再来");
            }
        }
    }
}
