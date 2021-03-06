package com.tf.base.unpublic.domain;


import com.tf.base.common.constants.CommonConstants;
import com.tf.base.common.excel.Excel;

public class UnpublicOrgExportBean {

	/**
	 * 企业名称
	 */
	@Excel(name = "企业名称")
	private String name;

	/**
	 * 企业类型
	 */
	@Excel(name = "企业类型", dictCode = CommonConstants.ENTERPRISE_TYPE)
	private String industryType;

	/**
	 * 国家
	 */
	@Excel(name = "国家", dictCode = CommonConstants.NATIONALITY)
	private String nationality;

	/**
	 * 行业类型
	 */
	@Excel(name = "行业类型", dictCode = CommonConstants.BUSINESS_TYPE)
	private String businessType;

	@Excel(name = "联系方式")
	private String contactPhone;

	/**
	 * 企业坐落地
	 */
	@Excel(name = "企业坐落地", content = "{0:其他,1:园区,2:楼宇,3:园区及楼宇}")
	private String belocatedAddress;

	/**
	 * 园区级别
	 */
	@Excel(name = "园区等级", content = "{1:国家级,2:市级,3:区级及以下}")
	private String level;

	/**
	 * 所在园区名称
	 */
	@Excel(name = "所在园区名称")
	private String inparkName;

	/**
	 * 是否为亿元楼宇
	 */
	@Excel(name = "是否为亿元楼宇", content = "{0:否,1:是}")
	private String millionBuildingIs;

	/**
	 * 所在商务楼宇名称
	 */
	@Excel(name = "所在商务楼宇名称")
	private String buildingName;

	/**
	 * 状态 1.临时保存 2.正常, 4.已撤销 0.已删除
	 */
	@Excel(name = "状态", dictCode = CommonConstants.UNPUBLIC_ORG_STATUS)
	private String status;

	/**
	 * 年营业收入
	 */
	@Excel(name = "年营业收入")
	private float businessVolume;

	/**
	 * 从业人员数量
	 */
	@Excel(name = "从业人员数量")
	private Integer jobinTotalnum;

	/**
	 * 是否规模以上企业
	 */
	@Excel(name = "是否规模以上企业", content = "{0:否,1:是}")
	private String onScaleIs;

	/**
	 * 企业注册地
	 */
	@Excel(name = "企业注册地")
	private String registerAddress;

	/**
	 * 企业注册地:地区等级
	 */
	@Excel(name = "注册地-地区等级", content = "{1:新区内,2:天津市内,3:天津市外}")
	private String registerAddressLevel;

	/**
	 * 注册地-省
	 */
	@Excel(name = "注册地省", content = "{110000:北京市,120000:天津市,130000:河北省,140000:山西省,150000:内蒙古自治区,210000:辽宁省,220000:吉林省,230000:黑龙江省,310000:上海市,320000:江苏省,330000:浙江省,340000:安徽省,350000:福建省,360000:江西省,370000:山东省,410000:河南省,420000:湖北省,430000:湖南省,440000:广东省,450000:广西壮族自治区,460000:海南省,500000:重庆市,510000:四川省,520000:贵州省,530000:云南省,540000:西藏自治区,610000:陕西省,620000:甘肃省,630000:青海省,640000:宁夏回族自治区,650000:新疆维吾尔自治区,710000:台湾省,810000:香港特别行政区,820000:澳门特别行政区}")
	private String registerAddressProvince;

	/**
	 * 注册地-市
	 */
	@Excel(name = "注册地市")
	private String registerAddressCity;

	public String getRegisterAddressCity() {
		return registerAddressCity;
	}

	public void setRegisterAddressCity(String registerAddressCity) {
		this.registerAddressCity = registerAddressCity;
	}

	/**
	 * 注册地-区
	 */
	@Excel(name = "注册地区", content = "{120000:天津市,120101:和平区,120102:河东区,120103:河西区,120104:南开区,120105:河北区,120106:红桥区,120110:东丽区,120111:西青区,120112:津南区,120113:北辰区,120114:武清区,120115:宝坻区,120116:滨海新区,120117:宁河区,120118:静海区,120119:蓟州区}")
	private String registerAddressDistrict;

	/**
	 * 注册地-街道
	 */
	@Excel(name = "注册地街道", content = "{120116001:塘沽街道,120116005:杭州道街道,120116006:新河街道,120116007:大沽街道,120116008:新北街道,120116009:北塘街道,120116011:胡家园街道,120116031:汉沽街道,120116032:寨上街道,120116033:茶淀街道,120116051:大港街道,120116053:古林街道,120116054:海滨街道,120116100:新城镇,120116135:杨家泊镇,120116156:太平镇,120116157:小王庄镇,120116158:中塘镇,120116400:开发区,120116401:保税区,120116402:天津滨海新区高新技术产业开发区,120116403:东疆保税港区,120116404:中心商务区,120116405:临港经济区,120116406:中新天津生态城}")
	private String registerAddressStreet;

	/**
	 * 是否存在企业经营地
	 */
	@Excel(name = "是否存在企业经营地", content = "{0:否,1:是}")
	private String isHaveAddress;

	/**
	 * 企业经营地
	 */
	@Excel(name = "企业经营地")
	private String operateAddress;

	/**
	 * 企业出资人姓名
	 */
	@Excel(name = "企业出资人姓名")
	private String sponsorName;

	/**
	 * 企业出资人是否党员
	 */
	@Excel(name = "是否党员", content = "{0:否,1:是}")
	private String sponsorPartymemberIs;

	/**
	 * 企业出资人是否兼任党组织书记
	 */
	@Excel(name = "是否兼任党组织书记", content = "{0:否,1:是}")
	private String sponsorPartyorgSecretaryIs;

	/**
	 * 企业出资人是否担任区县级以上（含区县）“两代表一委员
	 */
	@Excel(name = "是否担任区县级以上（含区县）“两代表一委员", content = "{0:否,1:是}")
	private String sponsorTwodeputyAcommitteeIs;

	/**
	 * 企业出资人担任区县级以上（含区县）“两代表一委员类型
	 */
	@Excel(name = "担任区县级以上（含区县）“两代表一委员类型")
	private String sponsorTwodeputyAcommitteeType;

	/**
	 * 类型为其他时，录入的内容
	 */
	@Excel(name = "类型为其他时")
	private String sponsorTwodeputyAcommitteeTypeOther;

	/**
	 * 工会 1.有 0 无
	 */
	@Excel(name = "工会", content = "{0:无,1:有}")
	private String hasSociaty;

	/**
	 * 共青团 1 有 0.无
	 */
	@Excel(name = "共青团", content = "{0:无,1:有}")
	private String hasYouthLeague;

	/**
	 * 妇联 1.有 0.无
	 */
	@Excel(name = "妇联", content = "{0:无,1:有}")
	private String hasWomenLeague;

	
	@Excel(name = "党员数统计")
    private Integer partymbrNum;
	@Excel(name = "组织关系在企业内的党员数统计")
    private Integer partymbrInUnpublicNum;
	@Excel(name = "组织关系在企业外的党员数统计")
    private Integer partymbrNotinUnpublicNum;

	public String getOperateAddress() {
		return operateAddress;
	}

	public void setOperateAddress(String operateAddress) {
		this.operateAddress = operateAddress;
	}

	
}
