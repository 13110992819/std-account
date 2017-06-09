package com.std.account.dao;

import com.std.account.dao.base.IBaseDAO;
import com.std.account.domain.Jour;

/**
 * @author: xieyj 
 * @since: 2016年12月23日 上午11:25:21 
 * @history:
 */
public interface IJourHistoryDAO extends IBaseDAO<Jour> {
    String NAMESPACE = IJourHistoryDAO.class.getName().concat(".");

    public long selectTotalAmount(Jour data);
}
