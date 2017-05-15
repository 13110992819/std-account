package com.std.account.bo.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.WebUtils;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.IAlipayBO;
import com.std.account.bo.IChargeBO;
import com.std.account.bo.ICompanyChannelBO;
import com.std.account.bo.IJourBO;
import com.std.account.core.StringValidater;
import com.std.account.domain.CallbackResult;
import com.std.account.domain.Charge;
import com.std.account.domain.CompanyChannel;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EChargeStatus;
import com.std.account.enums.EJourBizType;
import com.std.account.exception.BizException;
import com.std.account.http.PostSimulater;
import com.std.account.util.CalculationUtil;
import com.std.account.util.alipay.AlipayCore;

@Component
public class AlipayBOImpl implements IAlipayBO {
    static Logger logger = Logger.getLogger(AlipayBOImpl.class);

    public static final String CHARSET = "utf-8";

    @Autowired
    IJourBO jourBO;

    @Autowired
    IAccountBO accountBO;

    @Autowired
    IChargeBO chargeBO;

    @Autowired
    ICompanyChannelBO companyChannelBO;

    @Override
    public CallbackResult doCallbackAPP(String result) {
        String systemCode = "CD-CZH000001";
        String companyCode = "CD-CZH000001";
        // 明显错误：
        // 目前只有正汇钱包使用支付宝，暂时写死
        // todo：如何判断公司编号和系统编号???。使用passback_params或者buyer_id(需先延签)
        CompanyChannel companyChannel = companyChannelBO.getCompanyChannel(
            companyCode, systemCode, EChannelType.Alipay.getCode());
        try {
            // 参数进行url_decode
            // String params = URLDecoder.decode(result, CHARSET);
            // 将异步通知中收到的待验证所有参数都存放到map中
            Map<String, String> paramsMap = split(result);
            // 过滤+排序
            Map<String, String> filterMap = AlipayCore.paraFilter(paramsMap);
            String content = AlipayCore.createLinkString(filterMap);
            // 拿到签名
            String sign = paramsMap.get("sign");
            filterMap.put("sign", sign);
            // 调用SDK验证签名
            boolean signVerified = AlipaySignature.rsa256CheckContent(content,
                sign, companyChannel.getPrivateKey2(), CHARSET);
            logger.info("验签结果：" + signVerified);

            boolean isSuccess = false;
            if (signVerified) {
                // TODO 验签成功后
                // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
                String outTradeNo = paramsMap.get("out_trade_no");
                String totalAmount = paramsMap.get("total_amount");
                String sellerId = paramsMap.get("seller_id");
                String appId = paramsMap.get("app_id");
                String alipayOrderNo = paramsMap.get("trade_no");
                String bizBackUrl = paramsMap.get("passback_params");
                String tradeStatus = paramsMap.get("trade_status");
                // 取到订单信息
                Charge order = chargeBO.getCharge(outTradeNo, systemCode);
                if (!EChargeStatus.toPay.getCode().equals(order.getStatus())) {
                    throw new BizException("xn000000", "充值订单不处于待支付状态，重复回调");
                }
                // 数据正确性校验
                if (order.getAmount().equals(
                    StringValidater.toLong(CalculationUtil.mult(totalAmount)))
                        && sellerId.equals(companyChannel.getChannelCompany())
                        && appId.equals(companyChannel.getPrivateKey3())) {
                    if ("TRADE_SUCCESS".equals(tradeStatus)
                            || "TRADE_FINISHED".equals(tradeStatus)) {// 支付成功
                        isSuccess = true;
                        // 更新充值订单状态
                        chargeBO.callBackChange(order, true);
                        // 收款方账户加钱
                        accountBO
                            .changeAmount(order.getAccountNumber(),
                                EChannelType.getEChannelType(order
                                    .getChannelType()), alipayOrderNo, order
                                    .getPayGroup(), order.getRefNo(),
                                EJourBizType.getBizType(order.getBizType()),
                                order.getBizNote(), order.getAmount());
                        // 托管账户加钱
                        accountBO
                            .changeAmount(order.getCompanyCode(), EChannelType
                                .getEChannelType(order.getChannelType()),
                                alipayOrderNo, order.getPayGroup(), order
                                    .getRefNo(), EJourBizType.getBizType(order
                                    .getBizType()), order.getBizNote(), order
                                    .getAmount());

                    } else {// 支付失败
                        // 更新充值订单状态
                        chargeBO.callBackChange(order, false);
                    }
                } else {
                    throw new BizException("xn000000", "数据正确性校验失败，非法回调");
                }

                return new CallbackResult(isSuccess, order.getBizType(),
                    order.getCode(), order.getPayGroup(), order.getAmount(),
                    systemCode, companyCode, bizBackUrl);
            } else {
                throw new BizException("xn000000", "验签失败，默认为非法回调");
            }

        } catch (AlipayApiException e) {
            throw new BizException("xn000000", "支付结果通知验签异常");
        }
    }

    private Map<String, String> split(String urlparam) {
        Map<String, String> map = new HashMap<String, String>();
        String[] param = urlparam.split("&");
        for (String keyvalue : param) {
            String[] pair = keyvalue.split("=");
            if (pair.length == 2) {
                map.put(pair[0], WebUtils.decode(pair[1]));
            }
        }
        return map;
    }

    @Override
    public void doBizCallback(CallbackResult callbackResult) {
        try {
            Properties formProperties = new Properties();
            formProperties.put("isSuccess", callbackResult.isSuccess());
            formProperties.put("systemCode", callbackResult.getSystemCode());
            formProperties.put("companyCode", callbackResult.getCompanyCode());
            formProperties.put("payGroup", callbackResult.getPayGroup());
            formProperties.put("payCode", callbackResult.getJourCode());
            formProperties.put("bizType", callbackResult.getBizType());
            formProperties.put("transAmount", callbackResult.getTransAmount());
            PostSimulater.requestPostForm(callbackResult.getUrl(),
                formProperties);
        } catch (Exception e) {
            throw new BizException("xn000000", "回调业务biz异常");
        }
    }

}
