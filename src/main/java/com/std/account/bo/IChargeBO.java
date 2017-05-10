package com.std.account.bo;

import java.util.List;

import com.std.account.bo.base.IPaginableBO;
import com.std.account.domain.Account;
import com.std.account.domain.Charge;
import com.std.account.domain.User;
import com.std.account.enums.EChannelType;

public interface IChargeBO extends IPaginableBO<Charge> {

    String applyOrder(Account account, Long amount, String payCardInfo,
            String payCardNo, String applyUser, String applyNote);

    void payOrder(Charge data, boolean booleanFlag, String payUser,
            String payNote, String payCode);

    String onlineOrder(Account dbAccount, User dbUser, Long amount,
            EChannelType wechatH5);

    void callBackChange(Charge dbCharge, boolean booleanFlag, String payCode);

    List<Charge> queryChargeList(Charge condition);

    Charge getCharge(String code, String systemCode);

}
