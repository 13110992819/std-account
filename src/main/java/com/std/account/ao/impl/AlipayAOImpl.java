/**
 * @Title AlipayAOImpl.java 
 * @Package com.std.account.ao.impl 
 * @Description 
 * @author haiqingzheng  
 * @date 2017年1月11日 下午8:56:56 
 * @version V1.0   
 */
package com.std.account.ao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.WebUtils;
import com.std.account.ao.IAlipayAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.IAlipayBO;
import com.std.account.bo.IChargeBO;
import com.std.account.bo.ICompanyChannelBO;
import com.std.account.common.DateUtil;
import com.std.account.common.JsonUtil;
import com.std.account.common.PropertiesUtil;
import com.std.account.domain.Account;
import com.std.account.domain.CallbackResult;
import com.std.account.domain.CompanyChannel;
import com.std.account.dto.res.XN002510Res;
import com.std.account.enums.EChannelType;
import com.std.account.enums.ECurrency;
import com.std.account.enums.EJourBizType;
import com.std.account.exception.BizException;
import com.std.account.util.alipay.AlipayConfig;
import com.std.account.util.alipay.AlipayCore;

/** 
 * @author: haiqingzheng 
 * @since: 2017年1月11日 下午8:56:56 
 * @history:
 */
@Service
public class AlipayAOImpl implements IAlipayAO {
    static Logger logger = Logger.getLogger(AlipayAOImpl.class);

    public static final String CHARSET = "utf-8";

    @Autowired
    ICompanyChannelBO companyChannelBO;

    @Autowired
    IAccountBO accountBO;

    @Autowired
    IAlipayBO alipayBO;

    @Autowired
    IChargeBO chargeBO;

    // 配置说明
    // channel_company —— 卖家支付宝用户号
    // private_key1 —— APP_PRIVATE_KEY，开发者应用私钥，由开发者自己生成
    // private_key2 —— ALIPAY_PUBLIC_KEY，支付宝公钥，由支付宝生成
    // private_key3 —— APP_ID，APPID即创建应用后生成
    @Override
    public Object getSignedOrder(String applyUser, String toUser,
            String payGroup, String refNo, String bizType, String bizNote,
            Long transAmount, String backUrl) {
        if (transAmount.longValue() == 0l) {
            throw new BizException("xn000000", "发生金额为零，不能使用支付宝支付");
        }
        // 获取来去方账户信息
        Account toAccount = accountBO.getAccountByUser(toUser,
            ECurrency.CNY.getCode());
        // 落地付款方和收款方流水信息
        String chargeOrderCode = chargeBO.applyOrderOnline(toAccount, payGroup,
            refNo, EJourBizType.getBizType(bizType), bizNote, transAmount,
            EChannelType.Alipay, applyUser);

        // 获取支付宝支付配置参数
        String systemCode = toAccount.getSystemCode();
        String companyCode = toAccount.getCompanyCode();
        CompanyChannel companyChannel = companyChannelBO.getCompanyChannel(
            companyCode, systemCode, EChannelType.Alipay.getCode());

        // 生成业务参数(bizContent)json字符串
        String bizContentJson = getBizContentJson(bizNote, chargeOrderCode,
            transAmount, backUrl);

        // 1、按照key=value&key=value方式拼接的未签名原始字符串
        Map<String, String> unsignedParamMap = getUnsignedParamMap(
            companyChannel.getPrivateKey3(), bizContentJson);
        // 注意注意：获取所有请求参数，不包括字节类型参数，如文件、字节流，剔除sign字段，剔除值为空的参数，
        // 并按照第一个字符的键值ASCII码递增排序（字母升序排序），如果遇到相同字符则按照第二个字符的键值ASCII码递增排序，以此类推。
        String unsignedContent = AlipayCore.createLinkString(unsignedParamMap);
        logger.info("*****未签名原始字符串：*****\n" + unsignedContent);

        // 2、对原始字符串进行签名
        String sign = getSign(unsignedContent, companyChannel.getPrivateKey1());
        logger.info("*****签名成功：*****\n" + sign);

        // 3、对请求字符串的所有一级value（biz_content作为一个value）进行encode
        String encodedParams = getEncodedparam(unsignedParamMap);
        encodedParams = encodedParams + "&sign=" + WebUtils.encode(sign);
        logger.info("*****签名并Encode后的请求字符串*****\n" + encodedParams);

        XN002510Res res = new XN002510Res();
        res.setPayCode(chargeOrderCode);
        res.setSignOrder(encodedParams);
        return res;
    }

    private String getEncodedparam(Map<String, String> unsignedParamMap) {
        Map<String, String> encodedParamMap = new HashMap<String, String>();
        Set<String> keys = unsignedParamMap.keySet();
        for (String key : keys) {
            encodedParamMap
                .put(key, WebUtils.encode(unsignedParamMap.get(key)));
        }
        return AlipayCore.createLinkString(encodedParamMap);
    }

    private String getSign(String content, String privateKey) {
        String sign = null;
        try {
            sign = AlipaySignature.rsaSign(content, privateKey,
                AlipayConfig.input_charset, AlipayConfig.sign_type);
            if (sign == null) {
                throw new BizException("xn000000", "原始字符串签名失败");
            }
            return sign;
        } catch (AlipayApiException e) {
            throw new BizException("xn000000", "原始字符串签名出错");
        }

    }

    private Map<String, String> getUnsignedParamMap(String appId,
            String bizContentJson) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("app_id", appId);
        paramMap.put("method", "alipay.trade.app.pay");
        paramMap.put("format", "JSON");
        paramMap.put("charset", CHARSET);
        paramMap.put("sign_type", "RSA2");
        paramMap.put("timestamp",
            DateUtil.dateToStr(new Date(), DateUtil.DATA_TIME_PATTERN_1));
        paramMap.put("version", "1.0");
        paramMap.put("notify_url", PropertiesUtil.Config.ALIPAY_APP_BACKURL);
        paramMap.put("biz_content", bizContentJson);
        return paramMap;
    }

    private String getBizContentJson(String fromBizNote, String jourCode,
            Long transAmount, String backUrl) {
        Map<String, String> bizParams = new HashMap<String, String>();
        bizParams.put("subject", fromBizNote); // 商品的标题 例如：大乐透
        bizParams.put("out_trade_no", jourCode); // 商户网站唯一订单号
        bizParams.put("total_amount", String.valueOf(transAmount / 1000.00)); // 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        bizParams.put("product_code", "QUICK_MSECURITY_PAY"); // 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        bizParams.put("passback_params", backUrl);
        bizParams.put("timeout_express", "1m");
        return JsonUtil.Object2Json(bizParams);
    }

    @Override
    public void doCallbackAPP(String result) {
        // 解析回调结果
        logger.info("**** APP支付回调结果： ****：" + result);
        CallbackResult callbackResult = alipayBO.doCallbackAPP(result);
        // 回调业务biz，通知支付结果
        logger.info("**** 回调业务biz参数： ****：" + callbackResult);
        alipayBO.doBizCallback(callbackResult);
    }

}
