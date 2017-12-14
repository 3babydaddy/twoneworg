package com.tf.base.unpublic.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tf.base.common.constants.CommonConstants;
import com.tf.base.common.constants.CommonConstants.LOG_OPERATION_TYPE;
import com.tf.base.common.domain.DataDictionary;
import com.tf.base.common.domain.DictionaryRepository;
import com.tf.base.common.service.BaseService;
import com.tf.base.common.service.LogService;
import com.tf.base.common.utils.PageUtil;
import com.tf.base.unpublic.domain.AttachmentCommonInfo;
import com.tf.base.unpublic.domain.CancelReasonInfo;
import com.tf.base.unpublic.domain.DeputySecretaryInfo;
import com.tf.base.unpublic.domain.QueryPmbrParams;
import com.tf.base.unpublic.domain.UnpublicOrgInfo;
import com.tf.base.unpublic.domain.UnpublicOrgPmbrCount;
import com.tf.base.unpublic.domain.UnpublicOrgPmbrInfo;
import com.tf.base.unpublic.domain.UnpublicPartyOrgChangeInfo;
import com.tf.base.unpublic.domain.UnpublicPartyOrgInfo;
import com.tf.base.unpublic.persistence.AttachmentCommonInfoMapper;
import com.tf.base.unpublic.persistence.CancelReasonInfoMapper;
import com.tf.base.unpublic.persistence.DeputySecretaryInfoMapper;
import com.tf.base.unpublic.persistence.UnpublicOrgInfoMapper;
import com.tf.base.unpublic.persistence.UnpublicOrgPmbrCountMapper;
import com.tf.base.unpublic.persistence.UnpublicOrgPmbrInfoMapper;
import com.tf.base.unpublic.persistence.UnpublicPartyOrgChangeInfoMapper;
import com.tf.base.unpublic.persistence.UnpublicPartyOrgInfoMapper;
import com.tf.base.unpublic.service.UnPublicPartyOrgService;

import tk.mybatis.mapper.entity.Example;

