package com.std.account.dao;

import com.std.account.dao.base.IBaseDAO;
import com.std.account.domain.Charge;

public interface IChargeDAO extends IBaseDAO<Charge> {
    String NAMESPACE = IChargeDAO.class.getName().concat(".");

    void payOrder(Charge data);
}
