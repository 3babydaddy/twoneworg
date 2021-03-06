package com.tf.base.basemanage.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tf.base.basemanage.domain.PartyOrgInfo;
import com.tf.base.basemanage.persistence.PartyOrgInfoMapper;
import com.tf.base.basemanage.service.PartyOrgInfoService;
import com.tf.base.common.constants.CommonConstants;
import com.tf.base.common.domain.DataDictionary;
import com.tf.base.common.domain.DictionaryRepository;
import com.tf.base.common.service.BaseService;


@Controller
public class PartyOrgInfoController {

	@Autowired
	private DictionaryRepository dict;
	
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private PartyOrgInfoService partyOrgInfoService;
	
	@Autowired
	private PartyOrgInfoMapper partyOrgInfoMapper;
	
	@RequestMapping(value = "/basemanage/partyorginfoquery", method = RequestMethod.GET)
	public String partyOrgInfoQueryInit() {
		
		return "basemanage/partyOrgInfoQuery";
	}
	
	@RequestMapping(value = "/basemanage/partyorginfoquery", method = RequestMethod.POST)
	@ResponseBody
	public List<PartyOrgInfo> query(PartyOrgInfo partyOrgInfo) {
		List<PartyOrgInfo> result = null;
		String deptId = "";
		if(!baseService.isQuWeiDept()){
			deptId = baseService.getCurrentUserDeptId();
		}
		if (!StringUtils.isEmpty(partyOrgInfo.getName())) {
			result = partyOrgInfoService.getDepartmentInfosWithParentsByName(partyOrgInfo.getName(), deptId);
		}else{
			result = partyOrgInfoMapper.getAllPartyOrgInfo(deptId);			
		}
		if(!StringUtils.isEmpty(partyOrgInfo.getStatus())){
			
			List<PartyOrgInfo> availResult = new ArrayList<>();
			for (PartyOrgInfo dept : result) {
				if(dept.getStatus().equals(partyOrgInfo.getStatus())){
					availResult.add(dept);
				}
			}
			
			return availResult;
		}
		
		return result;
	}
	
	@RequestMapping(value = "/basemanage/partyorginfocreate", method = RequestMethod.GET)
	public String partyOrgInfoCreateInit(Model model) {
		initDict(model);
		return "basemanage/partyOrgInfoCreate";
	}
	
	@RequestMapping(value = "/basemanage/partyorginfocreate", method = RequestMethod.POST)
	@ResponseBody
	public int create(PartyOrgInfo partyOrgInfo, String partySetUpTimeStr, String partyStartTimeStr, HttpSession session) {
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		int flag = 3;
		try{
			PartyOrgInfo params = new PartyOrgInfo();
			params.setName(partyOrgInfo.getName());
			int count = partyOrgInfoMapper.selectCount(params);
			if(count > 0){
				flag = 2;
			}else{
				partyOrgInfo.setPartySetUpTime(partySetUpTimeStr.isEmpty()?null:sdf.parse(partySetUpTimeStr));
				partyOrgInfo.setPartyStartTime(partyStartTimeStr.isEmpty()?null:sdf.parse(partyStartTimeStr));
				partyOrgInfo.setCreater(baseService.getUserName());
				partyOrgInfo.setCreateOrg(baseService.getCurrentUserDeptId());
				partyOrgInfo.setCreateTime(new Date());
				partyOrgInfoMapper.insertSelective(partyOrgInfo);
			}
		}catch(Exception e){
			e.printStackTrace();
			flag = 0;
		}
		return flag;
	}
	
	@RequestMapping(value = "/basemanage/partyorginfomodify", method = RequestMethod.GET)
	public String partyOrgInfoModeifyInit(Model model, String depId)throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		PartyOrgInfo info = null;
		if(!StringUtils.isEmpty(depId)){
			info = partyOrgInfoMapper.selectByPrimaryKey(depId);
		}
		initDict(model);
		model.addAttribute("partyOrgInfo", info);
		model.addAttribute("partySetUpTimeStr", info.getPartySetUpTime() != null ? sdf.format(info.getPartySetUpTime()): "");
		model.addAttribute("partyStartTimeStr", info.getPartyStartTime() != null ? sdf.format(info.getPartyStartTime()) : "");
		return "basemanage/partyOrgInfoModify";
	}
	
	@RequestMapping(value = "/basemanage/partyorginfomodify", method = RequestMethod.POST)
	@ResponseBody
	public int modify(PartyOrgInfo partyOrgInfo, String partySetUpTimeStr, String partyStartTimeStr, HttpSession session) {
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		int flag = 4;
		try{
			partyOrgInfo.setPartySetUpTime(partySetUpTimeStr.isEmpty()?null:sdf.parse(partySetUpTimeStr));
			partyOrgInfo.setPartyStartTime(partyStartTimeStr.isEmpty()?null:sdf.parse(partyStartTimeStr));
			//User user = (User) session.getAttribute(PermissionConstants.CURRENT_USER);
			partyOrgInfo.setCreater(baseService.getUserName());
			partyOrgInfo.setCreateOrg(baseService.getCurrentUserDeptId());
			partyOrgInfo.setCreateTime(new Date());
			partyOrgInfoMapper.updateByPrimaryKeySelective(partyOrgInfo);
		}catch(Exception e){
			e.printStackTrace();
			flag = 0;
		}
		return flag;
	}
	
	public void initDict(Model model){
		List<DataDictionary> partyOrgStatusList = dict.findByDmm(CommonConstants.PARTY_ORG_STATUS);
		List<DataDictionary> partyOrgClassList = dict.findByDmm(CommonConstants.PARTY_ORG_CLASS);
		model.addAttribute("partyOrgStatusList", partyOrgStatusList);
		model.addAttribute("partyOrgClassList", partyOrgClassList);
	}
}
