package com.std.account.bo;

import java.util.List;

import com.std.account.bo.base.IPaginableBO;
import com.std.account.domain.ExchangeCurrency;
import com.std.account.domain.User;

/**
 * @author: xieyj 
 * @since: 2017年3月30日 下午1:01:17 
 * @history:
 */
public interface IExchangeCurrencyBO extends IPaginableBO<ExchangeCurrency> {

    public List<ExchangeCurrency> queryExchangeCurrencyList(
            ExchangeCurrency condition);

    public List<ExchangeCurrency> queryExchangeCurrencyList(String payGroup);

    public ExchangeCurrency getExchangeCurrency(String code);

    // 1fromCurrency=多少toCurrency
    public Double getExchangeRate(String fromCurrency, String toCurrency);

    public String applyExchange(User user, Long fromAmount,
            String fromCurrency, String toCurrency);

    public void approveExchangeYes(ExchangeCurrency dbOrder, String approver,
            String approveNote);

    public void approveExchangeNo(ExchangeCurrency dbOrder, String approver,
            String approveNote);

    public Object payExchange(User user, Long amount, String currency,
            String payType);

    public int paySuccess(String code, String status, String payCode,
            Long payAmount);

    public ExchangeCurrency doExchange(User user, Long fromAmount,
            String fromCurrency, String toCurrency);

}
