package com.std.account.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.std.account.dao.IHLOrderDAO;
import com.std.account.dao.base.support.AMybatisTemplate;
import com.std.account.domain.HLOrder;

@Repository("hlOrderDAOImpl")
public class HLOrderDAOImpl extends AMybatisTemplate implements IHLOrderDAO {

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#insert(java.lang.Object)
     */
    @Override
    public int insert(HLOrder data) {
        return super.insert(NAMESPACE.concat("insert_hlOrder"), data);
    }

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#delete(java.lang.Object)
     */
    @Override
    public int delete(HLOrder data) {
        return 0;
    }

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#select(java.lang.Object)
     */
    @Override
    public HLOrder select(HLOrder condition) {
        return super.select(NAMESPACE.concat("select_hlOrder"), condition,
            HLOrder.class);
    }

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#selectTotalCount(java.lang.Object)
     */
    @Override
    public long selectTotalCount(HLOrder condition) {
        return super.selectTotalCount(NAMESPACE.concat("select_hlOrder_count"),
            condition);
    }

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#selectList(java.lang.Object)
     */
    @Override
    public List<HLOrder> selectList(HLOrder condition) {
        return super.selectList(NAMESPACE.concat("select_hlOrder"), condition,
            HLOrder.class);
    }

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#selectList(java.lang.Object, int, int)
     */
    @Override
    public List<HLOrder> selectList(HLOrder condition, int start, int count) {
        return super.selectList(NAMESPACE.concat("select_hlOrder"), start,
            count, condition, HLOrder.class);
    }

    @Override
    public void approveOrder(HLOrder data) {
        super.update(NAMESPACE.concat("approve_order"), data);
    }
}
