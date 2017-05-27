package com.std.account.ao.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.std.account.ao.IExchangeCurrencyAO;
import com.std.account.ao.IWeChatAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.IExchangeCurrencyBO;
import com.std.account.bo.IUserBO;
import com.std.account.bo.base.Paginable;
import com.std.account.common.PropertiesUtil;
import com.std.account.common.UserUtil;
import com.std.account.domain.Account;
import com.std.account.domain.ExchangeCurrency;
import com.std.account.domain.User;
import com.std.account.dto.res.BooleanRes;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EChannelType;
import com.std.account.enums.ECurrency;
import com.std.account.enums.EExchangeCurrencyStatus;
import com.std.account.enums.EJourBizType;
import com.std.account.enums.EPayType;
import com.std.account.enums.ESystemCode;
import com.std.account.exception.BizException;
import com.std.account.util.AmountUtil;
import com.std.account.util.CalculationUtil;

@Service
public class ExchangeCurrencyAOImpl implements IExchangeCurrencyAO {

    @Autowired
    private IUserBO userBO;

    @Autowired
    private IAccountBO accountBO;

    @Autowired
    private IExchangeCurrencyBO exchangeCurrencyBO;

    @Autowired
    private IWeChatAO weChatAO;

    @Override
    public Paginable<ExchangeCurrency> queryExchangeCurrencyPage(int start,
            int limit, ExchangeCurrency condition) {
        Paginable<ExchangeCurrency> page = exchangeCurrencyBO.getPaginable(
            start, limit, condition);
        if (page != null && CollectionUtils.isNotEmpty(page.getList())) {
            for (ExchangeCurrency exchangeCurrency : page.getList()) {
                User fromUser = userBO.getRemoteUser(exchangeCurrency
                    .getFromUserId());
                exchangeCurrency.setFromUser(fromUser);
                User toUser = userBO.getRemoteUser(exchangeCurrency
                    .getToUserId());
                exchangeCurrency.setToUser(toUser);
            }
        }
        return page;
    }

    @Override
    public ExchangeCurrency getExchangeCurrency(String code) {
        ExchangeCurrency exchangeCurrency = exchangeCurrencyBO
            .getExchangeCurrency(code);
        User fromUser = userBO.getRemoteUser(exchangeCurrency.getFromUserId());
        exchangeCurrency.setFromUser(fromUser);
        return exchangeCurrency;
    }

    @Override
    public Double getExchangeRate(String fromCurrency, String toCurrency) {
        return exchangeCurrencyBO.getExchangeRate(fromCurrency, toCurrency);
    }

    @Override
    public String applyExchange(String userId, Long fromAmount,
            String fromCurrency, String toCurrency) {
        User user = userBO.getRemoteUser(userId);
        Account account = accountBO.getAccountByUser(userId, fromCurrency);
        if (fromAmount > account.getAmount()) {
            new BizException("xn000000", "余额不足");
        }
        // 判断是否生成条件是否满足
        if (ESystemCode.ZHPAY.getCode().equals(user.getSystemCode())) {
            exchangeCurrencyBO.doCheckZH(userId, fromCurrency, toCurrency);
        }
        return exchangeCurrencyBO.applyExchange(user, fromAmount, fromCurrency,
            toCurrency);
    }

    @Override
    @Transactional
    public void approveExchange(String code, String approveResult,
            String approver, String approveNote) {
        ExchangeCurrency dbOrder = exchangeCurrencyBO.getExchangeCurrency(code);
        if (EExchangeCurrencyStatus.TO_PAY.getCode()
            .equals(dbOrder.getStatus())) {
            if (EBoolean.YES.getCode().equals(approveResult)) {
                exchangeCurrencyBO.approveExchangeYes(dbOrder, approver,
                    approveNote);
                // 开始资金划转
                String bizNote = CalculationUtil.divi(dbOrder.getFromAmount())
                        + dbOrder.getFromCurrency() + "虚拟币转化为"
                        + CalculationUtil.divi(dbOrder.getToAmount())
                        + dbOrder.getToCurrency();
                Account fromAccount = accountBO.getAccountByUser(
                    dbOrder.getFromUserId(), dbOrder.getFromCurrency());
                Account toAccount = accountBO.getAccountByUser(
                    dbOrder.getToUserId(), dbOrder.getToCurrency());

                accountBO.changeAmount(fromAccount.getAccountNumber(),
                    EChannelType.NBZ, null, null, code,
                    EJourBizType.EXCHANGE_CURRENCY,
                    EJourBizType.EXCHANGE_CURRENCY.getCode(),
                    -dbOrder.getFromAmount());
                accountBO.changeAmount(toAccount.getAccountNumber(),
                    EChannelType.NBZ, null, null, code,
                    EJourBizType.EXCHANGE_CURRENCY, bizNote,
                    dbOrder.getToAmount());
            } else {
                exchangeCurrencyBO.approveExchangeNo(dbOrder, approver,
                    approveNote);
            }
        } else {
            throw new BizException("xn000000", code + "不处于待审批状态");
        }
    }

