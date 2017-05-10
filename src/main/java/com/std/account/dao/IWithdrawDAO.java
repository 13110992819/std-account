package com.std.account.dao;

import com.std.account.dao.base.IBaseDAO;
import com.std.account.domain.Withdraw;

public interface IWithdrawDAO extends IBaseDAO<Withdraw> {

    void approveOrder(Withdraw data);

    void payOrder(Withdraw data);

}
