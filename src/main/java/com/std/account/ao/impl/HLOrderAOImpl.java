package com.std.account.ao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.std.account.ao.IHLOrderAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.IHLOrderBO;
import com.std.account.bo.IJourBO;
import com.std.account.bo.IJourHistoryBO;
import com.std.account.bo.base.Paginable;
import com.std.account.domain.HLOrder;
import com.std.account.domain.Jour;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EHLOrderStatus;
import com.std.account.enums.EJourStatus;
import com.std.account.exception.BizException;

@Service
public class HLOrderAOImpl implements IHLOrderAO {
    @Autowired
    private IAccountBO accountBO;

    @Autowired
    private IHLOrderBO hlOrderBO;

    @Autowired
    private IJourBO jourBO;

    @Autowired
    private IJourHistoryBO jourHistoryBO;

    @Override
    @Transactional
    public void approveOrder(String code, String adjustResult,
            String approveUser, String approveNote, String systemCode) {
        HLOrder order = hlOrderBO.getHLOrder(code, systemCode);
        Jour jour = jourBO.getJourNotException(order.getJourCode(),
            order.getSystemCode());
        if (null != jour) {
            approveOrderNow(order, jour, adjustResult, approveUser, approveNote);// 现在流水调账
        } else {
            jour = jourHistoryBO.getJour(order.getJourCode(), systemCode);
            approveOrderHistory(order, jour, adjustResult, approveUser,
                approveNote);// 历史流水调账
        }
    }

    private void approveOrderNow(HLOrder order, Jour jour, String adjustResult,
            String approveUser, String approveNote) {
        if (!EHLOrderStatus.toApprove.getCode().equals(order.getStatus())) {
            throw new BizException("xn000000", "该单号不处于调账待审核状态");
        }
        if (!EJourStatus.Checked_NO.getCode().equals(jour.getStatus())) {
            throw new BizException("xn000000", "该流水不处于调账待审核状态");
        }
        if (EBoolean.YES.getCode().equals(adjustResult)) {
            hlOrderBO.approveOrder(order, EHLOrderStatus.Approved_YES,
                approveUser, approveNote);
            jourBO.adjustJourYES(jour, approveUser, approveNote);
            accountBO.changeAmountForHL(order);
        } else {
            hlOrderBO.approveOrder(order, EHLOrderStatus.Approved_NO,
                approveUser, approveNote);
            jourBO.adjustJourNO(jour, approveUser, approveNote);
        }
    }

    private void approveOrderHistory(HLOrder order, Jour jour,
            String adjustResult, String approveUser, String approveNote) {
        if (!EHLOrderStatus.toApprove.getCode().equals(order.getStatus())) {
            throw new BizException("xn000000", "该单号不处于调账待审核状态");
        }
        if (!EJourStatus.Checked_NO.getCode().equals(jour.getStatus())) {
            throw new BizException("xn000000", "该流水不处于调账待审核状态");
        }
        if (EBoolean.YES.getCode().equals(adjustResult)) {
            hlOrderBO.approveOrder(order, EHLOrderStatus.Approved_YES,
                approveUser, approveNote);
            jourHistoryBO.adjustJourYES(jour, approveUser, approveNote);
            accountBO.changeAmountForHL(order);
        } else {
            hlOrderBO.approveOrder(order, EHLOrderStatus.Approved_NO,
                approveUser, approveNote);
            jourHistoryBO.adjustJourNO(jour, approveUser, approveNote);
        }
    }

    @Override
    public Paginable<HLOrder> queryHLOrderPage(int start, int limit,
            HLOrder condition) {
        return hlOrderBO.getPaginable(start, limit, condition);
    }

    @Override
    public List<HLOrder> queryHLOrderList(HLOrder condition) {
        return hlOrderBO.queryHLOrderList(condition);
    }

    @Override
    public HLOrder getHLOrder(String code, String systemCode) {
        HLOrder hLOrder = hlOrderBO.getHLOrder(code, systemCode);
        Jour jour = jourBO.getJourNotException(hLOrder.getJourCode(),
            systemCode);
        if (null == jour) {
            jour = jourHistoryBO.getJour(hLOrder.getJourCode(), systemCode);
        }
        List<Jour> jourList = new ArrayList<Jour>();
        jourList.add(jour);
        hLOrder.setJourList(jourList);
        return hLOrder;
    }
}
