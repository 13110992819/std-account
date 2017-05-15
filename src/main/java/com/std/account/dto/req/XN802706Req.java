package com.std.account.dto.req;

/**
 * 查询充值订单详情
 * @author: xieyj 
 * @since: 2017年5月12日 上午10:00:44 
 * @history:
 */
public class XN802706Req {
    private String code;

    // 系统编号
    private String systemCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

}
