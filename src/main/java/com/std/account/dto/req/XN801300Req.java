package com.std.account.dto.req;

public class XN801300Req {

    // 公司名字(必填)
    private String companyName;

    // 工商营业执照号(必填)
    private String licenceNo;

    // 法人证件类型(必填)
    private String idKind;

    // 法人证件号码(必填)
    private String idNo;

    // 法人真实姓名(必填)
    private String realName;

    // 注册资金(必填)
    private String capital;

    // 省(必填)
    private String province;

    // 市(必填)
    private String city;

    // 申请提交人(必填)
    private String applyUser;

    // 公司地址(非必填)
    private String address;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public String getIdKind() {
        return idKind;
    }

    public void setIdKind(String idKind) {
        this.idKind = idKind;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

}
