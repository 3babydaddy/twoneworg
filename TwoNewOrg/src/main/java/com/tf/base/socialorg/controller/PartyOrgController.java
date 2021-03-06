package com.tf.base.socialorg.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tf.base.common.constants.CommonConstants;
import com.tf.base.common.constants.CommonConstants.LOG_OPERATION_TYPE;
import com.tf.base.common.domain.DataDictionary;
import com.tf.base.common.domain.DictionaryRepository;
import com.tf.base.common.service.BaseService;
import com.tf.base.common.service.LogService;
import com.tf.base.common.utils.PageUtil;
import com.tf.base.socialorg.domain.SocialOrgInfo;
import com.tf.base.socialorg.domain.SocialOrgPmbrCount;
import com.tf.base.socialorg.domain.SocialOrgPmbrInfo;
import com.tf.base.socialorg.domain.SocialPartyOrgChangeInfo;
import com.tf.base.socialorg.domain.SocialPartyOrgInfo;
import com.tf.base.socialorg.persistence.SocialOrgInfoMapper;
import com.tf.base.socialorg.persistence.SocialOrgPmbrCountMapper;
import com.tf.base.socialorg.persistence.SocialOrgPmbrInfoMapper;
import com.tf.base.socialorg.persistence.SocialPartyOrgChangeInfoMapper;
import com.tf.base.socialorg.persistence.SocialPartyOrgInfoMapper;
import com.tf.base.socialorg.service.PartyOrgService;
import com.tf.base.unpublic.domain.AttachmentCommonInfo;
import com.tf.base.unpublic.domain.CancelReasonInfo;
import com.tf.base.unpublic.domain.DeputySecretaryInfo;
import com.tf.base.unpublic.domain.LowerPartyOrg;
import com.tf.base.unpublic.domain.PartyInstructorInfo;
import com.tf.base.unpublic.domain.QueryPmbrParams;
import com.tf.base.unpublic.persistence.AttachmentCommonInfoMapper;
import com.tf.base.unpublic.persistence.CancelReasonInfoMapper;
import com.tf.base.unpublic.persistence.DeputySecretaryInfoMapper;
import com.tf.base.unpublic.persistence.LowerPartyOrgMapper;
import com.tf.base.unpublic.persistence.PartyInstructorInfoMapper;

import tk.mybatis.mapper.entity.Example;

