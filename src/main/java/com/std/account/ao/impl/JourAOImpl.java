/**
 * @Title IJourAOImpl.java 
 * @Package com.std.account.ao.impl 
 * @Description 
 * @author xieyj  
 * @date 2016年12月23日 下午9:16:58 
 * @version V1.0   
 */
package com.std.account.ao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.std.account.ao.IJourAO;
import com.std.account.bo.IAccountBO;
import com.std.account.bo.IBankcardBO;
import com.std.account.bo.ICompanyChannelBO;
import com.std.account.bo.IHLOrderBO;
import com.std.account.bo.IJourBO;
import com.std.account.bo.ISYSConfigBO;
import com.std.account.bo.IUserBO;
import com.std.account.bo.IWechatBO;
import com.std.account.bo.base.Paginable;
import com.std.account.common.PropertiesUtil;
import com.std.account.common.SysConstant;
import com.std.account.domain.Account;
import com.std.account.domain.Bankcard;
import com.std.account.domain.CompanyChannel;
import com.std.account.domain.Jour;
import com.std.account.domain.User;
import com.std.account.enums.EAccountType;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EChannelType;
import com.std.account.enums.ECurrency;
import com.std.account.enums.EJourStatus;
import com.std.account.enums.EPayType;
import com.std.account.exception.BizException;
import com.std.account.util.AmountUtil;

/** 
 * @author: xieyj 
 * @since: 2016年12月23日 下午9:16:58 
 * @history:
 */
@Service
public class JourAOImpl implements IJourAO {

    @Autowired
    private IUserBO userBO;

    @Autowired
    private ICompanyChannelBO companyChannelBO;

    @Autowired
    private IJourBO jourBO;

    @Autowired
    private IAccountBO accountBO;

    @Autowired
    private IHLOrderBO hlOrderBO;

    @Autowired
    private IBankcardBO bankcardBO;

    @Autowired
    private IWechatBO wechatBO;

    @Autowired
    private ISYSConfigBO sysConfigBO;

    /** 
     * @see com.std.account.ao.IJourAO#doRechargeOnline(java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    public Object doRechargeOnline(String userId, String payType, Long amount) {
        if (EPayType.WEIXIN_H5.getCode().equals(payType)) {
            return doWeiXinH5Qz(userId, payType, amount);
        } else if (EPayType.WEIXIN_APP.getCode().equals(payType)) {
        } else if (EPayType.ALIPAY.getCode().equals(payType)) {
        } else {
            throw new BizException("xn000000", "暂不支持该支付方式");
        }
        return null;
    }

    /** 
     * @param userId
     * @param payType
     * @param amount 
     * @create: 2017年4月21日 下午4:45:47 xieyj
     * @history: 
     */
    private Object doWeiXinH5Qz(String userId, String payType, Long transAmount) {
        User user = userBO.getRemoteUser(userId);
        if (transAmount.longValue() == 0l) {
            throw new BizException("xn000000", "发生金额为零，不能使用微信支付");
        }
        if (StringUtils.isBlank(user.getOpenId())) {
            throw new BizException("xn000000", "请微信登录后再支付");
        }
        Account account = accountBO.getAccountByUser(userId,
            ECurrency.CNY.getCode());
        String systemCode = account.getSystemCode();
        // 落地付款方流水信息
        String jourCode = jourBO.addToChangeJour(systemCode,
            account.getAccountNumber(), EChannelType.WeChat_H5.getCode(),
            EBizType.AJ_CZ.getCode(), EBizType.AJ_CZ.getValue(), transAmount,
            null);
        // 获取微信公众号支付prepayid
        CompanyChannel companyChannel = companyChannelBO.getCompanyChannel(
            systemCode, systemCode, EChannelType.WeChat_H5.getCode());
        String prepayId = wechatBO.getPrepayIdH5(companyChannel,
            user.getOpenId(), "微信公众号充值", jourCode, transAmount, SysConstant.IP,
            PropertiesUtil.Config.WECHAT_H5_QzBACKURL, null);
        // 返回微信公众号支付所需信息
        return wechatBO.getPayInfoH5(companyChannel, jourCode, prepayId);
    }

