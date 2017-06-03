package com.std.account.dto.res;

/** 
 * @author: haiqingzheng 
 * @since: 2017年5月18日 下午7:28:09 
 * @history:
 */
public class XN802901Res {
    // 收入金额
    private Long incomeAmount;

    // 取现金额
    private Long withdrawAmount;

    public XN802901Res(Long incomeAmount, Long withdrawAmount) {
        super();
        this.incomeAmount = incomeAmount;
        this.withdrawAmount = withdrawAmount;
    }

    public Long getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Long incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Long getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(Long withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }
}
