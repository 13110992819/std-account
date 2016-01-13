package com.std.account.bo.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.std.account.bo.IIdentifyBO;
import com.std.account.dto.req.XN798001Req;
import com.std.account.dto.res.XN798001Res;
import com.std.account.http.BizConnecter;
import com.std.account.http.JsonUtils;

@Component
public class IdentifyBOImpl implements IIdentifyBO {
    static Logger logger = Logger.getLogger(IdentifyBOImpl.class);

    @Override
    public void doIdentify(String userId, String realName, String idKind,
            String idNo) {
        // try {
        XN798001Req req = new XN798001Req();
        req.setSystemId("1");
        req.setUserId(userId);
        req.setRealName(realName);
        req.setIdKind(idKind);
        req.setIdNo(idNo);
        req.setRemark("来自雄牛账户体系的实名认证");
        BizConnecter.getBizData("798001", JsonUtils.object2Json(req),
            XN798001Res.class);
        // } catch (Exception e) {
        // logger.error("调用实名认证服务异常");
        // }
    }
}
