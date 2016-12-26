/**
 * @Title CallbackWeChatServlet.java 
 * @Package com.std.account 
 * @Description 
 * @author haiqingzheng  
 * @date 2016年12月26日 下午1:44:16 
 * @version V1.0   
 */
package com.std.account;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.std.account.ao.IWeChatAO;
import com.std.account.enums.EBoolean;
import com.std.account.util.wechat.WXOrderQuery;
import com.std.account.util.wechat.XMLUtil;

/** 
 * @author: haiqingzheng 
 * @since: 2016年12月26日 下午1:44:16 
 * @history:
 */
@Controller
public class CallbackConroller {

    static Logger logger = Logger.getLogger(CallbackServlet.class);

    @Autowired
    IWeChatAO weChatAO;

    @RequestMapping("/wechat/h5/callback")
    public void doCallbackWechatH5(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        System.out.println("**** 进入微信公众号支付服务器回调 ****");
        PrintWriter out = response.getWriter();
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> map = null;
        try {
            map = XMLUtil.doXMLParse(result);
        } catch (JDOMException e) {
            e.printStackTrace();
        }

        // 此处获取accessToken
        String accessToken = weChatAO.getAccessToken("");
        // 此处调用订单查询接口验证是否交易成功
        boolean isSucc = reqOrderquery(map, accessToken);
        // 支付成功，商户处理后同步返回给微信参数
        if (!isSucc) {
            // 支付失败
            System.out.println("支付失败");
            weChatAO.doCallbackH5(map.get("out_trade_no"),
                EBoolean.NO.getCode());
        } else {
            System.out.println("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            weChatAO.doCallbackH5(map.get(""), EBoolean.YES.getCode());
            // ------------------------------
            // 处理业务完毕
            // ------------------------------
            String noticeStr = setXML("SUCCESS", "");
            out.print(new ByteArrayInputStream(noticeStr.getBytes(Charset
                .forName("UTF-8"))));
        }
    }

    public String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code
                + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }

    /**
     * 请求订单查询接口
     * @param map
     * @param accessToken
     * @return
     */
    public static boolean reqOrderquery(Map<String, String> map,
            String accessToken) {
        WXOrderQuery orderQuery = new WXOrderQuery();
        orderQuery.setAppid(map.get("appid"));
        orderQuery.setMch_id(map.get("mch_id"));
        orderQuery.setTransaction_id(map.get("transaction_id"));
        orderQuery.setOut_trade_no(map.get("out_trade_no"));
        orderQuery.setNonce_str(map.get("nonce_str"));

        // 此处需要密钥PartnerKey，此处直接写死，自己的业务需要从持久化中获取此密钥，否则会报签名错误
        orderQuery.setPartnerKey("r2jgDFSdiikklwlllejlwjio3242342n");

        Map<String, String> orderMap = orderQuery.reqOrderquery();
        // 此处添加支付成功后，支付金额和实际订单金额是否等价，防止钓鱼
        if (orderMap.get("return_code") != null
                && orderMap.get("return_code").equalsIgnoreCase("SUCCESS")) {
            if (orderMap.get("trade_state") != null
                    && orderMap.get("trade_state").equalsIgnoreCase("SUCCESS")) {
                String total_fee = map.get("total_fee");
                String order_total_fee = map.get("total_fee");
                if (Integer.parseInt(order_total_fee) >= Integer
                    .parseInt(total_fee)) {
                    return true;
                }
            }
        }
        return false;
    }
}
