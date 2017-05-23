package com.std.account.ao.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.std.account.ao.IBankcardAO;
import com.std.account.bo.IBankcardBO;
import com.std.account.bo.base.Paginable;
import com.std.account.domain.Bankcard;
import com.std.account.exception.BizException;

/**
 * @author: asus 
 * @since: 2016年12月22日 下午5:35:09 
 * @history:
 */
@Service
public class BankcardAOImpl implements IBankcardAO {

    @Autowired
    private IBankcardBO bankcardBO;

    @Override
    public synchronized String addBankcard(Bankcard data) {
        // 判断卡号是否重复
        List<Bankcard> list = bankcardBO.queryBankcardList(data.getUserId(),
            data.getSystemCode());
        // 新增时
        if (CollectionUtils.isNotEmpty(list)) {
            throw new BizException("xn0000", "您已绑定银行卡,无需绑定多张");
        }
        return bankcardBO.saveBankcard(data);
    }

    @Override
    public int editBankcard(Bankcard data) {
        Bankcard bankcard = bankcardBO.getBankcard(data.getCode());
        // 户名有传就修改，不传不修改
        if (StringUtils.isBlank(data.getRealName())) {
            data.setRealName(bankcard.getRealName());
        }
        if (!bankcard.getBankcardNumber().equals(data.getBankcardNumber())) { // 有修改就去判断是否唯一
            List<Bankcard> list = bankcardBO.queryBankcardList(
                bankcard.getUserId(), bankcard.getSystemCode());
            for (Bankcard card : list) {
                if (data.getBankcardNumber().equals(card.getBankcardNumber())) {
                    throw new BizException("xn0000", "银行卡号已存在");
                }
            }
        }
        return bankcardBO.refreshBankcard(data);
    }

    @Override
    public int dropBankcard(String code) {
        if (!bankcardBO.isBankcardExist(code)) {
            throw new BizException("xn0000", "记录编号不存在");
        }
        return bankcardBO.removeBankcard(code);
    }

    @Override
    public Paginable<Bankcard> queryBankcardPage(int start, int limit,
            Bankcard condition) {
        return bankcardBO.getPaginable(start, limit, condition);
    }

    @Override
    public List<Bankcard> queryBankcardList(Bankcard condition) {
        return bankcardBO.queryBankcardList(condition);
    }

    @Override
    public Bankcard getBankcard(String code) {
        return bankcardBO.getBankcard(code);
    }
}
