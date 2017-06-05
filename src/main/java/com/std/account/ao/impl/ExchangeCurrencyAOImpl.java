package com.std.account.ao.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.std.account.ao.IExchangeCurrencyAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.IExchangeCurrencyBO;
import com.std.account.bo.ISYSConfigBO;
import com.std.account.bo.IUserBO;
import com.std.account.bo.base.Paginable;
import com.std.account.common.SysConstant;
import com.std.account.domain.Account;
import com.std.account.domain.ExchangeCurrency;
import com.std.account.domain.User;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EChannelType;
import com.std.account.enums.ECurrency;
import com.std.account.enums.EExchangeCurrencyStatus;
import com.std.account.enums.EJourBizType;
import com.std.account.enums.ESystemCode;
import com.std.account.enums.EUserKind;
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
    private ISYSConfigBO sysConfigBO;

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
        if (!ECurrency.CNY.getCode().equals(toCurrency)) {
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
    public void doTransferC2CByZhFR(String fromUserId, String toMobile,
            Long transAmount, String tradePwd) {
        if (transAmount <= 0) {
            throw new BizException("xn000000", "划转金额需大于零");
        }
        String transAmountBsValue = sysConfigBO.getSYSConfig(
            SysConstant.TRANSAMOUNTBS, ESystemCode.ZHPAY.getCode());
        if (StringUtils.isNotBlank(transAmountBsValue)) {
            // 转账金额倍数
            Long transAmountBs = AmountUtil.mul(1000L,
                Double.valueOf(transAmountBsValue));
            if (transAmountBs > 0 && transAmount % transAmountBs > 0) {
                throw new BizException("xn000000", "请取" + transAmountBsValue
                        + "的倍数");
            }
        }
        // 验证交易密码
        userBO.checkTradePwd(fromUserId, tradePwd);
        // 验证双方是否C端用户
        User fromUser = userBO.getRemoteUser(fromUserId);
        if (!EUserKind.F1.getCode().equals(fromUser.getKind())) {
            throw new BizException("xn000000", "当前划转用户不是C端用户，不能进行转账业务");
        }
        String toUserId = userBO.isUserExist(toMobile, EUserKind.F1,
            fromUser.getSystemCode());
        // 同一个用户不可以相互转账
        if (toUserId.equals(fromUser.getUserId())) {
            throw new BizException("xn000000", "不能给自己转账");
        }

        // 开始资金划转
        String currency = ECurrency.ZH_FRB.getCode();
        Account fromAccount = accountBO.getAccountByUser(fromUserId, currency);
        Account toAccount = accountBO.getAccountByUser(toUserId, currency);
        String bizNote = fromUser.getMobile() + "用户转账" + toMobile + "用户分润"
                + CalculationUtil.divi(transAmount);

        String code = exchangeCurrencyBO.saveExchange(fromUserId, transAmount,
            currency, toUserId, transAmount, currency,
            fromAccount.getCompanyCode(), fromAccount.getSystemCode());

        accountBO.changeAmount(fromAccount.getAccountNumber(),
            EChannelType.NBZ, null, null, code,
            EJourBizType.Transfer_CURRENCY_C2C, bizNote, -transAmount);
        accountBO.changeAmount(toAccount.getAccountNumber(), EChannelType.NBZ,
            null, null, code, EJourBizType.Transfer_CURRENCY_C2C, bizNote,
            transAmount);
    }
}