    @Override
    @Transactional
    public void doTransfer(String fromUserId, String fromCurrency,
            String toUserId, String toCurrency, Long transAmount) {
        // 转化前提是否满足
        if (ECurrency.CNY.getCode().equals(toCurrency)) {
            throw new BizException("xn000000", "转化币种不能是人民币");
        }
        Account fromAccount = accountBO.getAccountByUser(fromUserId,
            fromCurrency);
        Account toAccount = accountBO.getAccountByUser(toUserId, toCurrency);
        Double rate = this.getExchangeRate(fromCurrency, toCurrency);
        Long toAmount = AmountUtil.mul(transAmount, rate);

        // 开始资金划转
        String bizNote = CalculationUtil.divi(transAmount)
                + ECurrency.getCurrencyMap().get(fromCurrency).getValue()
                + "转化为" + CalculationUtil.divi(toAmount)
                + ECurrency.getCurrencyMap().get(toCurrency).getValue();
        String code = exchangeCurrencyBO.saveExchange(fromUserId, transAmount,
            fromCurrency, toUserId, toAmount, toCurrency,
            fromAccount.getCompanyCode(), fromAccount.getSystemCode());
        accountBO.changeAmount(fromAccount.getAccountNumber(),
            EChannelType.NBZ, null, null, code, EJourBizType.Transfer_CURRENCY,
            bizNote, -transAmount);
        accountBO
            .changeAmount(toAccount.getAccountNumber(), EChannelType.NBZ, null,
                null, code, EJourBizType.Transfer_CURRENCY, bizNote, toAmount);
    }

    @Override
    @Transactional
    public Object payExchange(String fromUserId, String toUserId, Long amount,
            String currency, String payType) {
        Object result = null;
        User fromUser = userBO.getRemoteUser(fromUserId);
        // 获取微信公众号支付prepayid
        if (EPayType.RMB_YE.getCode().equals(payType)) {
            result = rmbYePay(fromUser, toUserId, amount, currency, payType);
        } else if (EPayType.WEIXIN_H5.getCode().equals(payType)) {
            result = weixinH5Pay(fromUser, toUserId, amount, currency, payType);
        } else if (EPayType.WEIXIN_NATIVE.getCode().equals(payType)) {
            result = weixinNativePay(fromUser, toUserId, amount, currency,
                payType);
        } else {
            throw new BizException("XN000000", "现只支持微信H5和微信二维码，其他方式不支持");
        }
        return result;
    }

    /**
    * 人民币余额购买虚拟币
    * @param user
    * @param amount
    * @param currency
    * @param payType
    * @return
    * @create: 2017年4月20日 下午6:02:46 xieyj
    * @history:
    */
    private Object rmbYePay(User fromUser, String toUser, Long amount,
            String currency, String payType) {
        EJourBizType bizType = null;
        if (ECurrency.CG_CGB.getCode().equals(currency)) {
            bizType = EJourBizType.CG_CGBGM;
        } else if (ECurrency.YC_CB.getCode().equals(currency)) {
            bizType = EJourBizType.YC_CBGM;
        } else {
            throw new BizException("xn000000", "币种未识别或不支持购买");
        }

        Long rmbAmount = AmountUtil.mulJinFen(amount, 1 / exchangeCurrencyBO
            .getExchangeRate(ECurrency.CNY.getCode(), currency));
        // 产生记录
        String code = exchangeCurrencyBO.saveExchange(fromUser.getUserId(),
            rmbAmount, ECurrency.CNY.getCode(), toUser, amount, currency,
            fromUser.getCompanyCode(), fromUser.getSystemCode());
        // 人民币划转
        accountBO.transAmountCZB(fromUser.getUserId(), ECurrency.CNY.getCode(),
            toUser, ECurrency.CNY.getCode(), rmbAmount, bizType,
            bizType.getValue(), UserUtil.getUserMobile(fromUser.getMobile())
                    + bizType.getValue(), code);
        // 购买的币种划转
        accountBO.transAmountCZB(toUser, currency, fromUser.getUserId(),
            currency, amount, bizType,
            UserUtil.getUserMobile(fromUser.getMobile()) + bizType.getValue(),
            bizType.getValue(), code);
        return new BooleanRes(true);
    }

