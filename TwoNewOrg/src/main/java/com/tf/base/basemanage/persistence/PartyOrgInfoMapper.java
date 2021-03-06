package com.tf.base.basemanage.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.tf.base.basemanage.domain.PartyOrgInfo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface PartyOrgInfoMapper extends MySqlMapper<PartyOrgInfo>, Mapper<PartyOrgInfo> {
	
	List<PartyOrgInfo> getAllPartyOrgInfo(@Param("deptId") String deptId);
	
	List<PartyOrgInfo> getPartyOrgInfosByNameLike(@Param(value="name") String name, @Param(value="deptId") String deptId);
	
	PartyOrgInfo getDepartmentInfosByName(@Param(value="name") String name);
	
	PartyOrgInfo getDepartmentInfosByNameAndNotEQID(@Param(value="name") String name,@Param(value="id") String id);
	
	PartyOrgInfo getDepartmentInfoById(@Param(value="id") String id);
	
	List<PartyOrgInfo> getDepartmentInfoByParentId(@Param(value="id") String id);
}