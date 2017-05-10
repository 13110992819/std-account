package com.std.account.dao;

import com.std.account.dao.base.IBaseDAO;
import com.std.account.domain.HLOrder;

public interface IHLOrderDAO extends IBaseDAO<HLOrder> {

    void approveOrder(HLOrder order);

}
