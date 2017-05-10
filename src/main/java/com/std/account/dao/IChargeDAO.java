package com.std.account.dao;

import com.std.account.dao.base.IBaseDAO;
import com.std.account.domain.Charge;

public interface IChargeDAO extends IBaseDAO<Charge> {

    void payOrder(Charge data);

}
