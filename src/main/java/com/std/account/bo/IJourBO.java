package com.std.account.bo;

import java.util.List;

import com.std.account.bo.base.IPaginableBO;
import com.std.account.domain.Account;
import com.std.account.domain.HLOrder;
import com.std.account.domain.Jour;
import com.std.account.enums.EBoolean;
import com.std.account.enums.EChannelType;
import com.std.account.enums.EJourBizType;

/**
 * @author: xieyj 
 * @since: 2016年12月23日 下午2:40:13 
 * @history:
 */
public interface IJourBO extends IPaginableBO<Jour> {

    public String addJour(Account dbAccount, EChannelType channelType,
            String channelOrder, String payGroup, String refNo,
            EJourBizType bizType, String bizNote, Long transAmount);

    // 对账结果录入
    public void doCheckJour(Jour jour, EBoolean checkResult, Long checkAmount,
            String checkUser, String checkNote);

    public String addWithChangeJour(String systemCode, String accountNumber,
            String channelType, String bizType, String bizNote,
            Long transAmount, Long fee, String payGroup);

    /**
     * 线上充值/to线上划账回调处理流水(发生前金额，发生后金额内部设置)
     * @param data
     * @param rollBackResult
     * @param rollbackUser
     * @param rollbackNote
     * @param channelOrder
     * @return 
     * @create: 2017年5月3日 上午11:07:35 xieyj
     * @history:
     */
    public int callBackChangeJour(Jour data, String rollBackResult,
            String rollbackUser, String rollbackNote, String channelOrder);

    /**
     * 线下充值取现回调处理流水
     * @param data
     * @param rollbackResult
     * @param rollbackUser
     * @param rollbackNote
     * @param preAmount
     * @param postAmount
     * @return 
     * @create: 2017年5月3日 上午11:09:27 xieyj
     * @history:
     */
    public int callBackOffChangeJour(Jour data, String rollbackResult,
            String rollbackUser, String rollbackNote, Long preAmount,
            Long postAmount);

    /**
     * 新增已变动金额之流水
     * @param systemCode
     * @param accountNumber
     * @param channelType
     * @param channelOrder
     * @param payType
     * @param bizType
     * @param bizNote
     * @param preAmount
     * @param transAmount
     * @return 
     * @create: 2016年12月23日 下午2:48:40 xieyj
     * @history:
     */
    public String addChangedJour(String systemCode, String accountNumber,
            EChannelType channelType, String channelOrder, String bizType,
            String bizNote, Long preAmount, Long transAmount);

    /**
     * 对账结果录入
     * @param code
     * @param checkResult
     * @param checkUser
     * @param checkNote 
     * @create: 2016年12月25日 下午5:00:10 xieyj
     * @history:
     */
    public void doCheckJour(String code, EBoolean checkResult,

    String checkUser, String checkNote);

    // 当前流水，调整不通过。即账其实是平的
    public void adjustJourNO(Jour jour, String adjustUser, String adjustNote);

    public void adjustJourYES(Jour jour, String adjustUser, String adjustNote);

    public List<Jour> queryJourList(Jour condition);

    /**
     * 判断申请记录是否存在
     * @param accountNumber
     * @param bizType 
     * @create: 2017年5月2日 下午2:24:39 xieyj
     * @history:
     */
    public void doCheckExistApplyJour(String accountNumber, String bizType);

    /**
     * 获取详情
     * @param code
     * @param systemCode
     * @return 
     * @create: 2016年12月24日 上午8:19:51 xieyj
     * @history:
     */

    public Jour getJour(String code, String systemCode);

    public String addJourForHL(Account dbAccount, HLOrder order);

}
