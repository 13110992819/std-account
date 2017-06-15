/**
 * @Title IJourAO.java 
 * @Package com.std.account.ao 
 * @Description 
 * @author xieyj  
 * @date 2016年12月23日 下午9:05:07 
 * @version V1.0   
 */
package com.std.account.ao;

import java.util.List;

import com.std.account.annotation.ServiceModule;
import com.std.account.bo.base.Paginable;
import com.std.account.domain.Jour;
import com.std.account.dto.res.XN802901Res;

/** 
 * @author: xieyj 
 * @since: 2016年12月23日 下午9:05:07 
 * @history:
 */
@ServiceModule
public interface IJourAO {
    String DEFAULT_ORDER_COLUMN = "code";

    // 流水对账(包括现在和历史流水)
    public void checkJour(String code, Long checkAmount, String checkUser,
            String checkNote, String systemCode);

    public Paginable<Jour> queryJourPage(int start, int limit, Jour condition);

    public List<Jour> queryJourList(Jour condition);

    public Jour getJour(String code, String systemCode);

    public Long getTotalAmount(String bizType, String channelType,
            String accountNumber);

    /**
     * 获取一段时间统计金额
     * @param accountNumber
     * @param dateStart
     * @param dateEnd
     * @return 
     * @create: 2017年5月31日 下午8:46:22 xieyj
     * @history:
     */
    public XN802901Res getTotalAmountByDate(String accountNumber,
            String dateStart, String dateEnd);
}
