package com.std.account.dto.req;

/**
 * @author: xieyj 
 * @since: 2017年5月31日 下午7:51:02 
 * @history:
 */
public class XN802901Req {

    // 账户名称（必填）
    private String accountNumber;

    // 开始时间起（必填）
    private String dateStart;

    // 开始时间止（必填）
    private String dateEnd;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
