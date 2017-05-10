package com.std.account.bo;

import java.util.List;

import com.std.account.bo.base.IPaginableBO;
import com.std.account.domain.Account;
import com.std.account.domain.HLOrder;
import com.std.account.domain.Jour;
import com.std.account.enums.EHLOrderStatus;

public interface IHLOrderBO extends IPaginableBO<HLOrder> {

    String applyOrder(Account account, Jour jour, Long applyAmount,
            String applyUser, String applyNote);

    void approveOrder(HLOrder order, EHLOrderStatus status, String approveUser,
            String approveNote);

    HLOrder getHLOrder(String code, String systemCode);

    List<HLOrder> queryHLOrderList(HLOrder condition);

}
