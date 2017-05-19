/**
 * @Title XN802900Res.java 
 * @Package com.std.account.dto.res 
 * @Description 
 * @author leo(haiqing)  
 * @date 2017年5月18日 下午7:28:09 
 * @version V1.0   
 */
package com.std.account.dto.res;

/** 
 * @author: haiqingzheng 
 * @since: 2017年5月18日 下午7:28:09 
 * @history:
 */
public class XN802900Res {
    public XN802900Res(Long totalAmount) {
        super();
        this.totalAmount = totalAmount;
    }

    private Long totalAmount;

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }
}
