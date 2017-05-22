package com.std.account.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.std.account.bo.IHLOrderBO;
import com.std.account.bo.base.PaginableBOImpl;
import com.std.account.core.OrderNoGenerater;
import com.std.account.dao.IHLOrderDAO;
import com.std.account.domain.Account;
import com.std.account.domain.HLOrder;
import com.std.account.domain.Jour;
import com.std.account.enums.EDirection;
import com.std.account.enums.EGeneratePrefix;
import com.std.account.enums.EHLOrderStatus;
import com.std.account.exception.BizException;

@Component
public class HLOrderBOImpl extends PaginableBOImpl<HLOrder> implements
        IHLOrderBO {
    @Autowired
    private IHLOrderDAO hlOrderDAO;

    @Override
    public String applyOrder(Account account, Jour jour, Long applyAmount,
            String applyUser, String applyNote) {
        if (applyAmount == 0) {
            throw new BizException("xn000000", "红蓝订单的变动金额不能为0");
        }
        String code = OrderNoGenerater.generate(EGeneratePrefix.HLORDER
            .getCode());
        HLOrder data = new HLOrder();
        data.setCode(code);
        data.setAccountNumber(account.getAccountNumber());
        data.setAccountName(account.getRealName());

        data.setCurrency(account.getCurrency());
        data.setJourCode(jour.getCode());
        data.setChannelType(jour.getChannelType());
        if (applyAmount > 0) {
            data.setDirection(EDirection.PLUS.getCode());
        } else {
            data.setDirection(EDirection.MINUS.getCode());
        }

        data.setAmount(applyAmount);
        data.setStatus(EHLOrderStatus.toApprove.getCode());
        data.setApplyUser(applyUser);
        data.setApplyNote(applyNote);
        data.setApplyDatetime(new Date());

        data.setSystemCode(account.getSystemCode());
        data.setCompanyCode(account.getCompanyCode());
        hlOrderDAO.insert(data);
        return code;
    }

    @Override
    public void approveOrder(HLOrder order, EHLOrderStatus status,
            String approveUser, String approveNote) {
        order.setStatus(status.getCode());
        order.setApproveUser(approveUser);
        order.setApproveNote(approveNote);
        order.setApproveDatetime(new Date());
        hlOrderDAO.approveOrder(order);
    }

    @Override
    public HLOrder getHLOrder(String code, String systemCode) {
        HLOrder order = null;
        if (StringUtils.isNotBlank(code)) {
            HLOrder condition = new HLOrder();
            condition.setCode(code);
            condition.setSystemCode(systemCode);
            order = hlOrderDAO.select(condition);
        }
        return order;
    }

    @Override
    public List<HLOrder> queryHLOrderList(HLOrder condition) {
        return hlOrderDAO.selectList(condition);
    }
}
