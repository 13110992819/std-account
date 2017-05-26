package com.std.account.enums;

import java.util.HashMap;
import java.util.Map;

import com.std.account.exception.BizException;

public enum EJourBizType {
    // 通用业务类型 每个系统的分布说明
    AJ_REG("01", "注册送积分"), AJ_SIGN("02", "每日签到"), AJ_CZ("11", "充值"), AJ_QX(
            "-11", "取现"), AJ_HCLB("HL", "红冲蓝补"), Transfer_CURRENCY("201",
            "同币种的划转"), EXCHANGE_CURRENCY("200", "币种兑换")

    // 各自系统特有的业务类型
    // 正汇
    , AJ_GW("-30", "购物"), AJ_GWTK("30", "购物退款"), AJ_QRSH("32", "确认收货，商户收钱"), AJ_GMZKQ(
            "-40", "购买折扣券"), ZH_O2O("-ZH1", "正汇O2O支付"), ZH_STOCK("-ZH2",
            "正汇分红权分红"), GD_MALL("GD_MALL", "积分商城消费"), GD_O2O("GD_O2O",
            "O2O店铺积分消费"), AJ_GMHZB("-50", "购买汇赚宝"), AJ_GMHZBFC("51", "购买汇赚宝分成"), AJ_YYJL(
            "52", "汇赚宝摇一摇奖励"), AJ_YYFC("53", "汇赚宝摇一摇分成"), AJ_YYFC_REF("54",
            "推荐人摇一摇分成"), AJ_FSDHB("60", "发送得红包"), AJ_LQHB("61", "领取红包"), AJ_DUOBAO(
            "-70", "参与小目标"), AJ_DUOBAO_PRIZE("71", "小目标中奖")
    // 城市网
    , CSW_PAY("100", "城市网商品积分购买支付")

    // 菜狗
    , CG_O2O_CGB("90", "菜狗O2O菜狗币支付"), AJ_CGBSM("210", "采购币售卖"), CG_HB2CGB(
            "211", "嗨币兑换菜狗币"), CG_O2O_CGBFD("91", "菜狗O2O菜狗币返点人民币"), CG_O2O_RMB(
            "92", "菜狗O2O人民币支付"), CG_O2O_CGJF("93", "菜狗O2O积分支付"), CG_O2O_RMBFD(
            "95", "菜狗O2O人民币支付返点菜狗币"), CG_XNCZ_P("94", "菜狗充值专区用款"), CG_XNCZ_M(
            "-94", "菜狗充值专区退款"), CG_CGBGM("CG_CGBGM", "菜狗币购买")

    // 定制通
    , AJ_GWFK("GW", "购物付款"), AJ_TK("GWTK", "购物退款"), AJ_HHRFC("HHRFC", "合伙人分成"), AJ_LTSFC(
            "LTSFC", "量体师分成")

    // 姚橙
    , YC_O2O_RMB("YC_O2O_RMB", "姚橙O2O人民币支付"), YC_O2O_RMBFD("YC_O2O_RMBFD",
            "姚橙O2O人民币支付返橙币"), YC_O2O_CB("YC_O2O_CB", "姚橙O2O橙币支付"), YC_O2O_CBFD(
            "YC_O2O_CBFD", "姚橙O2O橙币支付返人民币"), YC_MALL("YC_MALL", "姚橙商城购物支付"), YC_MALL_BACK(
            "YC_MALL_BACK", "姚橙商城购物退款"), YC_XNCZ_P("YC_XNCZ_P", "姚橙充值专区用款"), YC_XNCZ_M(
            "YC_XNCZ_M", "姚橙充值专区退款"), YC_CBGM("YC_CBGM", "橙币购买"), YC_SCB(
            "YC_SCB", "扫描二维码送橙币");
    ;

    public static EJourBizType getBizType(String code) {
        Map<String, EJourBizType> map = getBizTypeMap();
        EJourBizType result = map.get(code);
        if (result == null) {
            throw new BizException("XN0000", code + "对应的bizType不存在");
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
