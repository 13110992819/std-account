package com.std.account.bo;

import java.util.List;

import com.std.account.bo.base.IPaginableBO;
import com.std.account.domain.Jour;

/**
 * @author: xieyj 
 * @since: 2016年12月23日 下午2:40:13 
 * @history:
 */
public interface IJourHistoryBO extends IPaginableBO<Jour> {

    public List<Jour> queryJourList(Jour condition);

    public Jour getJour(String code, String systemCode);

    public Long getTotalAmount(String bizType, String channelType,
            String accountNumber);
}
