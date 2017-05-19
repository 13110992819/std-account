package com.std.account.bo;

import java.util.List;

import com.std.account.bo.base.IPaginableBO;
import com.std.account.domain.Account;
import com.std.account.domain.HLOrder;
import com.std.account.domain.Jour;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EJourBizType;

/**
 * @author: xieyj 
 * @since: 2016年12月23日 下午2:40:13 
 * @history:
 */
public interface IJourBO extends IPaginableBO<Jour> {
    // 不需要对账的新增
    public String addJourForHL(Account dbAccount, HLOrder order);

    // 正常新增
    public String addJour(Account dbAccount, EChannelType channelType,
            String channelOrder, String payGroup, String refNo,
            EJourBizType bizType, String bizNote, Long transAmount);

    // 对账结果录入
    public void doCheckJour(Jour jour, EBoolean checkResult, Long checkAmount,
            String checkUser, String checkNote);

    // 当前流水，调整不通过。即账其实是平的
    public void adjustJourNO(Jour jour, String adjustUser, String adjustNote);

    public void adjustJourYES(Jour jour, String adjustUser, String adjustNote);

    public List<Jour> queryJourList(Jour condition);

    public Jour getJour(String code, String systemCode);

    public Long getTotalAmount(String bizType, String channelType,
            String accountNumber);

}
