package com.tf.base.basemanage.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tf.base.basemanage.domain.PartyOrgInfo;
import com.tf.base.basemanage.persistence.PartyOrgInfoMapper;


@Service
public class PartyOrgInfoService {

	@Autowired
	private PartyOrgInfoMapper partyOrgInfoMapper;
	
	/**
	 * 根据部门名称查询部门的所有上级机构（包含本部门）
	 * @param name 部门名称
	 * @return
	 */
	public List<PartyOrgInfo> getDepartmentInfosWithParentsByName(String name){
		
		if(!StringUtils.isEmpty(name)){
			List<PartyOrgInfo> partyOrgInfoList = new ArrayList<PartyOrgInfo>();
			List<PartyOrgInfo> queryPartyOrgInfo = partyOrgInfoMapper.getPartyOrgInfosByNameLike(name);
			partyOrgInfoList.addAll(queryPartyOrgInfo);
			if (queryPartyOrgInfo != null && !queryPartyOrgInfo.isEmpty()) {
				for (PartyOrgInfo partyOrgInfo : queryPartyOrgInfo) {
					queryParemntDepartmentWithParentsInfo(partyOrgInfo, partyOrgInfoList);
				}
			}
			return partyOrgInfoList;
		}
		return null;
	}
	/**
	 * 根据部门名称查询部门的所有下级机构（包含本部门）
	 * @param name 部门名称
	 * @return
	 */
	public List<PartyOrgInfo> getDepartmentInfosWithChildByName(String name){
		
		if(!StringUtils.isEmpty(name)){
			List<PartyOrgInfo> departmentInfoList = new ArrayList<PartyOrgInfo>();
			List<PartyOrgInfo> queryDepartmentInfo = partyOrgInfoMapper.getPartyOrgInfosByNameLike(name);
			departmentInfoList.addAll(queryDepartmentInfo);
			if (queryDepartmentInfo != null && !queryDepartmentInfo.isEmpty()) {
				for (PartyOrgInfo departmentInfo : queryDepartmentInfo) {
					queryParemntDepartmentWithChildInfo(departmentInfo, departmentInfoList);
				}
			}
			return departmentInfoList;
		}
		return null;
	}
	
	
	private void queryParemntDepartmentWithParentsInfo(PartyOrgInfo departmentInfo,List<PartyOrgInfo> departmentInfoList){
		boolean flag = true;
		if (departmentInfo != null && !StringUtils.isEmpty(departmentInfo.getParentPartyOrg())) {
			PartyOrgInfo parentPartyOrgInfo = partyOrgInfoMapper.getDepartmentInfoById(departmentInfo.getParentPartyOrg()+"");
			if (parentPartyOrgInfo != null && parentPartyOrgInfo.getId() != null) {
				for (PartyOrgInfo departmentInfo2 : departmentInfoList) {
					if(departmentInfo2.getId().equals(parentPartyOrgInfo.getId())){
						flag = false;
						break;
					}
				}
				if(flag){
					departmentInfoList.add(parentPartyOrgInfo);
				}
				queryParemntDepartmentWithParentsInfo(parentPartyOrgInfo, departmentInfoList);	
			}
		}
	}


	private void queryParemntDepartmentWithChildInfo(PartyOrgInfo departmentInfo,List<PartyOrgInfo> departmentInfoList){
		if (departmentInfo != null && !StringUtils.isEmpty(departmentInfo.getParentPartyOrg())) {
			List<PartyOrgInfo> parentList = partyOrgInfoMapper.getDepartmentInfoByParentId(departmentInfo.getId()+"");
			if (parentList != null && !parentList.isEmpty()) {
				
				for (PartyOrgInfo departmentInfo2 : parentList) {
					if (!departmentInfoList.contains(departmentInfo2)) {
						departmentInfoList.add(departmentInfo2);
					}
					queryParemntDepartmentWithParentsInfo(departmentInfo2, departmentInfoList);
				}
			}
		}
	}
}