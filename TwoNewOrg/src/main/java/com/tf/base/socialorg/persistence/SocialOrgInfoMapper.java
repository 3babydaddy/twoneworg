package com.tf.base.socialorg.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tf.base.socialorg.domain.SocialOrgInfo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface SocialOrgInfoMapper extends MySqlMapper<SocialOrgInfo>, Mapper<SocialOrgInfo> {
	
	List<SocialOrgInfo> queryList(@Param("params") SocialOrgInfo params,@Param("orderby")String orderby);
	int queryListCount(@Param("params") SocialOrgInfo params);
}