package com.std.account.enums;

import java.util.HashMap;
import java.util.Map;

import com.std.account.exception.BizException;

//渠道类型分两大类：外部渠道和唯一的内部渠道（内部账）

public enum EChannelType {
    Yeepay_PC("11", "易宝支付-网关"), Yeepay_WAP("12", "易宝支付-WAP"), Baofoo_PC("13",
            "宝付支付-网关"), Baofoo_WAP("14", "宝付支付-WAP"), Fuiou_PC("15", "富友支付-网关"), Fuiou_WAP(
            "16", "富友支付-WAP"), Alipay("30", "支付宝APP支付"), WeChat_H5("35",
            "微信公众号支付"), WeChat_APP("36", "微信APP支付"), WeChat_NATIVE("37",
            "微信扫码支付"), BANK_PAY("40", "网银代付"), CMB("50", "招行银企直联"), Offline(
            "90", "人工线下")

    , NBZ("0", "内部账");

    public static Map<String, EChannelType> getChannelTypeResultMap() {
        Map<String, EChannelType> map = new HashMap<String, EChannelType>();
        for (EChannelType type : EChannelType.values()) {
            map.put(type.getCode(), type);
        }
        return map;
    }

    public static EChannelType getEChannelType(String code) {
        Map<String, EChannelType> map = getChannelTypeResultMap();
        EChannelType channelType = map.get(code);
        if (null == channelType) {
            throw new BizException("xn0000", code + "对应支付渠道类型不存在");
        }
        return channelType;
    }

    EChannelType(String code, String value) {
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