@Controller
public class UnPublicPartyOrgController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DictionaryRepository dict;
	@Autowired
	private BaseService baseService;
	@Autowired
	private LogService logService;
	@Autowired
	private UnPublicPartyOrgService unPublicPartyOrgService;
	@Autowired
	private UnpublicOrgInfoMapper unpublicOrgInfoMapper;
	@Autowired
	private UnpublicPartyOrgInfoMapper unpublicPartyOrgInfoMapper;
	@Autowired
	private AttachmentCommonInfoMapper attachmentCommonInfoMapper;
	@Autowired
	private UnpublicPartyOrgChangeInfoMapper unpublicPartyOrgChangeInfoMapper;
	@Autowired
	private CancelReasonInfoMapper cancelReasonInfoMapper;
	@Autowired
	private DeputySecretaryInfoMapper deputySecretaryInfoMapper;
	@Autowired
	private UnpublicOrgPmbrCountMapper unpublicOrgPmbrCountMapper;
	@Autowired
	private UnpublicOrgPmbrInfoMapper unpublicOrgPmbrInfoMapper;
	
	/**
	 * 初始化页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/unpublic/partyorglist",method=RequestMethod.GET)
	public String orginit(Model model){
		
		initPage(model);
		return "unpublic/partyOrgList";
	}
	
	/**
	 * 党组织信息列表展示
	 * @param params
	 * @param page
	 * @param rows
	 * @param response
	 */
	@RequestMapping(value="/unpublic/partyorglist",method=RequestMethod.POST)
	public void orglist(UnpublicPartyOrgInfo params,
			int page,int rows,HttpServletResponse response){
		String deptId = baseService.getCurrentUserDeptId();
		logger.debug("当前登录用户部门ID:===========>" + deptId + " ,是否为区委部门?" + baseService.isQuWeiDept());
		if(!baseService.isQuWeiDept()){
			params.setCreateOrg(deptId);
		}
		PageHelper.startPage(page, rows, true);
		List<UnpublicPartyOrgInfo> list = unpublicPartyOrgInfoMapper.queryList(params);
		this.convertRows(list);
		PageUtil.returnPage(response, new PageInfo<UnpublicPartyOrgInfo>(list));
		
		
	}
	
	/**
	 * 查看党组织信息
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/unpublic/partyOrglook",method=RequestMethod.GET)
	public String orglook(String id,Model model){
		List<UnpublicPartyOrgChangeInfo> changeDateList = new ArrayList<>();
		List<DeputySecretaryInfo> deputsecList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		Integer mainId = Integer.parseInt(id);
		
		UnpublicPartyOrgInfo unpublicPartyOrgInfo = unpublicPartyOrgInfoMapper.selectByPrimaryKey(mainId);
		if(unpublicPartyOrgInfo.getPartyOrgAttachment() != "" && unpublicPartyOrgInfo.getPartyOrgAttachment().length() > 0){
			AttachmentCommonInfo attachmentCommonInfo = attachmentCommonInfoMapper.selectByPrimaryKey(Integer.parseInt(unpublicPartyOrgInfo.getPartyOrgAttachment()));
			unpublicPartyOrgInfo.setFilepartyOrgAttachment(attachmentCommonInfo.getFilename());
		}
		unpublicPartyOrgInfo.setInstructorUnitTxt(baseService.getDeptNameById(unpublicPartyOrgInfo.getInstructorUnit()));
		unpublicPartyOrgInfo.setSecretaryCompanyTxt(baseService.getDeptNameById(unpublicPartyOrgInfo.getSecretaryCompany()));
		unpublicPartyOrgInfo.setBelongUnitTxt(baseService.getDeptNameById(unpublicPartyOrgInfo.getBelongUnit()));
		
		//查看换届的相关信息
		Example example = new Example(UnpublicPartyOrgChangeInfo.class);
		example.createCriteria().andEqualTo("socialPartyOrgId", mainId).andEqualTo("status", 1);
		changeDateList = unpublicPartyOrgChangeInfoMapper.selectByExample(example);
		for(UnpublicPartyOrgChangeInfo upocInfo : changeDateList){
			AttachmentCommonInfo acInfo = attachmentCommonInfoMapper.selectByPrimaryKey(upocInfo.getChangeAttachmentId());
			upocInfo.setChangeAttachmentName(acInfo.getFilename());
			upocInfo.setChangeTimeTxt(sdf.format(upocInfo.getChangeTime()));
		}
		//查询党副的相关信息
		Example dsexample = new Example(DeputySecretaryInfo.class);
		dsexample.createCriteria().andEqualTo("partyOrgId", mainId).andEqualTo("type", "2").andEqualTo("status", 1);
		deputsecList = deputySecretaryInfoMapper.selectByExample(dsexample);
		for(DeputySecretaryInfo dsInfo : deputsecList){
			dsInfo.setDeputySecretaryBirthdayTxt(sdf.format(dsInfo.getDeputySecretaryBirthday()));
		}
		//组装建立党组织的单位字符串
		String orgNames = "";
		String orgIds1 = "";
		Example example1 = new Example(UnpublicOrgInfo.class);
		example1.createCriteria().andEqualTo("unpublicPartyOrgId", mainId);
		List<UnpublicOrgInfo> orgInfoList = unpublicOrgInfoMapper.selectByExample(example1);
		for(UnpublicOrgInfo info : orgInfoList){
			if(orgNames == ""){
				orgNames = info.getName();
				orgIds1 = info.getId()+"";
			}else{
				orgNames = orgNames +","+ info.getName();
				orgIds1 = orgIds1 + "," + info.getId();
			}
		}
		//时间的类型转换
		if(unpublicPartyOrgInfo.getPartyOrgTime() != null){
			unpublicPartyOrgInfo.setPartyOrgTimeTxt(sdf.format(unpublicPartyOrgInfo.getPartyOrgTime()));
		}
		if(unpublicPartyOrgInfo.getSecretaryBirthday() != null){
			unpublicPartyOrgInfo.setSecretaryBirthdayTxt(sdf.format(unpublicPartyOrgInfo.getSecretaryBirthday()));
		}
		String[] orgIdArray = orgIds1.split(",");
		UnpublicOrgPmbrCount opcInfo = unpublicOrgPmbrCountMapper.getPmbrCount(orgIdArray);
		model.addAttribute("main", unpublicPartyOrgInfo);
		model.addAttribute("changeDateList", changeDateList);
		model.addAttribute("deputsecList", deputsecList);
		model.addAttribute("orgNames", orgNames);
		model.addAttribute("opcInfo", opcInfo);
		
		editPage(model);
		
		//查看日志
		logService.saveLog(LOG_OPERATION_TYPE.VIEW.toString(), 
				logService.getDetailInfo("log.unpublic.view",
						baseService.getUserName(),unpublicPartyOrgInfo.getPartyOrgName()));
		return "unpublic/partyOrgLook";
	}
	
	/**
	 * 编辑党组织信息
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/unpublic/partyorgedit",method=RequestMethod.GET)
	public String orgedit(String id, String orgIds, String orgNames, Model model)throws Exception{
		List<UnpublicPartyOrgChangeInfo> changeDateList = new ArrayList<>();
		List<DeputySecretaryInfo> deputsecList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		if(!StringUtils.isEmpty(id)){
			Integer mainId = Integer.parseInt(id);
			
			UnpublicPartyOrgInfo unpublicPartyOrgInfo = unpublicPartyOrgInfoMapper.selectByPrimaryKey(mainId);
			if(unpublicPartyOrgInfo.getPartyOrgAttachment() != "" && unpublicPartyOrgInfo.getPartyOrgAttachment().length() > 0){
				AttachmentCommonInfo attachmentCommonInfo = attachmentCommonInfoMapper.selectByPrimaryKey(Integer.parseInt(unpublicPartyOrgInfo.getPartyOrgAttachment()));
				unpublicPartyOrgInfo.setFilepartyOrgAttachment(attachmentCommonInfo.getFilename());
			}
			unpublicPartyOrgInfo.setInstructorUnitTxt(baseService.getDeptNameById(unpublicPartyOrgInfo.getInstructorUnit()));
			unpublicPartyOrgInfo.setSecretaryCompanyTxt(baseService.getDeptNameById(unpublicPartyOrgInfo.getSecretaryCompany()));
			unpublicPartyOrgInfo.setBelongUnitTxt(baseService.getDeptNameById(unpublicPartyOrgInfo.getBelongUnit()));
			
			//查看换届的相关信息
			Example example = new Example(UnpublicPartyOrgChangeInfo.class);
			example.createCriteria().andEqualTo("socialPartyOrgId", mainId).andEqualTo("status", 1);
			changeDateList = unpublicPartyOrgChangeInfoMapper.selectByExample(example);
			for(UnpublicPartyOrgChangeInfo upocInfo : changeDateList){
				AttachmentCommonInfo acInfo = attachmentCommonInfoMapper.selectByPrimaryKey(upocInfo.getChangeAttachmentId());
				upocInfo.setChangeAttachmentName(acInfo.getFilename());
				upocInfo.setChangeTimeTxt(sdf.format(upocInfo.getChangeTime()));
			}
			//查询党副的相关信息
			Example dsexample = new Example(DeputySecretaryInfo.class);
			dsexample.createCriteria().andEqualTo("partyOrgId", mainId).andEqualTo("type", "2").andEqualTo("status", 1);
			deputsecList = deputySecretaryInfoMapper.selectByExample(dsexample);
			for(DeputySecretaryInfo dsInfo : deputsecList){
				dsInfo.setDeputySecretaryBirthdayTxt(sdf.format(dsInfo.getDeputySecretaryBirthday()));
			}
			//组装建立党组织的单位字符串
			String  orgNames1 = "";
			String  orgIds1 = "";
			Example example1 = new Example(UnpublicOrgInfo.class);
			example1.createCriteria().andEqualTo("unpublicPartyOrgId", mainId).andEqualTo("status", 2);
			List<UnpublicOrgInfo> orgInfoList = unpublicOrgInfoMapper.selectByExample(example1);
			for(UnpublicOrgInfo info : orgInfoList){
				if(orgNames1 == ""){
					orgNames1 = info.getName();
					orgIds1 = info.getId()+"";
				}else{
					orgNames1 = orgNames1 +","+ info.getName();
					orgIds1 = orgIds1 + "," + info.getId();
				}
			}
			if(unpublicPartyOrgInfo.getPartyOrgTime() != null){
				unpublicPartyOrgInfo.setPartyOrgTimeTxt(sdf.format(unpublicPartyOrgInfo.getPartyOrgTime()));
			}
			if(unpublicPartyOrgInfo.getSecretaryBirthday() != null){
				unpublicPartyOrgInfo.setSecretaryBirthdayTxt(sdf.format(unpublicPartyOrgInfo.getSecretaryBirthday()));
			}
			String[] orgIdArray = orgIds1.split(",");
			UnpublicOrgPmbrCount opcInfo = unpublicOrgPmbrCountMapper.getPmbrCount(orgIdArray);
			model.addAttribute("main", unpublicPartyOrgInfo);
			model.addAttribute("changeDateList", changeDateList);
			model.addAttribute("deputsecList", deputsecList);
			model.addAttribute("orgNames", orgNames1);
			model.addAttribute("orgIds", orgIds1);
			model.addAttribute("opcInfo", opcInfo);
		}else{
			String[] orgIdArray = orgIds.split(",");
			UnpublicOrgPmbrCount opcInfo = unpublicOrgPmbrCountMapper.getPmbrCount(orgIdArray);
			
			UnpublicPartyOrgInfo main = new UnpublicPartyOrgInfo();
			model.addAttribute("main", main);
			model.addAttribute("orgIds", orgIds);
			model.addAttribute("changeDateList", changeDateList);
			model.addAttribute("deputsecList", deputsecList);
			model.addAttribute("orgNames", new String(orgNames.getBytes("iso-8859-1"),"utf-8"));
			model.addAttribute("opcInfo", opcInfo);
		}
		editPage(model);
		return "unpublic/partyOrgEdit"; 
	}
	
	/**
	 * 保存党组织信息
	 * @param model
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/unpublic/partyorgedit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> orgSave(String partyOrgIds, UnpublicPartyOrgInfo unpublicPartyOrgInfo, HttpServletRequest request, Model model)throws Exception{
		List<UnpublicPartyOrgChangeInfo> pociList = new ArrayList<>();
		List<DeputySecretaryInfo> dsiList = new ArrayList<>();
		String msg = "";
		String[] orgInfoArray = partyOrgIds.split(",");
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		//换届信息和党副信息
		for(int i = 0; i < 8; i++){
        	String changeTime = request.getParameter("partymbrInUnpublicNum"+i);
        	String fileId = request.getParameter("partymbrUnderThirtyfiveNum"+i);
        	if(changeTime != null && changeTime.length()>0 && fileId != null && fileId.length()>0){
        		UnpublicPartyOrgChangeInfo changeInfo = new UnpublicPartyOrgChangeInfo();
        		changeInfo.setChangeTime(sdf.parse(changeTime));
        		changeInfo.setChangeAttachmentId(Integer.parseInt(fileId));
        		changeInfo.setCreater(baseService.getUserName());
        		changeInfo.setCreateTime(new Date());
        		changeInfo.setStatus("1");
        		pociList.add(changeInfo);
        	}
        	DeputySecretaryInfo deputySecretaryInfo = new DeputySecretaryInfo();
        	String type = request.getParameter("deputySecretaryType"+i);
        	String name = request.getParameter("deputySecretaryName"+i);
        	String brithday = request.getParameter("deputySecretaryBirthdayTxt"+i);
        	String sex = request.getParameter("deputySecretarySex"+i);
        	String education = request.getParameter("deputySecretaryEducation"+i);
        	String isFullTime = request.getParameter("deputySecretaryIsFullTime"+i);
        	String isBoardOfficer = request.getParameter("isBoardOfficer"+i);
        	if(name != null && name.length() > 0 && brithday != null && brithday.length() > 0){
        		deputySecretaryInfo.setDeputySecretaryType(type);
            	deputySecretaryInfo.setDeputySecretaryName(name);
            	deputySecretaryInfo.setDeputySecretaryBirthday(sdf.parse(brithday));
            	deputySecretaryInfo.setDeputySecretarySex(sex);
            	deputySecretaryInfo.setDeputySecretaryEducation(education);
            	deputySecretaryInfo.setDeputySecretaryIsFullTime(isFullTime);
            	deputySecretaryInfo.setIsBoardOfficer(isBoardOfficer);
            	deputySecretaryInfo.setCreateOrg(baseService.getCurrentUserDeptId());
            	deputySecretaryInfo.setCreater(baseService.getUserName());
            	deputySecretaryInfo.setCreateTime(new Date());
            	deputySecretaryInfo.setStatus("1");
            	dsiList.add(deputySecretaryInfo);
        	}
        	
        }
		Integer mainId = unpublicPartyOrgInfo.getId();
		if(unpublicPartyOrgInfo.getReportHigher() == 1){
			unpublicPartyOrgInfo.setStatus("5");//上报申请中
			msg = "上报成功";
		}else{
			unpublicPartyOrgInfo.setStatus("1");//临时保存
			msg = "新增成功";
		}
		if(mainId == null || StringUtils.isEmpty(mainId.toString())){
			
			try {
				unPublicPartyOrgService.addOrg(unpublicPartyOrgInfo, pociList, dsiList, orgInfoArray);
				return returnMsg(1, msg);
			} catch (Exception e) {
				logger.debug("新增非公组织信息时出现异常:{}", e.getMessage(),e);
				return returnMsg(0, "新增失败:" + e.getMessage());
			}
		}else{
			try {
				unPublicPartyOrgService.updateOrg(unpublicPartyOrgInfo, pociList, dsiList);
				if(unpublicPartyOrgInfo.getReportHigher() == 0){
					msg = "修改成功";
				}
				return returnMsg(1, msg);
			} catch (Exception e) {
				logger.debug("修改非公组织信息时出现异常:{}", e.getMessage(),e);
				return returnMsg(0, "修改失败:" + e.getMessage());
			}
		}
	}
	/**
	 * 党组织信息退回
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/unpublic/partyOrgSetStatus",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> orgSetStatus(Model model,String id,String status){
		UnpublicPartyOrgInfo info = new UnpublicPartyOrgInfo();
		info.setId(Integer.parseInt(id));
		info.setStatus(status);
		
		//info.setUpdateTime(new Date());
		//info.setUpdator(baseService.getUserName());
		unpublicPartyOrgInfoMapper.updateByPrimaryKeySelective(info);
		return returnMsg(1, "操作成功!");
	}
	/**
	 * 党组织信息上报审核
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/unpublic/partyOrgsSetStatus",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> orgsSetStatus(Model model,String partyOrgIds,String status){
		String[] partyOrgArray = partyOrgIds.split(",");
		for(int i = 0; i < partyOrgArray.length; i++){
			if(partyOrgArray[i] != null && partyOrgArray[i].length() > 0){
				UnpublicPartyOrgInfo info = new UnpublicPartyOrgInfo();
				info.setId(Integer.parseInt(partyOrgArray[i]));
				info.setStatus(status);
				//info.setUpdateTime(new Date());
				//info.setUpdator(baseService.getUserName());
				unpublicPartyOrgInfoMapper.updateByPrimaryKeySelective(info);
			}
		}
		return returnMsg(1, "操作成功!");
	}
	
	/**
	 * 删除党组织信息
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/unpublic/partyorgdelete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> orgdelete(Model model,String id){
		UnpublicPartyOrgInfo info = new UnpublicPartyOrgInfo();
		info.setId(Integer.parseInt(id));
		info.setStatus("0");
		try {
			unpublicPartyOrgInfoMapper.updateByPrimaryKeySelective(info);
			//删除日志
			logService.saveLog(LOG_OPERATION_TYPE.DELETE.toString(), 
					logService.getDetailInfo("log.unpublic.delete",
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
	@RequestMapping(value="/unpublic/partyorgcancel",method=RequestMethod.GET)
	public String orgcancel(String id,String method,Model model){
		Example example = new Example(CancelReasonInfo.class);
		example.setOrderByClause("createTime desc");
		example.createCriteria().andEqualTo("partyOrgId", Integer.parseInt(id)).andEqualTo("type", '2').andEqualTo("status", 1);
		List<CancelReasonInfo> unpublicOrgCancelRecords = cancelReasonInfoMapper.selectByExample(example);
		if(!CollectionUtils.isEmpty(unpublicOrgCancelRecords)){
			model.addAttribute("reason", unpublicOrgCancelRecords.get(0));
		}
		
		model.addAttribute("id", id);
		model.addAttribute("method", method);
		return "unpublic/partyOrgCancel";
	}
	/**
	 * 撤销党组织信息
	 * @param model
	 * @param id
	 * @param remarks
	 * @return
	 */
	@RequestMapping(value="/unpublic/partyorgcancel",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> orgCancel(Model model,String id,String remarks){
		
		try {
			CancelReasonInfo reason = new CancelReasonInfo();
			reason.setPartyOrgId(Integer.parseInt(id));
			reason.setType("2");
			reason.setReason(remarks);
			reason.setCreateTime(new Date());
			reason.setCreator(baseService.getUserName());
			reason.setStatus(CommonConstants.STATUS_FLAG_VALID);
			UnpublicPartyOrgInfo info = new UnpublicPartyOrgInfo();
			info.setId(Integer.parseInt(id));
			info.setStatus("3");
			
			unPublicPartyOrgService.cancelOrg(reason, info);
			UnpublicPartyOrgInfo main = unpublicPartyOrgInfoMapper.selectByPrimaryKey(id);
			//撤销日志
			logService.saveLog(LOG_OPERATION_TYPE.DELETE.toString(), 
					logService.getDetailInfo("log.unpublic.cancel",
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
	@RequestMapping(value="/unpublic/partyNocancel",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> nocancel(Model model,String id,String remarks){
		
		UnpublicPartyOrgInfo main = new UnpublicPartyOrgInfo();
		main.setId(Integer.parseInt(id));
		main.setStatus("2");
		unPublicPartyOrgService.nocancelOrg(main);
		try {
			main = unpublicPartyOrgInfoMapper.selectByPrimaryKey(id);
			//取消撤销日志
			logService.saveLog(LOG_OPERATION_TYPE.DELETE.toString(), 
					logService.getDetailInfo("log.unpublic.nocancel",
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
	 * @param id
	 * @param remarks
	 * @return
	 */
	@RequestMapping(value="/unpublic/partyCancelok",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> cancelok(Model model,String partyOrgIds,String remarks){
		try {
			String[] partyOrgIdArray = partyOrgIds.split(",");
			for(int i = 0; i < partyOrgIdArray.length; i++){
				UnpublicPartyOrgInfo main = new UnpublicPartyOrgInfo();
				main.setId(Integer.parseInt(partyOrgIdArray[i]));
				main.setStatus("4");
		//		main.setUpdateTime(new Date());
		//		main.setUpdator(baseService.getUserName());
				unpublicPartyOrgInfoMapper.updateByPrimaryKeySelective(main);
			}
//			main = unpublicPartyOrgInfoMapper.selectByPrimaryKey(id);
//			//撤销审核通过日志
//			logService.saveLog(LOG_OPERATION_TYPE.MODIFY.toString(), 
//					logService.getDetailInfo("log.unpublic.cancelok",
//							baseService.getUserName(),main.getPartyOrgName()));
			return returnMsg(1, "审核通过，撤销该组织成功！");
		} catch (Exception e) {
			logger.debug("撤销审核通过党组织信息时出现异常:{}", e.getMessage(),e);
			return returnMsg(0, "撤销失败:" + e.getMessage());
		}
		
	}
	
	
	/**
	 * 文件上传页面
	 * @param id
	 * @param method
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/unpublic/uploadFile",method=RequestMethod.GET)     
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
	@RequestMapping(value="/unpublic/uploadFile",method=RequestMethod.POST)
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
	 * @param id
	 * @param method
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/unpublic/showPartyInfo",method=RequestMethod.GET)
	public String jumpShowPartyInfo(String orgIds, Model model){
		
		model.addAttribute("orgIds", orgIds);
		return "unpublic/showPartyInfoPop";
	}
	
	/**
	 * 展示党员基本信息页面
	 * @param id
	 * @param method
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/unpublic/showPartyInfo",method=RequestMethod.POST)
	public void showPartyInfo(HttpServletResponse response, int page, int rows, QueryPmbrParams params){
		String[] orgIdsArray = params.getOrgIds().split(",");
		PageHelper.startPage(page, rows, true);
		List<UnpublicOrgPmbrInfo> list = unpublicOrgPmbrInfoMapper.queryListByOrgId(params.getName(), orgIdsArray);
		for (UnpublicOrgPmbrInfo unpublicOrgPmbrInfo : list) {
			unpublicOrgPmbrInfo.setGender(dict.getValueByCode(CommonConstants.GENDER, unpublicOrgPmbrInfo.getGender()));
			unpublicOrgPmbrInfo.setPartymbrFrontlineIs(dict.getValueByCode(CommonConstants.YES_NO, unpublicOrgPmbrInfo.getPartymbrFrontlineIs()));
			unpublicOrgPmbrInfo.setPartymbrInUnpublicIs(dict.getValueByCode(CommonConstants.YES_NO, unpublicOrgPmbrInfo.getPartymbrInUnpublicIs()));
			unpublicOrgPmbrInfo.setPartymbrInVillageIs(dict.getValueByCode(CommonConstants.YES_NO, unpublicOrgPmbrInfo.getPartymbrInVillageIs()));
			unpublicOrgPmbrInfo.setPartymbrMiddleManagerIs(dict.getValueByCode(CommonConstants.YES_NO, unpublicOrgPmbrInfo.getPartymbrMiddleManagerIs()));
			unpublicOrgPmbrInfo.setPartymbrNotinUnpublicIs(dict.getValueByCode(CommonConstants.YES_NO, unpublicOrgPmbrInfo.getPartymbrNotinUnpublicIs()));
			unpublicOrgPmbrInfo.setPartymbrOnMiddletechIs(dict.getValueByCode(CommonConstants.YES_NO, unpublicOrgPmbrInfo.getPartymbrOnMiddletechIs()));
			unpublicOrgPmbrInfo.setEducation(dict.getValueByCode(CommonConstants.FINAL_EDUCATION, unpublicOrgPmbrInfo.getEducation()));
		}
		PageUtil.returnPage(response, new PageInfo<UnpublicOrgPmbrInfo>(list));
	}
	
	/**
	 * 初始化参数
	 * @param model
	 */
	private void initPage(Model model){
			
		List<DataDictionary> yesNoList = dict.findByDmm(CommonConstants.YES_NO);
		List<DataDictionary> partyOrgClassList = dict.findByDmm(CommonConstants.PARTY_ORG_CLASS);
		List<DataDictionary> partyOrgFormList = dict.findByDmm(CommonConstants.PARTY_ORG_FORM);
		model.addAttribute("yesNoList", yesNoList);
		model.addAttribute("partyOrgClassList", partyOrgClassList);
		model.addAttribute("partyOrgFormList", partyOrgFormList);
	}
	/**
	 * 编辑页面初始化参数
	 * @param model
	 */
	private void editPage(Model model){
		
		List<DataDictionary> yesNoList = dict.findByDmm(CommonConstants.YES_NO);
		
		List<DataDictionary> partyOrgClassList = dict.findByDmm(CommonConstants.PARTY_ORG_CLASS);
		List<DataDictionary> partyOrgFormList = dict.findByDmm(CommonConstants.PARTY_ORG_FORM);
		List<DataDictionary> noEstablishPartyOrgReasonList = dict.findByDmm(CommonConstants.NO_ESTABLISH_PARTY_ORG_REASON);
		List<DataDictionary> sourceList = dict.findByDmm(CommonConstants.SOURCE);
		List<DataDictionary> partyTypeList = dict.findByDmm(CommonConstants.PARTY_SECRETARIES_AND_COMMISSION_TYPE);
		List<DataDictionary> finalEducationList = dict.findByDmm(CommonConstants.FINAL_EDUCATION);
		List<DataDictionary> genderList = dict.findByDmm(CommonConstants.GENDER);
		
		model.addAttribute("yesNoList", yesNoList);
		model.addAttribute("partyOrgClassList", partyOrgClassList);
		model.addAttribute("partyOrgFormList", partyOrgFormList);
		model.addAttribute("noEstablishPartyOrgReasonList", noEstablishPartyOrgReasonList);
		model.addAttribute("sourceList", sourceList);
		model.addAttribute("partyTypeList", partyTypeList);
		model.addAttribute("finalEducationList", finalEducationList);
		model.addAttribute("genderList", genderList);

	}
	
	/**
	 * 转换数据集合
	 * @param rows
	 */
	private void convertRows(List<UnpublicPartyOrgInfo> rows) {
		for (UnpublicPartyOrgInfo info : rows) {
			this.convertRow(info);
		}
	}

	/**
	 * 转换单条数据
	 * @param info
	 */
	private void convertRow(UnpublicPartyOrgInfo main) {

		main.setPartyOrgFormTxt(dict.getValueByCode(CommonConstants.PARTY_ORG_FORM,main.getPartyOrgForm()));
		main.setPartyOrgTypeTxt(dict.getValueByCode(CommonConstants.PARTY_ORG_CLASS,main.getPartyOrgType()));
		main.setStatusTxt(dict.getValueByCode(CommonConstants.UNPUBLIC_ORG_STATUS,main.getStatus()));
		
		main.setInstructorUnitTxt(baseService.getDeptNameById(main.getInstructorUnit()));
		main.setSecretaryCompanyTxt(baseService.getDeptNameById(main.getSecretaryCompany()));
		main.setBelongUnitTxt(baseService.getDeptNameById(main.getBelongUnit()));
		
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
