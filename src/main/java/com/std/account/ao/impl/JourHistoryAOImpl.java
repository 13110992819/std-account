package com.std.account.ao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.std.account.ao.IJourHistoryAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.IHLOrderBO;
import com.std.account.bo.IJourHistoryBO;
import com.std.account.bo.base.Paginable;
import com.std.account.common.DateUtil;
import com.std.account.domain.Jour;
import com.std.account.exception.BizException;

/** 
 * @author: xieyj 
 * @since: 2016年12月23日 下午9:16:58 
 * @history:
 */
@Service
public class JourHistoryAOImpl implements IJourHistoryAO {

    @Autowired
    private IJourHistoryBO jourHistoryBO;

    @Autowired
    private IAccountBO accountBO;

    @Autowired
    private IHLOrderBO hlOrderBO;

    @Override
    public Paginable<Jour> queryJourPage(int start, int limit, Jour condition) {
        String bizType = condition.getBizType();
        if (StringUtils.isNotBlank(bizType)) {
            String[] bizTypeArrs = bizType.split(",");
            List<String> bizTypeList = new ArrayList<String>();
            for (int i = 0; i < bizTypeArrs.length; i++) {
                bizTypeList.add(bizTypeArrs[i]);
            }
            condition.setBizType(null);
            condition.setBizTypeList(bizTypeList);
        }
        return jourHistoryBO.getPaginable(start, limit, condition);
    }

    @Override
    public Paginable<Jour> queryFrontJourPage(int start, int limit,
            Jour condition) {
        if (DateUtil.daysBetween(condition.getCreateDatetimeStart(),
            condition.getCreateDatetimeEnd()) > 7) {
            throw new BizException("xn702000", "请选择7天内查询时间");
        }
        if (DateUtil.daysBetween(condition.getCreateDatetimeEnd(),
            DateUtil.getTodayStart()) < 7) {
            throw new BizException("xn702000", "结束时间请选择今天后7天时间");
        }
        String bizType = condition.getBizType();
        if (StringUtils.isNotBlank(bizType)) {
            String[] bizTypeArrs = bizType.split(",");
            List<String> bizTypeList = new ArrayList<String>();
            for (int i = 0; i < bizTypeArrs.length; i++) {
                bizTypeList.add(bizTypeArrs[i]);
            }
            condition.setBizType(null);
            condition.setBizTypeList(bizTypeList);
        }
        return jourHistoryBO.getPaginable(start, limit, condition);
    }

    @Override
    public Jour getJour(String code, String systemCode) {
        return jourHistoryBO.getJour(code, systemCode);
    }
}
