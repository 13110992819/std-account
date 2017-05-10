package com.std.account.enums;

import java.util.HashMap;
import java.util.Map;

import com.std.account.exception.BizException;

/**
 * @author: xieyj 
 * @since: 2016年11月11日 上午10:09:32 
 * @history:
 */
public enum EJourBizType {
    // 每个系统的分布说明
    AJ_REG("01", "注册送积分"), AJ_SIGN("02", "每日签到"), AJ_CZ("11", "充值"), AJ_QX(
            "-11", "取现"), AJ_HCLB("HL", "红冲蓝补")

    , ZH_O2O("-ZH1", "正汇O2O支付")
    // 取现审批和兑换币种，产生记录为冻结流水，故我的流水中排除这些情况
    , EXCHANGE_CURRENCY("200", "币种兑换"), Transfer_CURRENCY("201", "同币种的划转"), AJ_CGBSM(
            "210", "采购币售卖"), CG_HB2CGB("211", "嗨币兑换菜狗币");

    public static EJourBizType getBizType(String code) {
        Map<String, EJourBizType> map = getBizTypeMap();
        EJourBizType result = map.get(code);
        if (result == null) {
            new BizException("XN0000", code + "对应的bizType不存在");
        }
        return result;
    }

    public static Map<String, EJourBizType> getBizTypeMap() {
        Map<String, EJourBizType> map = new HashMap<String, EJourBizType>();
        for (EJourBizType bizType : EJourBizType.values()) {
            map.put(bizType.getCode(), bizType);
        }
        return map;
    }

    EJourBizType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
