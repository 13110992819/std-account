package com.std.account.bo;

import java.util.List;

import com.std.account.bo.base.IPaginableBO;
import com.std.account.domain.Account;
import com.std.account.domain.Withdraw;
import com.std.account.enums.EWithdrawStatus;

public interface IWithdrawBO extends IPaginableBO<Withdraw> {

    public void doCheckTimes(Account account);

    String applyOrder(Account account, Long amount, Long fee,
            String payCardInfo, String payCardNo, String applyUser,
            String applyNote);

    void approveOrder(Withdraw data, EWithdrawStatus status,
            String approveUser, String approveNote);

    void payOrder(Withdraw data, EWithdrawStatus status, String payUser,
            String payNote, String channelOrder);

    List<Withdraw> queryWithdrawList(Withdraw condition);

    Withdraw getWithdraw(String code, String systemCode);
}
