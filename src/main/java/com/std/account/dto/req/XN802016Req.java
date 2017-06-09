package com.std.account.dto.req;

/**
 * 列表查询银行卡
 * @author: asus 
 * @since: 2016年12月22日 下午7:28:32 
 * @history:
 */
public class XN802016Req {

    // 系统编号
    private String systemCode;

    // 银行卡号
    private String bankcardNumber;

    // 银行名称
    private String bankName;

    // 用户编号
    private String userId;

    // 用户姓名
    private String realName;

    // 类型
    private String type;

    // 状态(0 不可用 1可用)
    private String status;

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getBankcardNumber() {
        return bankcardNumber;
    }

    public void setBankcardNumber(String bankcardNumber) {
        this.bankcardNumber = bankcardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
