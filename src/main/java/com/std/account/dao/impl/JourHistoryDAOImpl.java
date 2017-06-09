package com.std.account.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.std.account.dao.IJourHistoryDAO;
import com.std.account.dao.base.support.AMybatisTemplate;
import com.std.account.domain.Jour;

/**
 * @author: xieyj 
 * @since: 2016年12月23日 上午11:26:51 
 * @history:
 */
@Repository("jourHistoryDAOImpl")
public class JourHistoryDAOImpl extends AMybatisTemplate implements
        IJourHistoryDAO {

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#insert(java.lang.Object)
     */
    @Override
    public int insert(Jour data) {
        return super.insert(NAMESPACE.concat("insert_jourHistory"), data);
    }

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#delete(java.lang.Object)
     */
    @Override
    public int delete(Jour data) {
        return 0;
    }

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#select(java.lang.Object)
     */
    @Override
    public Jour select(Jour condition) {
        return super.select(NAMESPACE.concat("select_jourHistory"), condition,
            Jour.class);
    }

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#selectTotalCount(java.lang.Object)
     */
    @Override
    public long selectTotalCount(Jour condition) {
        return super.selectTotalCount(
            NAMESPACE.concat("select_jourHistory_count"), condition);
    }

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#selectList(java.lang.Object)
     */
    @Override
    public List<Jour> selectList(Jour condition) {
        return super.selectList(NAMESPACE.concat("select_jourHistory"),
            condition, Jour.class);
    }

    /** 
     * @see com.ibis.account.dao.base.IBaseDAO#selectList(java.lang.Object, int, int)
     */
    @Override
    public List<Jour> selectList(Jour condition, int start, int count) {
        return super.selectList(NAMESPACE.concat("select_jourHistory"), start,
            count, condition, Jour.class);
    }

    @Override
    public long selectTotalAmount(Jour data) {
        return super.selectTotalCount(NAMESPACE.concat("select_totalAmount"),
            data);
    }
}
