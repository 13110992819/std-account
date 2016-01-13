/**
 * @Title Account.java 
 * @Package com.ibis.account.domain 
 * @Description 
 * @author miyb  
 * @date 2015-2-12 下午8:38:03 
 * @version V1.0   
 */
package com.std.account.domain;

import java.util.Date;

import com.std.account.dao.base.ABaseDO;

/** 
 * @author: miyb 
 * @since: 2015-2-12 下午8:38:03 
 * @history:
 */
public class Account extends ABaseDO {
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */
    private static final long serialVersionUID = 8322573358554172531L;

    // 创建起始时间
    private Date createDatetimeStart;

    // 创建终止时间
    private Date createDatetimeEnd;

    // -----db properties start---------------
    // 账号
    private String accountNumber;

    // 状态(0正常,1程序锁定,2人工锁定)
    private String status;

    // 账户余额(精确到厘,且永远大于等于0）
    private Long amount;

    // 冻结金额（精确到厘,且永远大于等于0）
    private Long frozenAmount;

    // 币种（默认CNY）
    private String currency;

    // MD5值
    private String md5;

    // 记录创建时间
    private Date createDatetime;

    // 记录更新时间
    private Date updateDatetime;

    // userid
    private String userId;

    // -----db properties end---------------

    // -----show properties start-----------
    // 手机号
    private String mobile;

    // 真实姓名
    private String realName;

    // -----show properties end-----------

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Long frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateDatetimeStart() {
        return createDatetimeStart;
    }

    public void setCreateDatetimeStart(Date createDatetimeStart) {
        this.createDatetimeStart = createDatetimeStart;
    }

    public Date getCreateDatetimeEnd() {
        return createDatetimeEnd;
    }

    public void setCreateDatetimeEnd(Date createDatetimeEnd) {
        this.createDatetimeEnd = createDatetimeEnd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
