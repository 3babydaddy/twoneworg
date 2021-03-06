package com.tf.base.socialorg.domain;


import com.tf.base.common.constants.CommonConstants;
import com.tf.base.common.excel.Excel;


public class SocialOrgExportBean {
    
	//基础
	@Excel(name = "企业名称")
    private String name;

	@Excel(name="性质",dictCode=CommonConstants.ORG_NATURE)
    private String nature;

	@Excel(name="类别", content = "{1:其他}")
    private String category;

	@Excel(name = "登记机构")
    private String registerOrg;

	@Excel(name = "业务主管单位")
    private String businessDirectorOrg;

	@Excel(name="住地")
    private String address;

    @Excel(name = "是否设立思想政治教育工作机构", content = "{0:否,1:是}")
    private String isIdeologicalPoliticalOrg;
    @Excel(name = "是否设立德育工作机构", content = "{0:否,1:是}")
    private String isMoralEducationOrg;
    
    //从业人员
    @Excel(name="从业人员总数")
    private Integer jobinTotalnum;
    @Excel(name="专职人数")
    private Integer jobinMajorNum;
    @Excel(name="兼职人数")
    private Integer jobinPluralityNum;
    @Excel(name="从业中共党员数")
    private Integer jobinPartymemberNum;
    @Excel(name="社会团体单位会员数量")
    private Integer jobinSocialteamGroupmemberNum;
    @Excel(name="社会团体个人会员数量")
    private Integer jobinSocialteamIndividualmemberNum;
  
    //负责人
    @Excel(name="负责人姓名")
    private String chargeName;
    @Excel(name="负责人是否党员")
    private String chargePartymemberIs;
    @Excel(name="负责人是否兼任党组织书记", content = "{0:否,1:是}")
    private String chargePartyorgSecretaryIs;
    @Excel(name="负责人是否担任区县级以上（含区县）“两代表一委员”", content = "{0:否,1:是}")
    private String chargeTwodeputyAcommitteeIs;
    @Excel(name="负责人是否担任区县级以上（含区县）“两代表一委员”类型", content = "{0:否,1:是}")
    private String chargeTwodeputyAcommitteeType;
    @Excel(name="内容")
    private String chargeTwodeputyAcommitteeTypeOther;

    @Excel(name = "党员数统计")
    private Integer partymbrNum;
    @Excel(name = "组织关系在组织内的党员数统计")
    private Integer partymbrInSocielorgNum;
    @Excel(name = "组织关系在组织外的党员数统计")
    private Integer partymbrNotInSocielorgNum;

    
}