    /**
     * 取现申请
     * @see com.std.account.ao.IJourAO#doOfflineWith(java.lang.String, java.lang.String, java.lang.Long, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    public void doOfflineWith(String accountNumber, String bankcardNumber,
            Long transAmount, String systemCode, String tradePwd) {
        Account account = accountBO.getAccount(accountNumber);
        if (StringUtils.isNotBlank(tradePwd)) {
            userBO.checkTradePwd(account.getUserId(), tradePwd);
        }
        String bizType = EBizType.AJ_QX.getCode();
        // 判断是否有取现申请待批准记录
        jourBO.doCheckExistApplyJour(accountNumber, bizType);
        // 业务备注由程序生成
        String bizNote = "";
        if (StringUtils.isNotBlank(bankcardNumber)) {
            Bankcard bankcard = bankcardBO
                .getBankcardByBankcardNumber(bankcardNumber);
            if (bankcard != null) {
                bizNote += "户名：" + bankcard.getRealName() + " ";
                bizNote += "卡号：" + bankcardNumber + " ";
                bizNote += "银行：" + bankcard.getBankName() + " ";
                if (StringUtils.isNotBlank(bankcard.getSubbranch())) {
                    bizNote += "支行：" + bankcard.getSubbranch();
                }
            } else {
                bizNote = bankcardNumber;
            }
        } else {
            bizNote = EBizType.getBizTypeMap().get(bizType).getValue();
        }
        doOfflineWith(accountNumber, bankcardNumber, transAmount, bizType,
            bizNote, systemCode, account);
    }

    /** 
     * @param accountNumber
     * @param bankcardNumber
     * @param transAmount
     * @param bizType
     * @param bizNote
     * @param systemCode
     * @param account 
     * @create: 2017年5月2日 下午2:12:52 xieyj
     * @history: 
     */
    private void doOfflineWith(String accountNumber, String bankcardNumber,
            Long transAmount, String bizType, String bizNote,
            String systemCode, Account account) {
        // 手续费
        Long fee = 0L;
        if (EAccountType.Customer.getCode().equals(account.getType())) {
            fee = doCheckWithArgs(transAmount, SysConstant.CUSERQXBS,
                SysConstant.CUSERQXFL, systemCode);
        } else if (EAccountType.Business.getCode().equals(account.getType())) {
            fee = doCheckWithArgs(transAmount, SysConstant.BUSERQXBS,
                SysConstant.BUSERQXFL, systemCode);
        }
        // 取现冻结
        String code = jourBO.addWithChangeJour(systemCode, accountNumber,
            EChannelType.CZB.getCode(), bizType, bizNote, transAmount - fee,
            fee, null);
        accountBO.frozenAmount(systemCode, accountNumber, -transAmount + fee,
            code);
    }

    /**
     * 取现申请检查，验证参数，返回手续费
     * @param transAmount
     * @param qxbs
     * @param qxfl
     * @param systemCode
     * @return 
     * @create: 2017年5月2日 下午4:15:01 xieyj
     * @history:
     */
    private Long doCheckWithArgs(Long transAmount, String qxbs, String qxfl,
            String systemCode) {
        Map<String, String> argsMap = sysConfigBO.getConfigsMap(systemCode);
        String qxBsValue = argsMap.get(qxbs);
        if (StringUtils.isNotBlank(qxBsValue)) {
            // 取现金额倍数
            Long qxBs = AmountUtil.mul(1000L, Double.valueOf(qxBsValue));
            if (qxBs > 0 && -transAmount % qxBs > 0) {
                throw new BizException("xn000000", "请取" + qxBsValue + "的倍数");
            }
        }
        String feeRateValue = argsMap.get(qxfl);
        Double feeRate = 0D;
        if (StringUtils.isNotBlank(feeRateValue)) {
            feeRate = Double.valueOf(feeRateValue);
        }
        return AmountUtil.mul(-transAmount, feeRate);
    }

    /** 
     * @param accountNumber
     * @param bankcardNumber
     * @param transAmount
     * @param bizType
     * @param bizNote
     * @param channelTypeList
     * @param systemCode
     * @param tradePwd
     * @return 
     * @create: 2017年4月24日 下午10:48:58 xieyj
     * @history: 
     */
    private void doChangeAmount(String accountNumber, String bankcardNumber,
            Long transAmount, String bizType, String bizNote, String systemCode) {
        Account account = accountBO.getAccount(accountNumber);
        // 业务备注由程序生成
        if (StringUtils.isNotBlank(bankcardNumber)) {
            Bankcard bankcard = bankcardBO
                .getBankcardByBankcardNumber(bankcardNumber);
            if (bankcard != null) {
                bizNote += "户名：" + bankcard.getRealName() + " ";
                bizNote += "卡号：" + bankcardNumber + " ";
                bizNote += "银行：" + bankcard.getBankName() + " ";
                if (StringUtils.isNotBlank(bankcard.getSubbranch())) {
                    bizNote += "支行：" + bankcard.getSubbranch();
                }
            } else {
                bizNote = bankcardNumber;
            }
        } else {
            bizNote = EBizType.getBizTypeMap().get(bizType).getValue();
        }
        // 取现冻结
        if (EBizType.AJ_QX.getCode().equals(bizType)) {
            this.doOfflineWith(accountNumber, bankcardNumber, transAmount,
                bizType, bizNote, systemCode, account);
        } else {
            jourBO
                .addToChangeJour(systemCode, accountNumber,
                    EChannelType.CZB.getCode(), bizType, bizNote, transAmount,
                    null);
        }
    }