    /**
    * 二维码扫描购买虚拟币
    * @param fromUserId
    * @param toUserId
    * @param amount
    * @param currency
    * @param payType
    * @param systemCode
    * @return
    * @create: 2017年4月20日 下午7:01:28 xieyj
    * @history:
    */
    private Object weixinNativePay(User fromUser, String toUserId, Long amount,
            String currency, String payType) {
        EJourBizType bizType = null;
        if (ECurrency.CG_CGB.getCode().equals(currency)) {
            bizType = EJourBizType.CG_CGBGM;
        } else if (ECurrency.YC_CB.getCode().equals(currency)) {
            bizType = EJourBizType.YC_CBGM;
        } else {
            throw new BizException("xn000000", "币种未识别或不支持购买");
        }

        Long rmbAmount = AmountUtil.mulJinFen(amount, 1 / exchangeCurrencyBO
            .getExchangeRate(ECurrency.CNY.getCode(), currency));
        String code = exchangeCurrencyBO.payExchange(fromUser.getUserId(),
            toUserId, rmbAmount, amount, currency, payType,
            fromUser.getSystemCode());

        return weChatAO.getPrepayIdNative(fromUser.getUserId(), toUserId, code,
            code, bizType.getCode(), bizType.getValue(), rmbAmount,
            PropertiesUtil.Config.SELF_PAY_BACKURL);
    }

    /**
    * 微信H5支付购买虚拟币
    * @param user
    * @param amount
    * @param currency
    * @param payType
    * @return
    * @create: 2017年4月20日 下午6:02:46 xieyj
    * @history:
    */
    private Object weixinH5Pay(User fromUser, String toUser, Long amount,
            String currency, String payType) {
        EJourBizType bizType = null;
        if (ECurrency.CG_CGB.getCode().equals(currency)) {
            bizType = EJourBizType.CG_CGBGM;
        } else if (ECurrency.YC_CB.getCode().equals(currency)) {
            bizType = EJourBizType.YC_CBGM;
        } else {
            throw new BizException("xn000000", "币种未识别或不支持购买");
        }

        Long rmbAmount = AmountUtil.mulJinFen(amount, 1 / exchangeCurrencyBO
            .getExchangeRate(ECurrency.CNY.getCode(), currency));
        String code = exchangeCurrencyBO.payExchange(fromUser.getUserId(),
            toUser, rmbAmount, amount, currency, payType,
            fromUser.getSystemCode());

        return weChatAO.getPrepayIdH5(fromUser.getUserId(),
            fromUser.getOpenId(), toUser, code, code, bizType.getCode(),
            bizType.getValue(), rmbAmount,
            PropertiesUtil.Config.SELF_PAY_BACKURL);
    }

    @Override
    @Transactional
    public void paySuccess(String payGroup, String payCode, Long transAmount) {
        List<ExchangeCurrency> resultList = exchangeCurrencyBO
            .queryExchangeCurrencyList(payGroup);
        if (CollectionUtils.isEmpty(resultList)) {
            throw new BizException("XN000000", "找不到对应的兑换记录");
        }
        ExchangeCurrency exchangeCurrency = resultList.get(0);
        if (!transAmount.equals(exchangeCurrency.getFromAmount())) {
            throw new BizException("XN000000", "金额校验错误，非正常调用");
        }
        // 更新状态
        exchangeCurrencyBO.paySuccess(exchangeCurrency.getCode(),
            EExchangeCurrencyStatus.PAYED.getCode(), payCode, transAmount);

        EJourBizType bizType = null;
        if (ECurrency.CG_CGB.getCode().equals(exchangeCurrency.getToCurrency())) {
            bizType = EJourBizType.CG_CGBGM;
        } else if (ECurrency.YC_CB.getCode().equals(
            exchangeCurrency.getToCurrency())) {
            bizType = EJourBizType.YC_CBGM;
        } else {
            throw new BizException("xn000000", "币种未识别或不支持购买");
        }

        User fromUser = userBO.getRemoteUser(exchangeCurrency.getFromUserId());

        // 购买的币种划转
        accountBO.transAmountCZB(exchangeCurrency.getToUserId(),
            exchangeCurrency.getToCurrency(), exchangeCurrency.getFromUserId(),
            exchangeCurrency.getToCurrency(), exchangeCurrency.getToAmount(),
            bizType,
            UserUtil.getUserMobile(fromUser.getMobile()) + bizType.getValue(),
            bizType.getValue(), exchangeCurrency.getCode());
    }
}
