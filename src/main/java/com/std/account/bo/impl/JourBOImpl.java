/**
 * @Title AJourBOImpl.java 
 * @Package com.ibis.account.bo.impl 
 * @Description 
 * @author miyb  
 * @date 2015-3-15 下午3:22:07 
 * @version V1.0   
 */
package com.std.account.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.std.account.bo.IJourBO;
import com.std.account.bo.base.PaginableBOImpl;
import com.std.account.common.DateUtil;
import com.std.account.core.OrderNoGenerater;
import com.std.account.dao.IJourDAO;
import com.std.account.domain.Account;
import com.std.account.domain.HLOrder;
import com.std.account.domain.Jour;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EGeneratePrefix;
import com.std.account.enums.EJourBizType;
import com.std.account.enums.EJourStatus;
import com.std.account.exception.BizException;

/** 
 * @author: miyb 
 * @since: 2015-3-15 下午3:22:07 
 * @history:
 */
@Component
public class JourBOImpl extends PaginableBOImpl<Jour> implements IJourBO {
    @Autowired
    private IJourDAO jourDAO;

    @Override
    public String addJour(Account dbAccount, EChannelType channelType,
            String channelOrder, String payGroup, String refNo,
            EJourBizType bizType, String bizNote, Long transAmount) {
        if (!EChannelType.Offline.getCode().equals(channelType.getCode())
                && !EChannelType.NBZ.getCode().equals(channelType.getCode())) {// 线下和内部帐可为空，线上必须有
            if (StringUtils.isBlank(payGroup)) {// 必须要有的判断。每一次流水新增，必有有对应业务分组
                throw new BizException("xn000000", "新增流水业务分组不能为空");
            }
        }
        if (StringUtils.isBlank(refNo)) {// 必须要有的判断。每一次流水新增，必有有对应流水分组
            throw new BizException("xn000000", "新增流水流水分组不能为空");
        }
        if (transAmount == 0) {
            throw new BizException("xn000000", "新增流水变动金额不能为0");
        }
        String code = OrderNoGenerater
            .generate(EGeneratePrefix.AJour.getCode());

        Jour data = new Jour();
        data.setCode(code);

        data.setPayGroup(payGroup);
        data.setRefNo(refNo);
        data.setChannelOrder(channelOrder);// 内部转账时为空，外部转账时必定有
        data.setAccountNumber(dbAccount.getAccountNumber());
        data.setTransAmount(transAmount);

        data.setUserId(dbAccount.getUserId());
        data.setRealName(dbAccount.getRealName());
        data.setBizType(bizType.getCode());
        data.setBizNote(bizNote);
        data.setPreAmount(dbAccount.getAmount());
        data.setPostAmount(dbAccount.getAmount() + transAmount);

        data.setStatus(EJourStatus.todoCheck.getCode());
        data.setRemark("记得对账哦");
        data.setCreateDatetime(new Date());
        data.setWorkDate(DateUtil.dateToStr(new Date(),
            DateUtil.DB_DATE_FORMAT_STRING));

        data.setChannelType(channelType.getCode());
        data.setSystemCode(dbAccount.getSystemCode());
        data.setCompanyCode(dbAccount.getCompanyCode());
        jourDAO.insert(data);
        return code;
    }

    @Override
    public String addJourForHL(Account dbAccount, HLOrder order) {
        String code = OrderNoGenerater
            .generate(EGeneratePrefix.AJour.getCode());

        Jour data = new Jour();
        data.setCode(code);
        data.setAccountNumber(dbAccount.getAccountNumber());
        data.setUserId(dbAccount.getUserId());
        data.setRealName(dbAccount.getRealName());
        data.setChannelType(EChannelType.NBZ.getCode());

        data.setRefNo(order.getCode());
        data.setBizType(EJourBizType.AJ_HCLB.getCode());
        data.setBizNote("根据红蓝订单《" + order.getCode() + "》变动资金");
        data.setTransAmount(order.getAmount());
        data.setPreAmount(dbAccount.getAmount());

        data.setPostAmount(dbAccount.getAmount() + order.getAmount());
        data.setStatus(EJourStatus.noAdjust.getCode());
        data.setCreateDatetime(new Date());
        data.setWorkDate(null);
        data.setSystemCode(dbAccount.getSystemCode());
        data.setCompanyCode(dbAccount.getCompanyCode());
        jourDAO.insert(data);
        return code;

    }

    @Override
    public void doCheckJour(Jour jour, EBoolean checkResult, Long checkAmount,
            String checkUser, String checkNote) {
        Jour data = new Jour();
        data.setCode(jour.getCode());
        EJourStatus eJourStatus = EJourStatus.Checked_YES;
        if (EBoolean.NO.equals(checkResult)) {
            eJourStatus = EJourStatus.Checked_NO;
        }
        data.setStatus(eJourStatus.getCode());
        data.setCheckUser(checkUser);
        data.setCheckNote(checkNote + ":调整金额" + checkAmount / 1000);
        data.setCheckDatetime(new Date());
        jourDAO.checkJour(data);
    }

    @Override
    public void adjustJourNO(Jour jour, String adjustUser, String adjustNote) {
        Jour data = new Jour();
        data.setCode(jour.getCode());
        data.setStatus(EJourStatus.Checked_YES.getCode());
        data.setAdjustUser(adjustUser);
        data.setAdjustNote(adjustNote);
        data.setAdjustDatetime(new Date());
        jourDAO.adjustJour(data);
    }

    @Override
    public void adjustJourYES(Jour jour, String adjustUser, String adjustNote) {
        Jour data = new Jour();
        data.setCode(jour.getCode());
        data.setStatus(EJourStatus.Adjusted.getCode());
        data.setAdjustUser(adjustUser);
        data.setAdjustNote(adjustNote);
        data.setAdjustDatetime(new Date());
        jourDAO.adjustJour(data);
    }

    @Override
    public Jour getJour(String code, String systemCode) {
        Jour data = null;
        if (StringUtils.isNotBlank(code)) {
            Jour condition = new Jour();
            condition.setCode(code);
            condition.setSystemCode(systemCode);
            data = jourDAO.select(condition);
            if (data == null) {
                throw new BizException("xn000000", "单号不存在");
            }
        }
        return data;

    }

    @Override
    public List<Jour> queryJourList(Jour condition) {
        return jourDAO.selectList(condition);
    }

    @Override
    public Long getTotalAmount(String bizType, String channelType,
            String accountNumber) {
        Jour jour = new Jour();
        jour.setBizType(bizType);
        jour.setChannelType(channelType);
        jour.setAccountNumber(accountNumber);
        long a = jourDAO.selectTotalAmount(jour);
        return Math.abs(a);
    }
}
