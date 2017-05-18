package com.std.account.ao;

import java.util.List;

import com.std.account.bo.base.Paginable;
import com.std.account.domain.Charge;

public interface IChargeAO {

    String DEFAULT_ORDER_COLUMN = "code";

    public String applyOrder(String accountNumber, String jourBizType,
            Long amount, String payCardInfo, String payCardNo,
            String applyUser, String applyNote);

    public void payOrder(String code, String payUser, String payResult,
            String payNote, String systemCode);

    public Paginable<Charge> queryChargePage(int start, int limit,
            Charge condition);

    public List<Charge> queryChargeList(Charge condition);

    public Charge getCharge(String code, String systemCode);

}
