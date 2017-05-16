package com.std.account.ao.impl;

import java.util.List;

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
import com.std.account.domain.Account;
import com.std.account.domain.Charge;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EChargeStatus;
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
        if (amount <= 0) {
            throw new BizException("xn000000", "充值金额需大于零");
        }
        Account account = accountBO.getAccount(accountNumber);
        // 生成充值订单
        String code = chargeBO.applyOrderOffline(account, amount, payCardInfo,
            payCardNo, applyUser, applyNote);
        return code;
    }

    @Override
    @Transactional
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
