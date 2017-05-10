package com.std.account.bo;

import java.util.List;

import com.std.account.bo.base.IPaginableBO;
import com.std.account.domain.CompanyChannel;

public interface ICompanyChannelBO extends IPaginableBO<CompanyChannel> {

    public boolean isCompanyChannelExist(Long id);

    public long getCompanyChannelCount(String companyCode, String channelType);

    public Long saveCompanyChannel(CompanyChannel data);

    public int removeCompanyChannel(Long id);

    public int refreshCompanyChannel(CompanyChannel data);

    public List<CompanyChannel> queryCompanyChannelList(CompanyChannel condition);

    public CompanyChannel getCompanyChannel(Long id);

    public CompanyChannel getCompanyChannel(String companyCode,
            String systemCode, String channelType);

}
