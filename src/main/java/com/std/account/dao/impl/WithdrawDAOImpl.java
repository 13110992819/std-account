package com.std.account.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.std.account.dao.IWithdrawDAO;
import com.std.account.dao.base.support.AMybatisTemplate;
import com.std.account.domain.Withdraw;

@Repository("withdrawDAOImpl")
public class WithdrawDAOImpl extends AMybatisTemplate implements IWithdrawDAO {

    @Override
    public int insert(Withdraw data) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(Withdraw data) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Withdraw select(Withdraw condition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long selectTotalCount(Withdraw condition) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Withdraw> selectList(Withdraw condition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Withdraw> selectList(Withdraw condition, int start, int count) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void approveOrder(Withdraw data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void payOrder(Withdraw data) {
        // TODO Auto-generated method stub

    }

}
