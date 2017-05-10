package com.std.account.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.std.account.dao.IHLOrderDAO;
import com.std.account.dao.base.support.AMybatisTemplate;
import com.std.account.domain.HLOrder;

@Repository("hlOrderDAOImpl")
public class HLOrderDAOImpl extends AMybatisTemplate implements IHLOrderDAO {

    @Override
    public int insert(HLOrder data) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(HLOrder data) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public HLOrder select(HLOrder condition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long selectTotalCount(HLOrder condition) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<HLOrder> selectList(HLOrder condition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<HLOrder> selectList(HLOrder condition, int start, int count) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void approveOrder(HLOrder order) {
        // TODO Auto-generated method stub

    }

}
