package com.std.account.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.std.account.dao.IChargeDAO;
import com.std.account.dao.base.support.AMybatisTemplate;
import com.std.account.domain.Charge;

@Repository("chargeDAOImpl")
public class ChargeDAOImpl extends AMybatisTemplate implements IChargeDAO {

    @Override
    public int insert(Charge data) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(Charge data) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Charge select(Charge condition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long selectTotalCount(Charge condition) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Charge> selectList(Charge condition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Charge> selectList(Charge condition, int start, int count) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void payOrder(Charge data) {
        // TODO Auto-generated method stub

    }

}