@Controller
public class PartyOrgController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DictionaryRepository dict;
	@Autowired
	private BaseService baseService;
	@Autowired
	private LogService logService;
	@Autowired
	private PartyOrgService partyOrgService;
	@Autowired
	private SocialOrgInfoMapper socialOrgInfoMapper;
	@Autowired
	private SocialPartyOrgInfoMapper socialPartyOrgInfoMapper;
	@Autowired
	private AttachmentCommonInfoMapper attachmentCommonInfoMapper;
	@Autowired
	private SocialPartyOrgChangeInfoMapper socialPartyOrgChangeInfoMapper;
	@Autowired
	private CancelReasonInfoMapper cancelReasonInfoMapper;
	@Autowired
	private DeputySecretaryInfoMapper deputySecretaryInfoMapper;
	@Autowired
	private SocialOrgPmbrCountMapper socialOrgPmbrCountMapper;
	@Autowired
	private SocialOrgPmbrInfoMapper socialOrgPmbrInfoMapper;
	@Autowired
	private PartyInstructorInfoMapper partyInstructorInfoMapper;
	@Autowired
	private LowerPartyOrgMapper lowerPartyOrgMapper;
	
	/**
	 * 初始化页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/socialorg/partyorglist",method=RequestMethod.GET)
	public String orginit(Model model){
		
		initPage(model);
		return "socialorg/partyOrgList";
	}
	
	/**
	 * 党组织信息列表展示
	 * @param params
	 * @param page
	 * @param rows
	 * @param response
	 */
	@RequestMapping(value="/socialorg/partyorglist",method=RequestMethod.POST)
	public void orglist(SocialPartyOrgInfo params,
			int page,int rows,HttpServletResponse response){
		String deptId = baseService.getCurrentUserDeptId();
		logger.debug("当前登录用户部门ID:===========>" + deptId + " ,是否为区委部门?" + baseService.isQuWeiDept());
		if(!baseService.isQuWeiDept()){
			params.setCreateOrg(deptId);
		}else{
			//工委列表展示过滤掉组织状态为正常但还没有新建党组织的数据
			params.setIsQuWeiSign("1");
		}
		PageHelper.startPage(page, rows, true);
		List<SocialPartyOrgInfo> list = socialPartyOrgInfoMapper.queryList(params);
		this.convertRows(list);
		PageUtil.returnPage(response, new PageInfo<SocialPartyOrgInfo>(list));
		
		
	}
	
	/**
	 * 查看党组织信息
	 * @param id
	 * @param nature
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/socialorg/partyOrglook",method=RequestMethod.GET)
	public String orglook(String id, String nature, Model model){
		List<SocialPartyOrgChangeInfo> changeDateList = new ArrayList<>();
		List<DeputySecretaryInfo> deputsecList = new ArrayList<>();
		List<PartyInstructorInfo> instructList = new ArrayList<>();
		List<LowerPartyOrg> lowerPartyOrgList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		Integer mainId = Integer.parseInt(id);
		
		SocialPartyOrgInfo socialPartyOrgInfo = socialPartyOrgInfoMapper.selectByPrimaryKey(mainId);
		if(socialPartyOrgInfo.getPartyOrgAttachment() != "" && socialPartyOrgInfo.getPartyOrgAttachment().length() > 0){
			AttachmentCommonInfo attachmentCommonInfo = attachmentCommonInfoMapper.selectByPrimaryKey(Integer.parseInt(socialPartyOrgInfo.getPartyOrgAttachment()));
			socialPartyOrgInfo.setFilepartyOrgAttachment(attachmentCommonInfo.getFilename());
		}
		socialPartyOrgInfo.setInstructorUnitTxt(baseService.getDeptNameById(socialPartyOrgInfo.getInstructorUnit()));
		socialPartyOrgInfo.setSecretaryCompanyTxt(baseService.getDeptNameById(socialPartyOrgInfo.getSecretaryCompany()));
		//查询下级党组织的相关信息
		Example exampleByLp = new Example(LowerPartyOrg.class);
		exampleByLp.createCriteria().andEqualTo("partyOrgId", mainId).andEqualTo("type", 1).andEqualTo("status", 1);
		lowerPartyOrgList = lowerPartyOrgMapper.selectByExample(exampleByLp);
		//查询指导人的相关信息
		Example exampleByIn = new Example(PartyInstructorInfo.class);
		exampleByIn.createCriteria().andEqualTo("partyOrgId", mainId).andEqualTo("type", 1).andEqualTo("status", 1);
		instructList = partyInstructorInfoMapper.selectByExample(exampleByIn);
		for(PartyInstructorInfo insInfo : instructList){
			//该指导人员的下的组织
			String insOrgs = insInfo.getInstructOrgs();
			if(StringUtils.isNotEmpty(insOrgs)){
				insInfo.setOrgIdList(Arrays.asList(insOrgs.split(",")));
			}
		}
		//查看换届的相关信息
		Example example = new Example(SocialPartyOrgChangeInfo.class);
		example.createCriteria().andEqualTo("socialPartyOrgId", mainId).andEqualTo("status", 1);
		changeDateList = socialPartyOrgChangeInfoMapper.selectByExample(example);
		for(SocialPartyOrgChangeInfo upocInfo : changeDateList){
			AttachmentCommonInfo acInfo = attachmentCommonInfoMapper.selectByPrimaryKey(upocInfo.getChangeAttachmentId());
			if(acInfo != null){
				upocInfo.setChangeAttachmentName(acInfo.getFilename());
			}
			if(upocInfo.getChangeTime() != null){
				upocInfo.setChangeTimeTxt(sdf.format(upocInfo.getChangeTime()));
			}
		}
		//查询党副的相关信息
		Example dsexample = new Example(DeputySecretaryInfo.class);
		dsexample.createCriteria().andEqualTo("partyOrgId", mainId).andEqualTo("type", "1").andEqualTo("status", 1);
		deputsecList = deputySecretaryInfoMapper.selectByExample(dsexample);
		for(DeputySecretaryInfo dsInfo : deputsecList){
			if(dsInfo.getDeputySecretaryBirthday() != null){
				dsInfo.setDeputySecretaryBirthdayTxt(sdf.format(dsInfo.getDeputySecretaryBirthday()));
			}
		}
		//组装建立党组织的单位字符串
		String orgNames = "";
		String orgIds1 = "";
		Example example1 = new Example(SocialOrgInfo.class);
		example1.createCriteria().andEqualTo("socialPartyOrgId", mainId);
		List<SocialOrgInfo> orgInfoList = socialOrgInfoMapper.selectByExample(example1);
		for(SocialOrgInfo info : orgInfoList){
			if(orgNames == ""){
				orgNames = info.getName();
				orgIds1 = info.getId()+"";
			}else{
				orgNames = orgNames +","+ info.getName();
				orgIds1 = orgIds1 + "," + info.getId();
			}
		}
		//时间的类型转换
		if(socialPartyOrgInfo.getPartyOrgTime() != null){
			socialPartyOrgInfo.setPartyOrgTimeTxt(sdf.format(socialPartyOrgInfo.getPartyOrgTime()));
		}
		if(socialPartyOrgInfo.getSecretaryBirthday() != null){
			socialPartyOrgInfo.setSecretaryBirthdayTxt(sdf.format(socialPartyOrgInfo.getSecretaryBirthday()));
		}
		String[] orgIdArray = orgIds1.split(",");
		SocialOrgPmbrCount opcInfo = socialOrgPmbrCountMapper.getPmbrCount(orgIdArray);
		this.convertData(orgIds1, orgNames, model);
		model.addAttribute("main", socialPartyOrgInfo);
		model.addAttribute("changeDateList", changeDateList);
		model.addAttribute("deputsecList", deputsecList);
		model.addAttribute("instructList", instructList);
		model.addAttribute("lowerPartyList", lowerPartyOrgList);
		model.addAttribute("instructSize", instructList.size());
		model.addAttribute("orgNames", orgNames);
		model.addAttribute("opcInfo", opcInfo);
		model.addAttribute("nature", nature);
		model.addAttribute("orgIds", orgIds1);
		editPage(model,"");
		
		//查看日志
		logService.saveLog(LOG_OPERATION_TYPE.VIEW.toString(), 
				logService.getDetailInfo("log.socialorg.view",
						baseService.getUserName(),socialPartyOrgInfo.getPartyOrgName()));
		return "socialorg/partyOrgLook";
	}
	
	/**
	 * 编辑党组织信息
	 * @param id
	 * @param orgIds
	 * @param orgNames
	 * @param nature
	 * @param clickSign
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/socialorg/partyorgedit",method=RequestMethod.GET)
	public String orgedit(String id, String orgIds, String orgNames, String nature, String clickSign, Model model)throws Exception{
		List<SocialPartyOrgChangeInfo> changeDateList = new ArrayList<>();
		List<DeputySecretaryInfo> deputsecList = new ArrayList<>();
		List<PartyInstructorInfo> instructList = new ArrayList<>();
		List<LowerPartyOrg> lowerPartyOrgList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		if(!StringUtils.isEmpty(id)){
			Integer mainId = Integer.parseInt(id);
			
			SocialPartyOrgInfo socialPartyOrgInfo = socialPartyOrgInfoMapper.selectByPrimaryKey(mainId);
			if(socialPartyOrgInfo.getPartyOrgAttachment() != "" && socialPartyOrgInfo.getPartyOrgAttachment().length() > 0){
				AttachmentCommonInfo attachmentCommonInfo = attachmentCommonInfoMapper.selectByPrimaryKey(Integer.parseInt(socialPartyOrgInfo.getPartyOrgAttachment()));
				socialPartyOrgInfo.setFilepartyOrgAttachment(attachmentCommonInfo.getFilename());
			}
			socialPartyOrgInfo.setInstructorUnitTxt(baseService.getDeptNameById(socialPartyOrgInfo.getInstructorUnit()));
			socialPartyOrgInfo.setSecretaryCompanyTxt(baseService.getDeptNameById(socialPartyOrgInfo.getSecretaryCompany()));
			//查询下级党组织的相关信息
			Example exampleByLp = new Example(LowerPartyOrg.class);
			exampleByLp.createCriteria().andEqualTo("partyOrgId", mainId).andEqualTo("type", 1).andEqualTo("status", 1);
			lowerPartyOrgList = lowerPartyOrgMapper.selectByExample(exampleByLp);
			//查询指导人的相关信息
			Example exampleByIn = new Example(PartyInstructorInfo.class);
			exampleByIn.createCriteria().andEqualTo("partyOrgId", mainId).andEqualTo("type", 1).andEqualTo("status", 1);
			instructList = partyInstructorInfoMapper.selectByExample(exampleByIn);
			for(PartyInstructorInfo insInfo : instructList){
				//该指导人员的下的组织
				String insOrgs = insInfo.getInstructOrgs();
				if(StringUtils.isNotEmpty(insOrgs)){
					insInfo.setOrgIdList(Arrays.asList(insOrgs.split(",")));
				}
			}
			//查看换届的相关信息
			Example example = new Example(SocialPartyOrgChangeInfo.class);
			example.createCriteria().andEqualTo("socialPartyOrgId", mainId).andEqualTo("status", 1);
			changeDateList = socialPartyOrgChangeInfoMapper.selectByExample(example);
			for(SocialPartyOrgChangeInfo upocInfo : changeDateList){
				AttachmentCommonInfo acInfo = attachmentCommonInfoMapper.selectByPrimaryKey(upocInfo.getChangeAttachmentId());
				if(acInfo != null){
					upocInfo.setChangeAttachmentName(acInfo.getFilename());
				}
				if(upocInfo.getChangeTime() != null){
					upocInfo.setChangeTimeTxt(sdf.format(upocInfo.getChangeTime()));
				}
			}
			//查询党副的相关信息
			Example dsexample = new Example(DeputySecretaryInfo.class);
			dsexample.createCriteria().andEqualTo("partyOrgId", mainId).andEqualTo("type", "1").andEqualTo("status", 1);
			deputsecList = deputySecretaryInfoMapper.selectByExample(dsexample);
			for(DeputySecretaryInfo dsInfo : deputsecList){
				if(dsInfo.getDeputySecretaryBirthday() != null){
					dsInfo.setDeputySecretaryBirthdayTxt(sdf.format(dsInfo.getDeputySecretaryBirthday()));
				}
			}
			//组装建立党组织的单位字符串
			String  orgNames1 = "";
			String  orgIds1 = "";
			Example example1 = new Example(SocialOrgInfo.class);
			example1.createCriteria().andEqualTo("socialPartyOrgId", mainId).andEqualTo("status", 2);
			List<SocialOrgInfo> orgInfoList = socialOrgInfoMapper.selectByExample(example1);
			for(SocialOrgInfo info : orgInfoList){
				if(orgNames1 == ""){
					orgNames1 = info.getName();
					orgIds1 = info.getId()+"";
				}else{
					orgNames1 = orgNames1 +","+ info.getName();
					orgIds1 = orgIds1 + "," + info.getId();
				}
			}
			if(socialPartyOrgInfo.getPartyOrgTime() != null){
				socialPartyOrgInfo.setPartyOrgTimeTxt(sdf.format(socialPartyOrgInfo.getPartyOrgTime()));
			}
			if(socialPartyOrgInfo.getSecretaryBirthday() != null){
				socialPartyOrgInfo.setSecretaryBirthdayTxt(sdf.format(socialPartyOrgInfo.getSecretaryBirthday()));
			}
			String[] orgIdArray = orgIds1.split(",");
			SocialOrgPmbrCount opcInfo = socialOrgPmbrCountMapper.getPmbrCount(orgIdArray);
			this.convertData(orgIds1, orgNames1, model);
			model.addAttribute("main", socialPartyOrgInfo);
			model.addAttribute("changeDateList", changeDateList);
			model.addAttribute("deputsecList", deputsecList);
			model.addAttribute("instructList", instructList);
			model.addAttribute("lowerPartyList", lowerPartyOrgList);
			model.addAttribute("instructSize", instructList.size());
			model.addAttribute("orgNames", orgNames1);
			model.addAttribute("orgIds", orgIds1);
			model.addAttribute("opcInfo", opcInfo);
			model.addAttribute("nature", nature);
			model.addAttribute("clickSign", clickSign);
		}else{
			String[] orgIdArray = orgIds.split(",");
			SocialOrgPmbrCount opcInfo = socialOrgPmbrCountMapper.getPmbrCount(orgIdArray);
			SocialPartyOrgInfo main = new SocialPartyOrgInfo();
			this.convertData(orgIds, new String(orgNames.getBytes("iso-8859-1"),"utf-8"), model);
			model.addAttribute("main", main);
			model.addAttribute("orgIds", orgIds);
			model.addAttribute("changeDateList", changeDateList);
			model.addAttribute("deputsecList", deputsecList);
			model.addAttribute("instructList", instructList);
			model.addAttribute("lowerPartyList", lowerPartyOrgList);
			model.addAttribute("instructSize", instructList.size());
			model.addAttribute("orgNames", new String(orgNames.getBytes("iso-8859-1"),"utf-8"));
			model.addAttribute("opcInfo", opcInfo);
			model.addAttribute("nature", nature);
		}
		editPage(model,nature);
		return "socialorg/partyOrgEdit"; 
	}
	
	/**
	 * 保存党组织信息
	 * @param partyOrgIds
	 * @param socialPartyOrgInfo
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/socialorg/partyorgedit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> orgSave(String partyOrgIds, SocialPartyOrgInfo socialPartyOrgInfo, HttpServletRequest request, Model model)throws Exception{
		List<SocialPartyOrgChangeInfo> pociList = new ArrayList<>();
		List<DeputySecretaryInfo> dsiList = new ArrayList<>();
		List<PartyInstructorInfo> instructList = new ArrayList<>();
		List<LowerPartyOrg> lowerPartyOrgList = new ArrayList<>();
		String msg = "";
		String[] orgInfoArray = partyOrgIds.split(",");
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		
		//下级党组织信息
		if(!socialPartyOrgInfo.getLowerPartyList().isEmpty()){
			lowerPartyOrgList = JSON.parseArray(socialPartyOrgInfo.getLowerPartyList(), LowerPartyOrg.class);
			for(int i = 0; i < lowerPartyOrgList.size(); i++){
				lowerPartyOrgList.get(i).setCreater(baseService.getUserName());
				lowerPartyOrgList.get(i).setCreateTime(new Date());
				lowerPartyOrgList.get(i).setCreateOrg(baseService.getCurrentUserDeptId());
				lowerPartyOrgList.get(i).setStatus("1");
			}
		}
		//指导员信息
		if(!socialPartyOrgInfo.getInstructList().isEmpty()){
			instructList = JSON.parseArray(socialPartyOrgInfo.getInstructList(), PartyInstructorInfo.class);
			for(int i = 0; i < instructList.size(); i++){
				instructList.get(i).setCreater(baseService.getUserName());
				instructList.get(i).setCreateTime(new Date());
				instructList.get(i).setCreateOrg(baseService.getCurrentUserDeptId());
				instructList.get(i).setStatus("1");
			}
		}
		//换届信息
		if(!socialPartyOrgInfo.getChangeList().isEmpty()){
			pociList = JSON.parseArray(socialPartyOrgInfo.getChangeList(), SocialPartyOrgChangeInfo.class);
			for(int i = 0; i < pociList.size(); i++){
				if(pociList.get(i).getChangeTimeTxt() != null && pociList.get(i).getChangeTimeTxt() != ""){
					pociList.get(i).setChangeTime(sdf.parse(pociList.get(i).getChangeTimeTxt()));
				}
				pociList.get(i).setCreater(baseService.getUserName());
				pociList.get(i).setCreateTime(new Date());
				pociList.get(i).setStatus("1");
			}
		}
		
		//党副信息
		if(!socialPartyOrgInfo.getDeputySecretaryList().isEmpty()){
			dsiList = JSON.parseArray(socialPartyOrgInfo.getDeputySecretaryList(), DeputySecretaryInfo.class);
			for(int j = 0; j < dsiList.size(); j++){
				if(dsiList.get(j).getDeputySecretaryBirthdayTxt() != null && dsiList.get(j).getDeputySecretaryBirthdayTxt() != ""){
					dsiList.get(j).setDeputySecretaryBirthday(sdf.parse(dsiList.get(j).getDeputySecretaryBirthdayTxt()));
				}
				dsiList.get(j).setCreateOrg(baseService.getCurrentUserDeptId());
				dsiList.get(j).setCreater(baseService.getUserName());
				dsiList.get(j).setCreateTime(new Date());
				dsiList.get(j).setStatus("1");
			}
		}
		Integer mainId = socialPartyOrgInfo.getId();
		if(socialPartyOrgInfo.getReportHigher() == 1){
			socialPartyOrgInfo.setStatus("5");//上报申请中
			msg = "上报成功";
		}else{
			socialPartyOrgInfo.setStatus("1");//临时保存
			msg = "新增成功";
		}
		if(mainId == null || StringUtils.isEmpty(mainId.toString())){
			
			try {
				partyOrgService.addOrg(socialPartyOrgInfo, pociList, dsiList, instructList, lowerPartyOrgList, orgInfoArray);
				return returnMsg(1, msg);
			} catch (Exception e) {
				logger.debug("新增社会组织信息时出现异常:{}", e.getMessage(),e);
				return returnMsg(0, "新增失败:" + e.getMessage());
			}
		}else{
			try {
				partyOrgService.updateOrg(socialPartyOrgInfo, pociList, dsiList, instructList, lowerPartyOrgList, orgInfoArray);
				if(socialPartyOrgInfo.getReportHigher() == 0){
					msg = "修改成功";
				}
				return returnMsg(1, msg);
			} catch (Exception e) {
				logger.debug("修改社会组织信息时出现异常:{}", e.getMessage(),e);
				return returnMsg(0, "修改失败:" + e.getMessage());
			}
		}
	}
	/**
	 * 党组织信息退回
	 * @param model
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/socialorg/partyOrgSetStatus",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> orgSetStatus(Model model,String id,String status){
		SocialPartyOrgInfo info = socialPartyOrgInfoMapper.selectByPrimaryKey(Integer.parseInt(id));
		info.setStatus(status);
		socialPartyOrgInfoMapper.updateByPrimaryKeySelective(info);
		return returnMsg(1, "操作成功!");
	}
	
	/**
	 * 党组织信息上报审核
	 * @param model
	 * @param partyOrgIds
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/socialorg/partyOrgsSetStatus",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> orgsSetStatus(Model model,String partyOrgIds,String status){
		try{
			partyOrgService.orgsSetStatus(partyOrgIds, status);
			return returnMsg(1, "操作成功!");
		}catch(Exception e){
			e.printStackTrace();
			return returnMsg(0, "操作失败!");
		}
	}
	
	/**
	 * 删除党组织信息
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/socialorg/partyorgdelete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> orgdelete(Model model,String id){
		try {
			SocialPartyOrgInfo info = socialPartyOrgInfoMapper.selectByPrimaryKey(Integer.parseInt(id));
			info.setStatus("0");
			socialPartyOrgInfoMapper.updateByPrimaryKeySelective(info);
			//删除日志
			logService.saveLog(LOG_OPERATION_TYPE.DELETE.toString(), 
					logService.getDetailInfo("log.socialorg.delete",
							baseService.getUserName(),info.getPartyOrgName()));
			
			return returnMsg(1, "删除成功!");
		} catch (Exception e) {
			logger.debug("删除非公党信息时出现异常:{}", e.getMessage(),e);
			return returnMsg(0, "删除失败："+e.getMessage());
		}
	}
	
	
	/**
	 * 撤销党组织页面
	 * @param id
	 * @param method
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/socialorg/partyorgcancel",method=RequestMethod.GET)
	public String orgcancel(String id,String method,Model model){
		Example example = new Example(CancelReasonInfo.class);
		example.setOrderByClause("createTime desc");
		example.createCriteria().andEqualTo("partyOrgId", Integer.parseInt(id)).andEqualTo("type", '1').andEqualTo("status", 1);
		List<CancelReasonInfo> unpublicOrgCancelRecords = cancelReasonInfoMapper.selectByExample(example);
		if(!CollectionUtils.isEmpty(unpublicOrgCancelRecords)){
			model.addAttribute("reason", unpublicOrgCancelRecords.get(0));
		}
		
		model.addAttribute("id", id);
		model.addAttribute("method", method);
		return "socialorg/partyOrgCancel";
	}
	
	/**
	 *  撤销党组织页面中的文件展示
	 * @param id
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/socialorg/partyShowlFile",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> showFile(String id, int page,int rows){
		Map<String,Object> result = new HashMap<>();
		int start = (page-1)*rows;
		Example example = new Example(AttachmentCommonInfo.class);
		example.createCriteria().andEqualTo("mainTableId", id).andEqualTo("modular", 1).andEqualTo("type", 1).andEqualTo("action", 1).andEqualTo("status", 1);
		int total = attachmentCommonInfoMapper.selectCountByExample(example);
		if(total == 0){
			result.put("rows", new ArrayList<>());
			result.put("total", total);
			return result;
		}
		List<AttachmentCommonInfo> dataFiles = attachmentCommonInfoMapper.selectByExampleAndRowBounds(example, new RowBounds(start, rows));
		result.put("total", total);
		result.put("rows", dataFiles);
		return result;
	}
	
	/**
	 * 撤销党组织信息
	 * @param model
	 * @param id
	 * @param remarks
	 * @return
	 */
	@RequestMapping(value="/socialorg/partyorgcancel",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> orgCancel(Model model,String id,String remarks){
		
		try {
			CancelReasonInfo reason = new CancelReasonInfo();
			reason.setPartyOrgId(Integer.parseInt(id));
			reason.setType("1");
			reason.setReason(remarks);
			reason.setCreateTime(new Date());
			reason.setCreator(baseService.getUserName());
			reason.setStatus(CommonConstants.STATUS_FLAG_VALID);
			SocialPartyOrgInfo info = new SocialPartyOrgInfo();
			info.setId(Integer.parseInt(id));
			info.setStatus("3");
			
			partyOrgService.cancelOrg(reason, info);
			SocialPartyOrgInfo main = socialPartyOrgInfoMapper.selectByPrimaryKey(id);
			//撤销日志
			logService.saveLog(LOG_OPERATION_TYPE.DELETE.toString(), 
					logService.getDetailInfo("log.socialorg.cancel",
							baseService.getUserName(),main.getPartyOrgName()));
			return returnMsg(1, "撤销成功，等待上级部门审核！");
		} catch (Exception e) {
			logger.debug("撤销非公党组织信息时出现异常:{}", e.getMessage(),e);
			return returnMsg(0, "撤销失败:" + e.getMessage());
		}
		
	}
	/**
	 * 取消撤销
	 * @param model
	 * @param id
	 * @param remarks
	 * @return
	 */
	@RequestMapping(value="/socialorg/partyNocancel",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> nocancel(Model model,String id,String remarks){
		
		SocialPartyOrgInfo main = new SocialPartyOrgInfo();
		main.setId(Integer.parseInt(id));
		main.setStatus("2");
//		main.setUpdateTime(new Date());
//		main.setUpdator(baseService.getUserName());
		partyOrgService.nocancelOrg(main);
		try {
			
			main = socialPartyOrgInfoMapper.selectByPrimaryKey(id);
			//取消撤销日志
			logService.saveLog(LOG_OPERATION_TYPE.DELETE.toString(), 
					logService.getDetailInfo("log.socialorg.nocancel",
							baseService.getUserName(),main.getPartyOrgName()));
			return returnMsg(1, "取消撤销该组织成功！");
		} catch (Exception e) {
			logger.debug("取消撤销党组织信息时出现异常:{}", e.getMessage(),e);
			return returnMsg(0, "撤销失败:" + e.getMessage());
		}
	}
	/**
	 * 审核
	 * @param model
	 * @param partyOrgIds
	 * @param remarks
	 * @return
	 */
	@RequestMapping(value="/socialorg/partyCancelok",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> cancelok(Model model,String partyOrgIds,String remarks){
		try {
			String[] partyOrgIdArray = partyOrgIds.split(",");
			for(int i = 0; i < partyOrgIdArray.length; i++){
				SocialPartyOrgInfo main = socialPartyOrgInfoMapper.selectByPrimaryKey(Integer.parseInt(partyOrgIdArray[i]));
				main.setStatus("4");
				socialPartyOrgInfoMapper.updateByPrimaryKeySelective(main);
				
				//撤销审核通过日志
				logService.saveLog(LOG_OPERATION_TYPE.MODIFY.toString(), 
						logService.getDetailInfo("log.socialorg.cancelok",
								baseService.getUserName(),main.getPartyOrgName()));
			}
			return returnMsg(1, "审核通过，撤销组织成功！");
		} catch (Exception e) {
			logger.debug("撤销审核通过党组织信息时出现异常:{}", e.getMessage(),e);
			return returnMsg(0, "撤销失败:" + e.getMessage());
		}
		
	}
	
	
	/**
	 * 文件上传页面
	 * @param attachmentCommonInfo
	 * @param sign
	 * @param method
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/socialorg/uploadFile",method=RequestMethod.GET)     
	public String fileUploadShow(AttachmentCommonInfo attachmentCommonInfo,String sign, String method,Model model) throws UnsupportedEncodingException{
		attachmentCommonInfo.setFilename(new String(attachmentCommonInfo.getFilename().getBytes("iso-8859-1"),"utf-8"));
		model.addAttribute("main", attachmentCommonInfo); 
		model.addAttribute("sign", sign);
		model.addAttribute("method", method);
		return "unpublic/partyOrgFileUpload";
	}
	/**
	 * 党组织上传文件展示
	 * @param id
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/socialorg/partycancelFile",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> uploadFile(String id, int page,int rows){
		Map<String,Object> result = new HashMap<>();
		int start = (page-1)*rows;
		Example example = new Example(AttachmentCommonInfo.class);
		example.createCriteria().andEqualTo("id", id).andEqualTo("status", 1);
		int total = attachmentCommonInfoMapper.selectCountByExample(example);
		if(total == 0){
			result.put("rows", new ArrayList<>());
			result.put("total", total);
			return result;
		}
		List<AttachmentCommonInfo> dataFiles = attachmentCommonInfoMapper.selectByExampleAndRowBounds(example, new RowBounds(start, rows));
		result.put("total", total);
		result.put("rows", dataFiles);
		return result;
	}
	
	/**
	 * 展示党员基本信息页面
	 * @param orgIds
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/socialorg/showPartyInfo",method=RequestMethod.GET)
	public String jumpShowPartyInfo(String orgIds, Model model){
		
		model.addAttribute("orgIds", orgIds);
		return "socialorg/showPartyInfoPop";
	}
	
	/**
	 *  展示党员基本信息页面
	 * @param response
	 * @param page
	 * @param rows
	 * @param params
	 */
	@RequestMapping(value="/socialorg/showPartyInfo",method=RequestMethod.POST)
	public void showPartyInfo(HttpServletResponse response, int page, int rows, QueryPmbrParams params){
		String[] orgIdsArray = params.getOrgIds().split(",");
		PageHelper.startPage(page, rows, true);
		List<SocialOrgPmbrInfo> list = socialOrgPmbrInfoMapper.queryListByOrgId(params.getName(), orgIdsArray);
		for (SocialOrgPmbrInfo socialOrgPmbrInfo : list) {
			socialOrgPmbrInfo.setGender(dict.getValueByCode(CommonConstants.GENDER, socialOrgPmbrInfo.getGender()));
			socialOrgPmbrInfo.setPartymbrGroupInSocialorgIs(dict.getValueByCode(CommonConstants.YES_NO, socialOrgPmbrInfo.getPartymbrGroupInSocialorgIs()));
			socialOrgPmbrInfo.setPartymbrInSocialorgIs(dict.getValueByCode(CommonConstants.YES_NO, socialOrgPmbrInfo.getPartymbrInSocialorgIs()));
			socialOrgPmbrInfo.setEducation(dict.getValueByCode(CommonConstants.FINAL_EDUCATION, socialOrgPmbrInfo.getEducation()));
		}
		PageUtil.returnPage(response, new PageInfo<SocialOrgPmbrInfo>(list));
	}
	
	/**
	 *  展示组织信息弹窗
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/social/showOrgInfoPop",method=RequestMethod.GET)
	public String jumpshowOrgInfoPop(Model model){
		
		return "socialorg/socialOrgInfoPop";
	}
	
	/**
	 * 展示组织信息弹窗
	 * @param response
	 * @param page
	 * @param rows
	 * @param params
	 */
	@RequestMapping(value="/social/showOrgInfoPop",method=RequestMethod.POST)
	public void showOrgInfoPop(HttpServletResponse response, int page, int rows, QueryPmbrParams params){
		if(!baseService.isQuWeiDept()){
			params.setCreateOrg(baseService.getCurrentUserDeptId());
		}
		PageHelper.startPage(page, rows, true);
		List<SocialOrgInfo> list = socialOrgInfoMapper.getList(params);
		for (SocialOrgInfo socialOrgInfo : list) {
			socialOrgInfo.setCreateOrgTxt(baseService.getDeptNameById(socialOrgInfo.getCreateOrg()));
		}
		PageUtil.returnPage(response, new PageInfo<SocialOrgInfo>(list));
	}
	
	/**
	 * 初始化参数
	 * @param model
	 */
	private void initPage(Model model){
			
		List<DataDictionary> yesNoList = dict.findByDmm(CommonConstants.YES_NO);
		List<DataDictionary> partyOrgClassList = dict.findByDmm(CommonConstants.PARTY_ORG_CLASS);
		List<DataDictionary> partyOrgFormList = dict.findByDmm(CommonConstants.PARTY_ORG_FORM);
		List<DataDictionary> partyorgStatusList = dict.findByDmm(CommonConstants.UNPUBLIC_ORG_STATUS);
		List<DataDictionary> operatorList = dict.findByDmm(CommonConstants.PARTY_ORG_OPERATOR);
		model.addAttribute("yesNoList", yesNoList);
		model.addAttribute("partyOrgClassList", partyOrgClassList);
		model.addAttribute("partyOrgFormList", partyOrgFormList);
		model.addAttribute("partyorgStatusList", partyorgStatusList);
		model.addAttribute("operatorList", operatorList);
		model.addAttribute("createOrgName", baseService.getDeptNameById(baseService.getCurrentUserDeptId()));
		model.addAttribute("createOrgId", baseService.getCurrentUserDeptId());
	}
	/**
	 * 编辑页面初始化参数
	 * @param model
	 */
	private void editPage(Model model, String nature){
		
		List<DataDictionary> yesNoList = dict.findByDmm(CommonConstants.YES_NO);
		
		List<DataDictionary> partyOrgClassList = dict.findByDmm(CommonConstants.PARTY_ORG_CLASS);
		List<DataDictionary> partyOrgFormList = dict.findByDmm(CommonConstants.PARTY_ORG_FORM);
		List<DataDictionary> noEstablishPartyOrgReasonList = dict.findByDmm(CommonConstants.NO_ESTABLISH_PARTY_ORG_REASON);
		List<DataDictionary> sourceList = dict.findByDmm(CommonConstants.SOURCE);
		List<DataDictionary> partyTypeList = dict.findByDmm(CommonConstants.PARTY_SECRETARIES_AND_COMMISSION_TYPE);
		List<DataDictionary> finalEducationList = dict.findByDmm(CommonConstants.FINAL_EDUCATION);
		List<DataDictionary> genderList = dict.findByDmm(CommonConstants.GENDER);
		List<DataDictionary> heightPartyOrgList = dict.findByDmm(CommonConstants.HEIGHT_PARTY_ORG);
		if("0201".endsWith(nature) || "0204".endsWith(nature) ){
			DataDictionary dictionary = new DataDictionary();
			dictionary.setCode("4");
			dictionary.setValue("由校长兼任");
		}
		List<DataDictionary> otherInfoManagerList = dict.findByDmm(CommonConstants.OTHER_INFO_MANAGER);
		model.addAttribute("yesNoList", yesNoList);
		model.addAttribute("partyOrgClassList", partyOrgClassList);
		model.addAttribute("partyOrgFormList", partyOrgFormList);
		model.addAttribute("noEstablishPartyOrgReasonList", noEstablishPartyOrgReasonList);
		model.addAttribute("sourceList", sourceList);
		model.addAttribute("partyTypeList", partyTypeList);
		model.addAttribute("finalEducationList", finalEducationList);
		model.addAttribute("genderList", genderList);
		model.addAttribute("heightPartyOrgList", heightPartyOrgList);
		model.addAttribute("otherInfoManagerList", otherInfoManagerList);

	}
	
	/**
	 * 转换数据集合
	 * @param rows
	 */
	private void convertRows(List<SocialPartyOrgInfo> rows) {
		for (SocialPartyOrgInfo info : rows) {
			this.convertRow(info);
		}
	}

	/**
	 * 转换单条数据
	 * @param info
	 */
	private void convertRow(SocialPartyOrgInfo main) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		main.setPartyOrgFormTxt(dict.getValueByCode(CommonConstants.PARTY_ORG_FORM,main.getPartyOrgForm()));
		main.setPartyOrgTypeTxt(dict.getValueByCode(CommonConstants.PARTY_ORG_CLASS,main.getPartyOrgType()));
		main.setStatusTxt(dict.getValueByCode(CommonConstants.UNPUBLIC_ORG_STATUS,main.getStatus()));
		main.setSecretarySourceTxt(dict.getValueByCode(CommonConstants.SOURCE,main.getSecretarySource()));
		if(main.getPartyOrgTime() != null){
			main.setPartyOrgTimeTxt(sdf.format(main.getPartyOrgTime()));
		}
		main.setInstructorUnitTxt(baseService.getDeptNameById(main.getInstructorUnit()));
		main.setSecretaryCompanyTxt(baseService.getDeptNameById(main.getSecretaryCompany()));
	}

	/**
	 * 数据转换
	 * @param info
	 */
	private void convertData(String orgIds, String orgNames, Model model){
		List<DataDictionary> unitList = new ArrayList<>();
		String[] orgIdArray = orgIds.split(",");
		String[] orgNameArray = orgNames.split(",");
		if(orgIdArray[0].length() > 0 && orgNameArray[0].length() > 0){
			for(int i = 0; i < orgIdArray.length; i++){
				DataDictionary data = new DataDictionary();
				data.setCode(orgIdArray[i]);
				data.setValue(orgNameArray[i]);
				unitList.add(data);
			}
		}
		model.addAttribute("unitList", unitList);
	}
	
	/**
	 * 返回信息及标示
	 * @param status
	 * @param msg
	 * @return
	 */
	private Map<String,Object> returnMsg(int status,String msg){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", status);
		result.put("msg", msg);
		return result;
	}
}
