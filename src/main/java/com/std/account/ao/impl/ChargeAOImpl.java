package com.std.account.ao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.std.account.ao.IChargeAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.IChargeBO;
import com.std.account.bo.ICompanyChannelBO;
import com.std.account.bo.IUserBO;
import com.std.account.bo.IWechatBO;
import com.std.account.bo.base.Paginable;
import com.std.account.common.PropertiesUtil;
import com.std.account.common.SysConstant;
import com.std.account.domain.Account;
import com.std.account.domain.Charge;
import com.std.account.domain.CompanyChannel;
import com.std.account.domain.User;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EChargeStatus;
import com.std.account.enums.ECurrency;
import com.std.account.enums.EJourBizType;
import com.std.account.exception.BizException;

@Service
public class ChargeAOImpl implements IChargeAO {
    @Autowired
    private IUserBO userBO;

    @Autowired
    private IAccountBO accountBO;

    @Autowired
    private IChargeBO chargeBO;

    @Autowired
    private IWechatBO wechatBO;

    @Autowired
    private ICompanyChannelBO companyChannelBO;

    @Override
    public String applyOrder(String accountNumber, Long amount,
            String payCardInfo, String payCardNo, String applyUser,
            String applyNote) {
        Account account = accountBO.getAccount(accountNumber);
        // 生成充值订单
        String code = chargeBO.applyOrder(account, amount, payCardInfo,
            payCardNo, applyUser, applyNote);
        return code;
    }

    @Override
    public void payOrder(String code, String payUser, String payResult,
            String payNote, String payCode, String systemCode) {
        Charge data = chargeBO.getCharge(code, systemCode);
        if (!EChargeStatus.toPay.getCode().equals(data.getStatus())) {
            throw new BizException("xn000000", "申请记录状态不是待支付状态，无法支付");
        }
        if (EBoolean.YES.getCode().equals(payResult)) {
            payOrderYES(data, payUser, payNote, payCode);
        } else {
            payOrderNO(data, payUser, payNote, payCode);
        }
    }

    private void payOrderNO(Charge data, String payUser, String payNote,
            String payCode) {
        chargeBO.payOrder(data, false, payUser, payNote, payCode);
    }

    private void payOrderYES(Charge data, String payUser, String payNote,
            String payCode) {
        chargeBO.payOrder(data, true, payUser, payNote, payCode);
        accountBO.changeAmount(data.getAccountNumber(), EChannelType.Offline,
            data.getCode(), data.getAmount(), EJourBizType.AJ_CZ, "线下充值");
    }

    @Override
    @Transactional
    public Object doWechatH5(String userId, Long amount) {
        if (amount == 0) {
            throw new BizException("xn000000", "充值金额不能为0");
        }
        User dbUser = userBO.getRemoteUser(userId);
        if (StringUtils.isBlank(dbUser.getOpenId())) {
            throw new BizException("xn000000", "请微信登录后再支付");
        }
        Account dbAccount = accountBO.getAccountByUser(userId,
            ECurrency.CNY.getCode());
        // 生成充值订单
        String chagerCode = chargeBO.onlineOrder(dbAccount, dbUser, amount,
            EChannelType.WeChat_H5);
        // 返回微信H5充值参数给前端
        String systemCode = dbAccount.getSystemCode();
        CompanyChannel companyChannel = companyChannelBO.getCompanyChannel(
            systemCode, systemCode, EChannelType.WeChat_H5.getCode());
        String prepayId = wechatBO.getPrepayIdH5(companyChannel,
            dbUser.getOpenId(), "微信公众号充值", chagerCode, amount, SysConstant.IP,
            PropertiesUtil.Config.WECHAT_H5_QzBACKURL, null);
        return wechatBO.getPayInfoH5(companyChannel, chagerCode, prepayId);
    }

    @Override
    public Paginable<Charge> queryChargePage(int start, int limit,
            Charge condition) {
        return chargeBO.getPaginable(start, limit, condition);
    }

    @Override
    public List<Charge> queryChargeList(Charge condition) {
        return chargeBO.queryChargeList(condition);
    }

    @Override
    public Charge getCharge(String code, String systemCode) {
        return chargeBO.getCharge(code, systemCode);
    }

}
