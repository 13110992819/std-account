package com.std.account.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.std.account.bo.IBankcardBO;
import com.std.account.bo.IWithdrawBO;
import com.std.account.bo.base.PaginableBOImpl;
import com.std.account.core.OrderNoGenerater;
import com.std.account.dao.IWithdrawDAO;
import com.std.account.domain.Account;
import com.std.account.domain.Bankcard;
import com.std.account.domain.Withdraw;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EGeneratePrefix;
import com.std.account.enums.EWithdrawStatus;
import com.std.account.exception.BizException;

@Component
public class WithdrawBOImpl extends PaginableBOImpl<Withdraw> implements
        IWithdrawBO {
    @Autowired
    private IBankcardBO bankcardBO; // 取现银行卡户名

    @Autowired
    private IWithdrawDAO withdrawDAO;

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

        // 取现户名，应该和银行卡户名一致
        Bankcard bankcard = bankcardBO.getBankcardInfo(code);
        if (null == bankcard) {
            data.setAccountName(account.getRealName());
        } else {
            data.setAccountName(bankcard.getRealName());
        }
        data.setAmount(amount);
        data.setFee(fee);

        data.setChannelType(EChannelType.Offline.getCode());
        data.setPayCardInfo(payCardInfo);
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

    /** 
     * @see com.std.account.bo.IWithdrawBO#doCheckTimes(java.lang.String)
     */
    @Override
    public void doCheckTimes(String accountNumber) {
        Withdraw condition = new Withdraw();
        condition.setAccountNumber(accountNumber);
        condition.setStatus("13");// 待申请，审核成功待支付
        if (withdrawDAO.selectTotalCount(condition) > 0) {
            throw new BizException("xn000000", "上笔取现申请还未处理成功，不能再次申请");
        }
    }
}
