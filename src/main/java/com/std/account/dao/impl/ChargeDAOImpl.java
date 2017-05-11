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
        return super.insert(NAMESPACE.concat("insert_charge"), data);
    }

    @Override
    public int delete(Charge data) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Charge select(Charge condition) {
        return super.select(NAMESPACE.concat("select_charge"), condition,
            Charge.class);
    }

    @Override
    public long selectTotalCount(Charge condition) {
        return super.selectTotalCount(NAMESPACE.concat("select_charge_count"),
            condition);
    }

    @Override
    public List<Charge> selectList(Charge condition) {
        return super.selectList(NAMESPACE.concat("select_charge"), condition,
            Charge.class);
    }

    @Override
    public List<Charge> selectList(Charge condition, int start, int count) {
        return super.selectList(NAMESPACE.concat("select_charge"), start,
            count, condition, Charge.class);
    }

    @Override
    public void payOrder(Charge data) {
        super.update(NAMESPACE.concat("pay_order"), data);
    }
}
