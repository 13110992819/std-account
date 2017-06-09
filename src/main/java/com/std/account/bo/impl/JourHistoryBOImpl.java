/**
 * @Title AJourBOImpl.java 
 * @Package com.ibis.account.bo.impl 
 * @Description 
 * @author miyb  
 * @date 2015-3-15 下午3:22:07 
 * @version V1.0   
 */
package com.std.account.bo.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.std.account.bo.IJourHistoryBO;
import com.std.account.bo.base.PaginableBOImpl;
import com.std.account.dao.IJourHistoryDAO;
import com.std.account.domain.Jour;
import com.std.account.exception.BizException;

/** 
 * @author: miyb 
 * @since: 2015-3-15 下午3:22:07 
 * @history:
 */
@Component
public class JourHistoryBOImpl extends PaginableBOImpl<Jour> implements
        IJourHistoryBO {

    @Autowired
    private IJourHistoryDAO jourHistoryDAO;

    @Override
    public Jour getJour(String code, String systemCode) {
        Jour data = null;
        if (StringUtils.isNotBlank(code)) {
            Jour condition = new Jour();
            condition.setCode(code);
            condition.setSystemCode(systemCode);
            data = jourHistoryDAO.select(condition);
            if (data == null) {
                throw new BizException("xn000000", "单号不存在");
            }
        }
        return data;
    }

    @Override
    public List<Jour> queryJourList(Jour condition) {
        return jourHistoryDAO.selectList(condition);
    }

    @Override
    public Long getTotalAmount(String bizType, String channelType,
            String accountNumber) {
        Jour condition = new Jour();
        condition.setBizType(bizType);
        condition.setChannelType(channelType);
        condition.setAccountNumber(accountNumber);
        long a = jourHistoryDAO.selectTotalAmount(condition);
        return Math.abs(a);
    }
}
