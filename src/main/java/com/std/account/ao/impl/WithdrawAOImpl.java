package com.std.account.ao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.std.account.ao.IWithdrawAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.ISYSConfigBO;
import com.std.account.bo.IUserBO;
import com.std.account.bo.IWithdrawBO;
import com.std.account.bo.base.Paginable;
import com.std.account.common.SysConstant;
import com.std.account.domain.Account;
import com.std.account.domain.User;
import com.std.account.domain.Withdraw;
import com.std.account.enums.EAccountType;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EChannelType;
import com.std.account.enums.ECurrency;
import com.std.account.enums.EJourBizType;
import com.std.account.enums.EWithdrawStatus;
import com.std.account.exception.BizException;
import com.std.account.util.AmountUtil;

@Service
public class WithdrawAOImpl implements IWithdrawAO {
    @Autowired
    private IAccountBO accountBO;

    @Autowired
    private IWithdrawBO withdrawBO;

    @Autowired
    private IUserBO userBO;

    @Autowired
    private ISYSConfigBO sysConfigBO;

    @Override
    @Transactional
    public String applyOrderTradePwd(String accountNumber, Long amount,
            String payCardInfo, String payCardNo, String applyUser,
            String applyNote, String tradePwd) {
        if (amount <= 0) {
            throw new BizException("xn000000", "提现金额需大于零");
        }
        Account dbAccount = accountBO.getAccount(accountNumber);
        // 判断本月是否次数已满，且现在只能有一笔取现未支付记录
        withdrawBO.doCheckTimes(dbAccount);
        // 验证交易密码
        userBO.checkTradePwd(dbAccount.getUserId(), tradePwd);
        if (dbAccount.getAmount() < amount) {
            throw new BizException("xn000000", "余额不足");
        }
        // 生成取现订单
        Long fee = doGetFee(dbAccount.getType(), amount,
            dbAccount.getSystemCode(), dbAccount.getCompanyCode());
        // 取现总金额
        amount = amount + fee;
        String withdrawCode = withdrawBO.applyOrder(dbAccount, amount, fee,
            payCardInfo, payCardNo, applyUser, applyNote);
        // 冻结取现金额
        accountBO.frozenAmount(dbAccount, amount, withdrawCode);
        return withdrawCode;
    }

    @Override
    @Transactional
    public String applyOrder(String accountNumber, Long amount,
            String payCardInfo, String payCardNo, String applyUser,
            String applyNote) {
        if (amount <= 0) {
            throw new BizException("xn000000", "提现金额需大于零");
        }
        Account dbAccount = accountBO.getAccount(accountNumber);
        // 判断本月是否次数已满，且现在只能有一笔取现未支付记录
        withdrawBO.doCheckTimes(dbAccount);
        if (dbAccount.getAmount() < amount) {
            throw new BizException("xn000000", "余额不足");
        }
        // 生成取现订单
        Long fee = doGetFee(dbAccount.getType(), amount,
            dbAccount.getSystemCode(), dbAccount.getCompanyCode());
        // 取现总金额
        amount = amount + fee;
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
            String payNote, String channelOrder, String systemCode) {
        Withdraw data = withdrawBO.getWithdraw(code, systemCode);
        if (!EWithdrawStatus.Approved_YES.getCode().equals(data.getStatus())) {
            throw new BizException("xn000000", "申请记录状态不是待支付状态，无法支付");
        }
        if (EBoolean.YES.getCode().equals(payResult)) {
            payOrderYES(data, payUser, payNote, channelOrder);
        } else {
            payOrderNO(data, payUser, payNote, channelOrder);
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
        Account account = accountBO.getAccount(data.getAccountNumber());
        if (ECurrency.CNY.getCode().equals(account.getCurrency())
                || ECurrency.ZH_FRB.getCode().equals(account.getCurrency())) {
            // 托管账户减钱
            accountBO.changeAmount(data.getCompanyCode(), EChannelType.Offline,
                null, null, data.getCode(), EJourBizType.AJ_QX, "线下取现",
                -data.getAmount());
        }
    }

    @Override
    public Paginable<Withdraw> queryWithdrawPage(int start, int limit,
            Withdraw condition) {
        Paginable<Withdraw> page = withdrawBO.getPaginable(start, limit,
            condition);
        if (CollectionUtils.isNotEmpty(page.getList())) {
            List<Withdraw> list = page.getList();
            for (Withdraw withdraw : list) {
                User user = userBO.getRemoteUser(withdraw.getApplyUser());
                withdraw.setUser(user);
            }
        }
        return page;
    }

    @Override
    public List<Withdraw> queryWithdrawList(Withdraw condition) {
        List<Withdraw> list = withdrawBO.queryWithdrawList(condition);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Withdraw withdraw : list) {
                User user = userBO.getRemoteUser(withdraw.getApplyUser());
                withdraw.setUser(user);
            }
        }
        return list;
    }

    @Override
    public Withdraw getWithdraw(String code, String systemCode) {
        Withdraw withdraw = withdrawBO.getWithdraw(code, systemCode);
        User user = userBO.getRemoteUser(withdraw.getApplyUser());
        withdraw.setUser(user);
        return withdraw;
    }

    /**
     * 取现申请检查，验证参数，返回手续费
     * @param accountType
     * @param amount
     * @param systemCode
     * @param companyCode
     * @return 
     * @create: 2017年5月17日 上午7:53:01 xieyj
     * @history:
     */
    private Long doGetFee(String accountType, Long amount, String systemCode,
            String companyCode) {
        Map<String, String> argsMap = sysConfigBO.getConfigsMap(systemCode,
            companyCode);
        String qxbs = null;
        String qxfl = null;
        if (EAccountType.Customer.getCode().equals(accountType)) {
            qxbs = SysConstant.CUSERQXBS;
            qxfl = SysConstant.CUSERQXFL;
        } else if (EAccountType.Business.getCode().equals(accountType)) {
            qxbs = SysConstant.BUSERQXBS;
            qxfl = SysConstant.BUSERQXFL;
        } else {// 暂定其他账户类型不收手续费
            return 0L;
        }
        String qxBsValue = argsMap.get(qxbs);
        if (StringUtils.isNotBlank(qxBsValue)) {
            // 取现金额倍数
            Long qxBs = AmountUtil.mul(1000L, Double.valueOf(qxBsValue));
            if (qxBs > 0 && -amount % qxBs > 0) {
                throw new BizException("xn000000", "请取" + qxBsValue + "的倍数");
            }
        }
        String feeRateValue = argsMap.get(qxfl);
        Double feeRate = 0D;
        if (StringUtils.isNotBlank(feeRateValue)) {
            feeRate = Double.valueOf(feeRateValue);
        }
        return AmountUtil.mul(-amount, feeRate);
    }
}