    /*
     * 外部账批量支付：1、产生支付申请订单；2、返回支付链接；
     */
    @Override
    @Transactional
    public void doChangeAmountList(List<String> accountNumberList,
            String bankcardNumber, Long transAmount, String bizType,
            String bizNote, String systemCode) {
        for (String accountNumber : accountNumberList) {
            this.doChangeAmount(accountNumber, bankcardNumber, transAmount,
                bizType, bizNote, systemCode);
        }
    }

    /*
     * 回调方法： 1、审核通过扣除金额；审核不通过，资金原路返回
     */
    @Override
    @Transactional
    public void doCallBackOffChange(String code, String rollbackResult,
            String rollbackUser, String rollbackNote, String systemCode) {
        Jour data = jourBO.getJour(code, systemCode);
        // 判断流水状态
        if (!EJourStatus.todoCallBack.getCode().equals(data.getStatus())) {
            throw new BizException("xn000000", "申请记录状态不是刚生成待回调状态，无法审批");
        }
        Account account = accountBO.getAccount(data.getAccountNumber());
        Long preAmount = account.getAmount();
        Long postAmount = preAmount;
        if (EBoolean.YES.getCode().equals(rollbackResult)) {
            if (EBizType.AJ_CZ.getCode().equals(data.getBizType())) {
                accountBO.transAmountNotJour(data.getSystemCode(),
                    data.getAccountNumber(), data.getTransAmount(), code);
                // 更新发生后金额
                postAmount = preAmount + data.getTransAmount();
            } else if (EBizType.AJ_QX.getCode().equals(data.getBizType())) {
                accountBO.unfrozenAmount(data.getSystemCode(),
                    EBoolean.YES.getCode(), data.getAccountNumber(),
                    -data.getTransAmount(), code);
                postAmount = preAmount;
                preAmount = preAmount - data.getTransAmount();
                // 更新累计取现金额
                accountBO.refreshOutAmount(account, -data.getTransAmount());
            }
        } else {
            if (EBizType.AJ_QX.getCode().equals(data.getBizType())) {
                accountBO.unfrozenAmount(data.getSystemCode(),
                    EBoolean.NO.getCode(), data.getAccountNumber(),
                    -data.getTransAmount(), code);
                postAmount = preAmount - data.getTransAmount();
            }
        }
        jourBO.callBackOffChangeJour(data, rollbackResult, rollbackUser,
            rollbackNote, preAmount, postAmount);
    }

    @Override
    @Transactional
    public void doCallBackChangeList(List<String> codeList,
            String rollbackResult, String rollbackUser, String rollbackNote,
            String systemCode) {
        for (String code : codeList) {
            this.doCallBackOffChange(code, rollbackResult, rollbackUser,
                rollbackNote, systemCode);
        }
    }

    /*
     * 人工调账： 1、判断流水账是否平，平则更改订单状态，不平则更改产生红冲蓝补订单，而后更改订单状态
     */
    @Override
    @Transactional
    public void checkJour(String code, Long checkAmount, String checkUser,
            String checkNote, String systemCode) {
        Jour jour = jourBO.getJour(code, systemCode);
        if (!EJourStatus.todoCheck.getCode().equals(jour.getStatus())) {
            throw new BizException("xn000000", "该流水<" + code + ">不处于待对账状态");
        }
        if (checkAmount != 0) {
            Account account = accountBO.getAccount(jour.getAccountNumber());
            hlOrderBO.applyOrder(account, jour, checkAmount, checkUser,
                checkNote);
            jourBO.doCheckJour(jour, EBoolean.NO, checkAmount, checkUser,
                checkNote);
        } else {
            jourBO.doCheckJour(jour, EBoolean.YES, checkAmount, checkUser,
                checkNote);
        }
    }

    @Override
    public Paginable<Jour> queryJourPage(int start, int limit, Jour condition) {
        // 处理bizType=52,54
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
        return jourBO.getPaginable(start, limit, condition);
    }

    @Override
    public List<Jour> queryJourList(Jour condition) {
        // 处理bizType=52,54
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
        return jourBO.queryJourList(condition);
    }

    @Override
    public Jour getJour(String code, String systemCode) {
        return jourBO.getJour(code, systemCode);
    }
}
