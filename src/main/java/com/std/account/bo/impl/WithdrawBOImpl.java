package com.std.account.bo.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.std.account.bo.IBankcardBO;
import com.std.account.bo.IChannelBankBO;
import com.std.account.bo.ISYSConfigBO;
import com.std.account.bo.IWithdrawBO;
import com.std.account.bo.base.PaginableBOImpl;
import com.std.account.common.DateUtil;
import com.std.account.common.SysConstant;
import com.std.account.core.OrderNoGenerater;
import com.std.account.dao.IWithdrawDAO;
import com.std.account.domain.Account;
import com.std.account.domain.Bankcard;
import com.std.account.domain.ChannelBank;
import com.std.account.domain.Withdraw;
import com.std.account.enums.EAccountType;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EGeneratePrefix;
import com.std.account.enums.EWithdrawStatus;
import com.std.account.exception.BizException;
import com.std.account.util.AmountUtil;

@Component
public class WithdrawBOImpl extends PaginableBOImpl<Withdraw> implements
        IWithdrawBO {
    @Autowired
    private IBankcardBO bankcardBO; // 取现银行卡户名

    @Autowired
    private IWithdrawDAO withdrawDAO;

    @Autowired
    ISYSConfigBO sysConfigBO;

    @Autowired
    IChannelBankBO channelBankBO;

    @Override
    public String applyOrder(Account account, Long amount, Long fee,
            String payCardInfo, String payCardNo, String applyUser,
            String applyNote) {
        if (amount == 0) {
            throw new BizException("xn000000", "取现金额不能为0");
        }
        String code = OrderNoGenerater.generate(EGeneratePrefix.Withdraw
            .getCode());
        Withdraw data = new Withdraw();
        data.setCode(code);
        data.setAccountNumber(account.getAccountNumber());
        data.setType(account.getType());
        data.setAmount(amount);
        data.setFee(fee);

        data.setChannelType(EChannelType.Offline.getCode());
        data.setPayCardInfo(payCardInfo);
        // 取现户名，应该和银行卡户名一致
        Bankcard bankcard = bankcardBO.getBankcardByBankcardNumber(payCardNo);
        if (null == bankcard) {
            data.setAccountName(account.getRealName());
        } else {
            // 设置银行名称和银行名称
            data.setAccountName(bankcard.getRealName());
            data.setPayCardInfo(bankcard.getBankName());
            // 获取银行编号
            ChannelBank channelBank = channelBankBO.getChannelBank(bankcard
                .getBankCode());
            if (null != channelBank) {
                data.setChannelBank(channelBank.getChannelBank());
            }
        }
        data.setPayCardNo(payCardNo);
        data.setStatus(EWithdrawStatus.toApprove.getCode());
        data.setApplyUser(applyUser);

        data.setApplyNote(applyNote);
        data.setApplyDatetime(new Date());
        data.setSystemCode(account.getSystemCode());
        data.setCompanyCode(account.getCompanyCode());
        withdrawDAO.insert(data);
        return code;
    }

    @Override
    public void approveOrder(Withdraw data, EWithdrawStatus status,
            String approveUser, String approveNote) {
        data.setStatus(status.getCode());
        data.setApproveUser(approveUser);
        data.setApproveNote(approveNote);
        data.setApproveDatetime(new Date());
        withdrawDAO.approveOrder(data);

    }

    @Override
    public void payOrder(Withdraw data, EWithdrawStatus status, String payUser,
            String payNote, String channelOrder) {
        data.setStatus(status.getCode());
        data.setPayUser(payUser);
        data.setPayNote(payNote);
        data.setPayGroup(null);
        data.setChannelOrder(channelOrder);
        data.setPayDatetime(new Date());
        withdrawDAO.payOrder(data);
    }

    @Override
    public List<Withdraw> queryWithdrawList(Withdraw condition) {
        return withdrawDAO.selectList(condition);
    }

    @Override
    public Withdraw getWithdraw(String code, String systemCode) {
        Withdraw order = null;
        if (StringUtils.isNotBlank(code)) {
            Withdraw condition = new Withdraw();
            condition.setCode(code);
            condition.setSystemCode(systemCode);
            order = withdrawDAO.select(condition);
        }
        return order;
    }

    @Override
    public void doCheckArgs(Account account, Long amount) {
        Map<String, String> argsMap = sysConfigBO.getConfigsMap(
            account.getSystemCode(), account.getCompanyCode());
        // 判断本月申请次数是否达到上限
        String monthTimesKey = null;
        if (EAccountType.Customer.getCode().equals(account.getType())) {
            monthTimesKey = SysConstant.CUSERMONTIMES;
        } else if (EAccountType.Business.getCode().equals(account.getType())) {
            monthTimesKey = SysConstant.BUSERMONTIMES;
        }
        String monthTimesValue = argsMap.get(monthTimesKey);
        if (StringUtils.isNotBlank(monthTimesValue)) {// 月取现次数判断
            Withdraw condition = new Withdraw();
            condition.setAccountNumber(account.getAccountNumber());
            condition.setApplyDatetimeStart(DateUtil.getCurrentMonthFirstDay());
            condition.setApplyDatetimeEnd(DateUtil.getCurrentMonthLastDay());
            long totalCount = withdrawDAO.selectTotalCount(condition);
            long maxMonthTimes = Long.valueOf(monthTimesValue);
            if (totalCount >= maxMonthTimes) {
                throw new BizException("xn0000", "每月取现最多" + maxMonthTimes
                        + "次,本月申请次数已用尽");
            }
        }

        // 判断是否还有未处理的取现记录
        Withdraw condition = new Withdraw();
        condition.setAccountNumber(account.getAccountNumber());
        condition.setStatus("13");// 待申请，审核成功待支付
        if (withdrawDAO.selectTotalCount(condition) > 0) {
            throw new BizException("xn000000", "上笔取现申请还未处理成功，不能再次申请");
        }

        // 取现金额验证
        if (amount <= 0) {
            throw new BizException("xn000000", "提现金额需大于零");
        }
        String qxDbzdjeValue = argsMap.get(SysConstant.QXDBZDJE);
        if (StringUtils.isNotBlank(qxDbzdjeValue)) {
            Long qxDbzdje = AmountUtil
                .mul(1000L, Double.valueOf(qxDbzdjeValue));
            if (amount > qxDbzdje) {
                throw new BizException("xn000000", "取现单笔最大金额不能超过"
                        + qxDbzdjeValue + "元。");
            }
        }
        String qxbs = null; // 取现倍数
        if (EAccountType.Customer.getCode().equals(account.getType())) {
            qxbs = SysConstant.CUSERQXBS;
        } else if (EAccountType.Business.getCode().equals(account.getType())) {
            qxbs = SysConstant.BUSERQXBS;
        }
        String qxBsValue = argsMap.get(qxbs);
        if (StringUtils.isNotBlank(qxBsValue)) {
            // 取现金额倍数
            Long qxBs = AmountUtil.mul(1000L, Double.valueOf(qxBsValue));
            if (qxBs > 0 && amount % qxBs > 0) {
                throw new BizException("xn000000", "金额请取" + qxBsValue + "的倍数");
            }
        }
    }

    // 获取手续费
    @Override
    public Long doGetFee(Account account, Long amount) {
        long fee = 0;
        double feeRate = 0;
        Map<String, String> argsMap = sysConfigBO.getConfigsMap(
            account.getSystemCode(), account.getCompanyCode());
        String qxfl = null;
        if (EAccountType.Customer.getCode().equals(account.getType())) {
            qxfl = SysConstant.CUSERQXFL;
        } else if (EAccountType.Business.getCode().equals(account.getType())) {
            qxfl = SysConstant.BUSERQXFL;
        }
        String feeRateValue = argsMap.get(qxfl);
        if (StringUtils.isNotBlank(feeRateValue)) {
            feeRate = Double.valueOf(feeRateValue);
        }
        fee = AmountUtil.mul(amount, feeRate);

        return fee;
    }
}
