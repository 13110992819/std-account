package com.std.account.ao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.std.account.ao.IWithdrawAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.IWithdrawBO;
import com.std.account.bo.base.Paginable;
import com.std.account.domain.Account;
import com.std.account.domain.Withdraw;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EWithdrawStatus;
import com.std.account.exception.BizException;

@Service
public class WithdrawAOImpl implements IWithdrawAO {
    @Autowired
    private IAccountBO accountBO;

    @Autowired
    private IWithdrawBO withdrawBO;

    // @Autowired
    // private IUserBO userBO;// 交易密码
    //
    // @Autowired
    // private IBankcardBO bankcardBO; // 取现银行卡户名
    //
    // @Autowired
    // private ISYSConfigBO sysConfigBO; //取现配置

    @Override
    @Transactional
    public String applyOrder(String accountNumber, Long amount,
            String payCardInfo, String payCardNo, String applyUser,
            String applyNote) {
        if (amount <= 0) {
            throw new BizException("xn000000", "提现金额需大于零");
        }
        Account dbAccount = accountBO.getAccount(accountNumber);
        if (dbAccount.getAmount() < amount) {
            throw new BizException("xn000000", "余额不足");
        }
        // 生成取现订单
        Long fee = 0L;// 手续费暂为0
        String withdrawCode = withdrawBO.applyOrder(dbAccount, amount, fee,
            payCardInfo, payCardNo, applyUser, applyNote);
        // 冻结取现金额
        accountBO.frozenAmount(dbAccount, amount, withdrawCode);
        return withdrawCode;
    }

    @Override
    @Transactional
    public void approveOrder(String code, String approveUser,
            String approveResult, String approveNote, String systemCode) {
        Withdraw data = withdrawBO.getWithdraw(code, systemCode);
        if (!EWithdrawStatus.toApprove.getCode().equals(data.getStatus())) {
            throw new BizException("xn000000", "申请记录状态不是待审批状态，无法审批");
        }
        if (EBoolean.YES.getCode().equals(approveResult)) {
            approveOrderYES(data, approveUser, approveNote);
        } else {
            approveOrderNO(data, approveUser, approveNote);
        }
    }

    @Override
    @Transactional
    public void payOrder(String code, String payUser, String payResult,
            String payNote, String payCode, String systemCode) {
        Withdraw data = withdrawBO.getWithdraw(code, systemCode);
        if (!EWithdrawStatus.Approved_YES.getCode().equals(data.getStatus())) {
            throw new BizException("xn000000", "申请记录状态不是待支付状态，无法支付");
        }
        if (EBoolean.YES.getCode().equals(payResult)) {
            payOrderYES(data, payUser, payNote, payCode);
        } else {
            payOrderNO(data, payUser, payNote, payCode);
        }
    }

    private void approveOrderYES(Withdraw data, String approveUser,
            String approveNote) {
        withdrawBO.approveOrder(data, EWithdrawStatus.Approved_YES,
            approveUser, approveNote);
    }

    private void approveOrderNO(Withdraw data, String approveUser,
            String approveNote) {
        withdrawBO.approveOrder(data, EWithdrawStatus.Approved_NO, approveUser,
            approveNote);
        Account dbAccount = accountBO.getAccount(data.getAccountNumber());
        // 释放冻结流水
        accountBO.unfrozenAmount(dbAccount, data.getAmount(), data.getCode());
    }

    private void payOrderNO(Withdraw data, String payUser, String payNote,
            String payCode) {
        withdrawBO.payOrder(data, EWithdrawStatus.Pay_NO, payUser, payNote,
            payCode);
        Account dbAccount = accountBO.getAccount(data.getAccountNumber());
        // 释放冻结流水
        accountBO.unfrozenAmount(dbAccount, data.getAmount(), data.getCode());
    }

    private void payOrderYES(Withdraw data, String payUser, String payNote,
            String payCode) {
        withdrawBO.payOrder(data, EWithdrawStatus.Pay_YES, payUser, payNote,
            payCode);
        Account dbAccount = accountBO.getAccount(data.getAccountNumber());
        // 扣减冻结流水
        accountBO.cutFrozenAmount(dbAccount, data.getAmount());
    }

    @Override
    public Paginable<Withdraw> queryWithdrawPage(int start, int limit,
            Withdraw condition) {
        return withdrawBO.getPaginable(start, limit, condition);
    }

    @Override
    public List<Withdraw> queryWithdrawList(Withdraw condition) {
        return withdrawBO.queryWithdrawList(condition);
    }

    @Override
    public Withdraw getWithdraw(String code, String systemCode) {
        return withdrawBO.getWithdraw(code, systemCode);
    }

    // /**
    // * 取现申请检查，验证参数，返回手续费
    // * @param transAmount
    // * @param qxbs
    // * @param qxfl
    // * @param systemCode
    // * @return
    // * @create: 2017年5月2日 下午4:15:01 xieyj
    // * @history:
    // */
    // private Long doCheckWithArgs(Long transAmount, String qxbs, String qxfl,
    // String systemCode) {
    // Map<String, String> argsMap = sysConfigBO.getConfigsMap(systemCode);
    // String qxBsValue = argsMap.get(qxbs);
    // if (StringUtils.isNotBlank(qxBsValue)) {
    // // 取现金额倍数
    // Long qxBs = AmountUtil.mul(1000L, Double.valueOf(qxBsValue));
    // if (qxBs > 0 && -transAmount % qxBs > 0) {
    // throw new BizException("xn000000", "请取" + qxBsValue + "的倍数");
    // }
    // }
    // String feeRateValue = argsMap.get(qxfl);
    // Double feeRate = 0D;
    // if (StringUtils.isNotBlank(feeRateValue)) {
    // feeRate = Double.valueOf(feeRateValue);
    // }
    // return AmountUtil.mul(-transAmount, feeRate);
    // }

}
