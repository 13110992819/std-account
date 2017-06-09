package com.std.account.ao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.std.account.ao.IJourAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.IHLOrderBO;
import com.std.account.bo.IJourBO;
import com.std.account.bo.IJourHistoryBO;
import com.std.account.bo.base.Paginable;
import com.std.account.common.DateUtil;
import com.std.account.domain.Account;
import com.std.account.domain.Jour;
import com.std.account.dto.res.XN802901Res;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EJourBizType;
import com.std.account.enums.EJourStatus;
import com.std.account.exception.BizException;

/** 
 * @author: xieyj 
 * @since: 2016年12月23日 下午9:16:58 
 * @history:
 */
@Service
public class JourAOImpl implements IJourAO {

    @Autowired
    private IJourBO jourBO;

    @Autowired
    private IJourHistoryBO jourHistoryBO;

    @Autowired
    private IAccountBO accountBO;

    @Autowired
    private IHLOrderBO hlOrderBO;

    /*
     * 人工调账： 1、判断流水账是否平，平则更改订单状态，不平则更改产生红冲蓝补订单，而后更改订单状态
     */
    @Override
    @Transactional
    public void checkJour(String code, Long checkAmount, String checkUser,
            String checkNote, String systemCode) {
        Jour jour = jourBO.getJour(code, systemCode);
        if (!EJourStatus.todoCheck.getCode().equals(jour.getStatus())) {
            throw new BizException("xn000000", "该流水<" + code + ">不处于待对账状态");
        }
        if (checkAmount != 0) {
            Account account = accountBO.getAccount(jour.getAccountNumber());
            hlOrderBO.applyOrder(account, jour, checkAmount, checkUser,
                checkNote);
            jourBO.doCheckJour(jour, EBoolean.NO, checkAmount, checkUser,
                checkNote);
        } else {
            jourBO.doCheckJour(jour, EBoolean.YES, checkAmount, checkUser,
                checkNote);
        }
    }

    @Override
    public Paginable<Jour> queryJourPage(int start, int limit, Jour condition) {
        String bizType = condition.getBizType();
        if (StringUtils.isNotBlank(bizType)) {
            String[] bizTypeArrs = bizType.split(",");
            List<String> bizTypeList = new ArrayList<String>();
            for (int i = 0; i < bizTypeArrs.length; i++) {
                bizTypeList.add(bizTypeArrs[i]);
            }
            condition.setBizType(null);
            condition.setBizTypeList(bizTypeList);
        }
        return jourBO.getPaginable(start, limit, condition);
    }

    @Override
    public List<Jour> queryJourList(Jour condition) {
        String bizType = condition.getBizType();
        if (StringUtils.isNotBlank(bizType)) {
            String[] bizTypeArrs = bizType.split(",");
            List<String> bizTypeList = new ArrayList<String>();
            for (int i = 0; i < bizTypeArrs.length; i++) {
                bizTypeList.add(bizTypeArrs[i]);
            }
            condition.setBizType(null);
            condition.setBizTypeList(bizTypeList);
        }
        List<Jour> jourList = jourBO.queryJourList(condition);
        List<Jour> jourHistoryList = jourHistoryBO.queryJourList(condition);
        List<Jour> result = new ArrayList<Jour>();
        result.addAll(jourList);
        result.addAll(jourHistoryList);
        return result;
    }

    @Override
    public Jour getJour(String code, String systemCode) {
        return jourBO.getJour(code, systemCode);
    }

    @Override
    public Long getTotalAmount(String bizType, String channelType,
            String accountNumber) {
        return jourBO.getTotalAmount(bizType, channelType, accountNumber);
    }

    @Override
    public XN802901Res getTotalAmountByDate(String accountNumber,
            String dateStart, String dateEnd) {
        Jour condition = new Jour();
        condition.setAccountNumber(accountNumber);
        condition.setCreateDatetimeStart(DateUtil
            .getFrontDate(dateStart, false));
        condition.setCreateDatetimeEnd(DateUtil.getFrontDate(dateEnd, true));

        List<Jour> jourList = jourBO.queryJourList(condition);
        Long incomeAmount = 0L;// 收入金额
        Long withdrawAmount = 0L;// 取现金额
        for (Jour jour : jourList) {
            Long transAmount = jour.getTransAmount();
            if (transAmount > 0
                    && !EJourBizType.AJ_QX.getCode().equals(jour.getBizType())) {// 取现解冻排除
                incomeAmount += transAmount;
            }
            if (EJourBizType.AJ_QX.getCode().equals(jour.getBizType())) {
                withdrawAmount += transAmount;
            }
        }
        return new XN802901Res(incomeAmount, -withdrawAmount);
    }
}
