<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
	<title>党组织信息详情页</title>
	<%@ include file="/WEB-INF/views/index/detailHeader.jsp"%>
    <script type="text/javascript" src="<c:url value='/resources/plugins/bootstrap-select/bootstrap-select.js'/>"></script>    
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/plugins/bootstrap-select/bootstrap-select.css'/>">    
	<script type="text/javascript" src="<c:url value='/resources/plugins/jquery.citys.js'/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/plugins/ztree/zTreeStyle/zTreeStyle.css'/>" />
	<script type="text/javascript" src="<c:url value='/resources/plugins/ztree/jquery.ztree.core-3.5.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/plugins/ztree/jquery.ztree.excheck-3.5.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/plugins/ztree/jquery.ztree.exedit-3.5.min.js'/>"></script>  
</head>
<body >
	<form action="" id="editForm" class="form-horizontal">
    	<div class="panel panel-info">
		   <div class="panel-heading">
		      <h3 class="panel-title">基本信息</h3>
		   </div>
		   <div class="panel-body" align="center">
				<table class="table table-bordered" cellpadding="2" border="0" cellspacing="0" >
					<colgroup>
				 		<col width="90" />
				 		<col width="120"/>
				 		<col width="100"/>
				 		<col width="120"/>
				 		<col width="90"/>
				 		<col width="120"/>
				 	</colgroup>
					<tr>
						<td style="text-align: right;">建立党组织单位：</td>
	                    <td style="text-align: left;" colspan="5">
                    		<div  class="form-control" id="partyOrgDiv" style="height: 70px;float:left;">
                    			<c:forEach var="it" items="${unitList}">
									<button class="btn btn-xs" value="${it.code }" disabled="disabled" name="btn_org_name">${it.value }</button>
								</c:forEach>
							</div>
	                    </td>
	                </tr>
					<tr>  
						<td style="text-align: right;"><font color="red">*</font>是否成立党组织：</td>
						<td style="text-align: left;">
							<select class="form-control" name="isSetUpPartyOrg" id="isSetUpPartyOrg" disabled="disabled">
								<option value="" >--请选择--</option>
								<c:forEach var="it" items="${yesNoList}">
									<option value="${it.code }" <c:if test="${main.isSetUpPartyOrg == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
						</td>
						<td style="text-align: right;">未建立党组织原因：</td>
						<td style="text-align: left;">
							<select id="absencePartyOrgReasion" name="absencePartyOrgReasion" class="form-control" disabled="disabled">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${noEstablishPartyOrgReasonList}">
									<option value="${it.code }" <c:if test="${main.absencePartyOrgReasion == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
						</td>
						<td style="text-align: right;">党组织组建形式：</td>
						<td style="text-align: left;">
							<select id="partyOrgForm" name="partyOrgForm" class="form-control" disabled="disabled">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${partyOrgFormList}">
									<option value="${it.code }" <c:if test="${main.partyOrgForm == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align: right;">党组织类别：</td>
						<td style="text-align: left;">
							<select id="partyOrgType" name="partyOrgType" class="form-control" disabled="disabled">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${partyOrgClassList}">
									<option value="${it.code }" <c:if test="${main.partyOrgType == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
						</td>
						<td style="text-align: right;">是否选派党建工作<br/>指导员或联络员：</td>
						<td style="text-align: left;">
							<select id="isInstructor" name="isInstructor" class="form-control" disabled="disabled">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${yesNoList}">
									<option value="${it.code }" <c:if test="${main.isInstructor == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
						</td>
						<td style="text-align: right;"><font color="red">*</font>党组织成立时间：</td>
						<td style="text-align: left;">${main.partyOrgTimeTxt }</td>
					</tr>
					<tr>
						<td style="text-align: right;"><font color="red">*</font>党组织成立相关附件：</td>
						<td style="text-align: left;">
							<input class="form-control" type="text" id="filepartyOrgAttachment" name="filepartyOrgAttachment" value="${main.filepartyOrgAttachment }" onclick="showUpload(this,4)"/>
							<input class="form-control" type="hidden" id="partyOrgAttachment" name="partyOrgAttachment" value="${main.partyOrgAttachment }" />
						</td>
					</tr>
				</table>
		   </div>
		</div>
		<div class="panel panel-info" id="divThree">
		   <div class="panel-heading">
		      <h3 class="panel-title">党组织基本信息</h3>
		   </div>
		   <div class="panel-body" align="center">
				<table class="table table-bordered" cellpadding="2" border="0" cellspacing="0" >
					<colgroup>
				 		<col width="90" />
				 		<col width="120"/>
				 		<col width="100"/>
				 		<col width="120"/>
				 		<col width="90"/>
				 		<col width="120"/>
				 	</colgroup>
					<tr>
						<td style="text-align: right;"><font color="red">*</font>党组织名称：</td>
						<td style="text-align: left;">${main.partyOrgName} </td>
						
						<td style="text-align: right;"><font color="red">*</font>党组织联系电话(书记手机号)：</td>
						<td style="text-align: left;">${main.partyOrgTel }</td>
						
						<td style="text-align: right;"><font color="red">*</font>隶属党组织名称：</td>
						<td style="text-align: left;">${main.subjectionPartyName }</td>
					</tr>
				</table>
		   </div>
		</div>
    	<div class="panel panel-info" id="divOne">
		   <div class="panel-heading">
		      <h3 class="panel-title">指导员或联络员信息</h3>
		   </div>
		   <div class="panel-body" align="center">
				<table class="table table-bordered" cellpadding="2" border="0" cellspacing="0" >
					<colgroup>
				 	</colgroup>
				 	<tr>
						<td style="text-align: left;">
							<c:if test="${instructList.size() == 0}">
								<div class="form-inline" style="margin-top:5px;">
									<label>人员姓名：</label>
									<input class="form-control" name="instructorName" value="" maxlength="20" disabled="disabled"/>
									<label>职务：</label>
									<input class="form-control" name="instructorJob" value="" maxlength="20" disabled="disabled"/>
									<label>单位：</label>
				                       <select id="zhidaodanwei0" name="instructorUnitTxt" class="form-control" disabled="disabled">
										    <c:forEach var="it" items="${unitList}">
										        <option value="${it.code }" >${it.value }</option>
										    </c:forEach>
									    </select>
								</div>
							</c:if>
							<c:if test="${!empty main.id and instructList.size() > 0}">
								<c:forEach items="${instructList }" var="e" varStatus="status">
									<div class="form-inline" style="margin-top:5px;">
										<label>人员姓名：</label>
										<input class="form-control" name="instructorName" value="${e.instructorName }" maxlength="20" disabled="disabled"/>
										<label>单位及职务：</label>
										<input class="form-control" name="instructorJob" value="${e.instructorJob }" maxlength="20" disabled="disabled"/>
										<label>指导单位：</label>
							               	<select id="zhidaodanwei${status.index }" name="instructorUnitTxt" class="form-control" disabled="disabled">
											    <c:forEach var="it" items="${unitList}">
											        <c:set var="tmpInstructorUnit" value=""/>
													   <c:forEach items="${e.orgIdList }" var="t">
														  <c:if test="${it.code == t}">
														      <c:set var="tmpInstructorUnit" value='selected="selected"' />
														  </c:if>
													  </c:forEach>
											        <option value="${it.code }" ${tmpInstructorUnit}>${it.value }</option>
											    </c:forEach>
										    </select>
									</div>
								</c:forEach>
							</c:if>
							<button type="button" id="add_operate_instructor" class="btn btn-primary btn-sm glyphicon glyphicon-plus"></button>
						</td>
					</tr>
				</table>
		   </div>
		</div>
		<div class="panel panel-info" id="divEight">
		   <div class="panel-heading">
		      <h3 class="panel-title">下级党组织信息</h3>
		   </div>
		   <div class="panel-body" align="center">
				<table class="table table-bordered" cellpadding="2" border="0" cellspacing="0" >
					<colgroup>
				 	</colgroup>
				 	<tr>
						<c:if test="${!empty main.id and lowerPartyList.size() > 0}">
							<td style="text-align: left;">
								<c:forEach items="${lowerPartyList }" var="e" >
									<div class="form-inline" style="margin-top:5px;">
										<label>名称：</label>
										<input class="form-control" name="lowerPartyOrgName" value="${e.lowerPartyOrgName }" maxlength="20" disabled="disabled"/>
										<label>类型：</label>
										<select id="lowerPartyOrgType" name="lowerPartyOrgType" class="form-control" disabled="disabled">
											<option value="">--请选择--</option>
											<c:forEach var="it" items="${partyOrgClassList}">
												<option value="${it.code }" <c:if test="${e.lowerPartyOrgType == it.code }"> selected="selected"</c:if>>${it.value }</option>
											</c:forEach>
										</select>
										<label>数量：</label>
										<input class="form-control" name="lowerPartyOrgSum" value="${e.lowerPartyOrgSum}" maxlength="20" disabled="disabled"/>
									</div>
								</c:forEach>
							</td>
						</c:if>
					</tr>
				</table>
		   </div>
		</div>
		<div class="panel panel-info" id="divTwo">
		   <div class="panel-heading">
		      <h3 class="panel-title">党组织换届信息(允许录入多次)</h3>
		   </div>
		   <div class="panel-body" align="center">
				<table class="table table-bordered" cellpadding="2" border="0" cellspacing="0" >
					<colgroup>
				 		<col width="90" />
				 		<col width="540"/>
				 	</colgroup>
					<tr>
						<c:if test="${!empty main.id and changeDateList.size() > 0}">
							<td style="text-align: right;">换届时间：</td>
							<td style="text-align: left;">
								<c:forEach items="${changeDateList }" var="e" varStatus="status">
									<div class="form-inline" style="margin-top:5px;">
										<input class="form-control" type="text" disabled="disabled" id="partymbrInUnpublicNum${status.index }" name="partymbrInUnpublicNum" value="${e.changeTimeTxt }"/>
										<label>换届相关附件</label><input class="form-control" type="text" id="filepartymbrUnderThirtyfiveNum${status.index }" name="filepartymbrUnderThirtyfiveNum${status.index }" value="${e.changeAttachmentName }" onclick="showUpload(this,2)"/>
										<input class="form-control" type="hidden" id="partymbrUnderThirtyfiveNum${status.index }" name="partymbrUnderThirtyfiveNum${status.index }" value="${e.changeAttachmentId }"/>
									</div>
								</c:forEach>
							</td>
						</c:if>
					</tr>
				</table>
		   </div>
		</div>
		
		<div class="panel panel-info" id="divFour">
			<div class="panel-heading">
		      <h3 class="panel-title">书记信息</h3>
		   </div>
		   <div class="panel-body" align="center">
				<table class="table table-bordered" cellpadding="2" border="0" cellspacing="0" >
					<colgroup>
				 		<col width="90" />
				 		<col width="120"/>
				 		<col width="100"/>
				 		<col width="120"/>
				 		<col width="90"/>
				 		<col width="120"/>
				 	</colgroup>
					<tr>
						<td style="text-align: right;"><font color="red">*</font>书记姓名：</td>
						<td style="text-align: left;">${main.secretaryName }</td>
						
						<td style="text-align: right;"><font color="red">*</font>书记出生日期：</td>
						<td style="text-align: left;">${main.secretaryBirthdayTxt }</td>
						
						<td style="text-align: right;"><font color="red">*</font>书记学历：</td>
						<td align= "left" >
							<select id="secretaryEducation" name="secretaryEducation" class="form-control" disabled="disabled">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${finalEducationList}">
									<option value="${it.code }" <c:if test="${main.secretaryEducation == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
	                    </td>
					</tr>
					<tr>
						<td style="text-align: right;"><font color="red">*</font>书记来源：</td>
						<td align= "left" >
							<select id="secretarySource" name="secretarySource" class="form-control" disabled="disabled">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${sourceList}">
									<option value="${it.code }" <c:if test="${main.secretarySource == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
	                    </td>
						<td style="text-align: right;">书记所在单位：</td>
	                    <td align= "left" >
		                    <input class="form-control" type="text" id="secretaryCompanyOne" name="secretaryCompany" value="${main.secretaryCompany }" />
	                    
	                    	<select id="secretaryCompanyTwo" name="secretaryCompany" class="form-control">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${unitList}">
									<option value="${it.code }" <c:if test="${main.secretaryCompany == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
	                    </td>
					</tr>
				</table>
		   </div>
		</div>
		<div class="panel panel-info" id="divFive">
			<div class="panel-heading">
		      <h3 class="panel-title">副书记及委员</h3>
		   </div>
		   <div class="panel-body" align="center">
				<table class="table table-bordered" cellpadding="2" border="0" cellspacing="0" >
					<colgroup>
				 	</colgroup>
				 	<tr>
						<c:if test="${!empty main.id and deputsecList.size() > 0}">
							<td style="text-align: left;">
								<c:forEach items="${deputsecList }" var="e" varStatus="status">
									<div class="form-inline" style="margin-top:5px;">
										<label style="width:90px;text-align: right;"><font style="font-weight:normal;">类型：</font></label>
										<select name="deputySecretaryType${status.index }" class="form-control" disabled="disabled" style="width:100px;">
												<option value="">--请选择--</option>
												<c:forEach var="it" items="${partyTypeList}">
												<option value="${it.code }" <c:if test="${e.deputySecretaryType == it.code }"> selected="selected"</c:if>>${it.value }</option>
											</c:forEach>
									    </select>
										<label style="width:90px;text-align: right;"><font style="font-weight:normal;">姓名：</font></label>
										<input class="form-control" style="width:100px;" disabled="disabled" name="deputySecretaryName${status.index }" value="${e.deputySecretaryName }"/>							  
										<label style="width:90px;text-align: right;"><font style="font-weight:normal;">出生日期：</font></label>
										<input class="form-control" style="width:100px;" disabled="disabled" name="deputySecretaryBirthdayTxt${status.index }" value="${e.deputySecretaryBirthdayTxt }" />
										<label style="width:90px;text-align: right;"><font style="font-weight:normal;">性别：</font></label>
										<select  name="deputySecretarySex${status.index }" class="form-control" disabled="disabled" style="width:100px;">
											<option value="">--请选择--</option>
											<c:forEach var="it" items="${genderList}">
												<option value="${it.code }" <c:if test="${e.deputySecretarySex == it.code }"> selected="selected"</c:if>>${it.value }</option>
											</c:forEach>
										</select>
										<label style="width:90px;text-align: right;"><font style="font-weight:normal;">学历：</font></label>
										<select  name="deputySecretaryEducation${status.index }" class="form-control" disabled="disabled" style="width:100px;">
											<option value="">--请选择--</option>
											<c:forEach var="it" items="${finalEducationList}">
												<option value="${it.code }" <c:if test="${e.deputySecretaryEducation == it.code }"> selected="selected"</c:if>>${it.value }</option>
											</c:forEach>
										</select>
										<label style="width:90px;text-align: right;"><font style="font-weight:normal;">是否是专职：</font></label>
										<select  name="deputySecretaryIsFullTime${status.index }" class="form-control" disabled="disabled" style="width:100px;">
											<option value="">--请选择--</option>
											<c:forEach var="it" items="${yesNoList}">
												<option value="${it.code }" <c:if test="${e.deputySecretaryIsFullTime == it.code }"> selected="selected"</c:if>>${it.value }</option>
											</c:forEach>
										</select>	
										<label style="width:90px;text-align: right;"><font style="font-weight:normal;">是否是理事会<br/>成员：</font></label>
										<select  name="isBoardOfficer${status.index }" class="form-control" disabled="disabled" style="width:100px;">
											<option value="">--请选择--</option>
											<c:forEach var="it" items="${yesNoList}">
												<option value="${it.code }" <c:if test="${e.deputySecretaryIsFullTime == it.code }"> selected="selected"</c:if>>${it.value }</option>
											</c:forEach>
										</select>
										<c:if test="${nature eq '0201' or nature eq '0204'}">
											<label style="width:90px;text-align: right;"><font style="font-weight:normal;">其它委员进入<br/>管理情况：</font></label>
											<select  name="otherInfoManager" class="form-control" disabled="disabled" style="width:100px;">
												<option value="">--请选择--</option>
												<c:forEach var="it" items="${otherInfoManagerList}">
														<option value="${it.code }" <c:if test="${e.otherInfoManager == it.code }"> selected="selected"</c:if>>${it.value }</option>
												</c:forEach>
											</select>
										</c:if>
									</div>
								</c:forEach>
							</td>
						</c:if>
					</tr>
				</table>
		   </div>
		</div>
		<div class="panel panel-info">
		   <div class="panel-heading">
		      <h3 class="panel-title">党员统计信息</h3>
		   </div>
		   <div class="panel-body" align="center">
				<table class="table table-bordered" cellpadding="2" border="0" cellspacing="0" >
					<colgroup>
				 		<col width="90" />
				 		<col width="120"/>
				 		<col width="100"/>
				 		<col width="120"/>
				 		<col width="90"/>
				 		<col width="120"/>
				 	</colgroup>
					<tr>
						<td style="text-align: right;">组织关系在非公　<br/>企业的党员总数：</td>
						<td style="text-align: left;"><input class="form-control" disabled="disabled"  value="${copc.partymbrIncoverNum }"/></td>
						<td style="text-align: right;">35岁以下：</td>
						<td style="text-align: left;"><input class="form-control" disabled="disabled" value="${copc.partymbrUnderThirtyfiveNum }"/></td>
						<td style="text-align: right;">大学及以上学历：</td>
						<td style="text-align: left;"><input class="form-control" disabled="disabled" value="${copc.partymbrOnCollegeNum }"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">高中及以下学历：</td>
						<td style="text-align: left;"><input class="form-control" disabled="disabled" value="${copc.partymbrUnderHighschoolNum }"/></td>
						<td style="text-align: right;">组织关系不在非公<br/>企业的党员总数：</td>
						<td style="text-align: left;"><input class="form-control" disabled="disabled" value="${copc.partymbrNotIncoverNum }"/></td>
						<td align="right" colspan="2" style="margin-right: 15px;">
							<div align="right" border="false" >
					    		<div class="btn-group">
								  <button type="button" class="btn btn-primary" onclick="showPartyInfo('${main.id}')">查看党员详情页面</button>
								</div>
					    	</div>
						</td>
					</tr>
				</table>
		   </div>
		</div>
		<div class="panel panel-info" id="divSix">
			<div class="panel-heading">
		      <h3 class="panel-title">其他党务工作者</h3>
		   </div>
		   <div class="panel-body" align="center">
				<table class="table table-bordered" cellpadding="2" border="0" cellspacing="0" >
					<colgroup>
				 		<col width="90" />
				 		<col width="120"/>
				 		<col width="100"/>
				 		<col width="120"/>
				 		<col width="90"/>
				 		<col width="120"/>
				 	</colgroup>
					<tr>
	                    <td style="text-align: right;"><font color="red">*</font>兼职人数：</td>
						<td style="text-align: left;">${main.partTimeWorkers }</td>
						
						<td style="text-align: right;"><font color="red">*</font>专职人数：</td>
						<td style="text-align: left;">${main.fullTimeWorkers }</td>
						
					</tr>
				</table>
		   </div>
		</div>
		<div class="panel panel-info" id="divSev">
			<div class="panel-heading">
		      <h3 class="panel-title">活动场所情况</h3>
		   </div>
		   <div class="panel-body" align="center">
				<table class="table table-bordered" cellpadding="2" border="0" cellspacing="0" >
					<colgroup>
				 		<col width="90" />
				 		<col width="120"/>
				 		<col width="100"/>
				 		<col width="120"/>
				 		<col width="90"/>
				 		<col width="120"/>
				 	</colgroup>
					<tr>
						<td style="text-align: right;"><font color="red">*</font>单独建设活动场所面积(m²)：</td>
						<td style="text-align: left;">${main.stageArea }</td>
						<td style="text-align: right;"><font color="red">*</font>与其他单位党组织共用活动场所：</td>
						<td style="text-align: left;">${main.otherShareStage }</td>
	                    <td style="text-align: right;"><font color="red">*</font>与社区共用活动场所活动场所：</td>
						<td style="text-align: left;">${main.communityShareStage }</td>
					</tr>
					<tr>
						<td style="text-align: right;"><font color="red">*</font>其他：</td>
						<td style="text-align: left;">${main.otherInfo }</td>
						<c:if test="${nature eq '0201' or nature eq '0204'}">
							<td style="text-align: right;"><font color="red">*</font>是否将党组织建设有关内容纳入学校章程：</td>
							<td align= "left" >
								<select id="isPartyIntoSchool" id="isPartyIntoSchool" name="isPartyIntoSchool" class="form-control" disabled="disabled">
									<option value="">--请选择--</option>
									<c:forEach var="it" items="${yesNoList}">
										<option value="${it.code }" <c:if test="${main.isPartyIntoSchool == it.code }"> selected="selected"</c:if>>${it.value }</option>
									</c:forEach>
								</select>
		                    </td>
							<td style="text-align: right;"><font color="red">*</font>隶属上级党组织类别(学校特有)：</td>
							<td align= "left" >
								<select id="parentPartyOrgType" id="parentPartyOrgType" name="parentPartyOrgType" class="form-control" disabled="disabled">
									<option value="">--请选择--</option>
									<c:forEach var="it" items="${heightPartyOrgList}">
										<option value="${it.code }" <c:if test="${main.parentPartyOrgType == it.code }"> selected="selected"</c:if>>${it.value }</option>
									</c:forEach>
								</select>
		                    </td>
		                </c:if>
					</tr>
				</table>
		   </div>
		</div>
		<input type="hidden" id="instructSize" name="instructSize" value="${instructSize }" /> 
		<input type="hidden" id="mainId" name="mainId" value="${main.id }" />
		<input type="hidden" id="modular" name="modular" value="1" />
		<input type="hidden" id="type" name="type" value="1" />
	</form>
	<div align="center" border="false" style="position:fixed;right:10px;bottom:10px;" id="returnDiv">
   		<div class="btn-group">
		  <button type="button" class="btn btn-primary" onclick="javascript:parent.utils.e.closeWin('lookwin');">关闭窗口</button>
		</div>
   	</div>
</body>
<script type="text/javascript">

	$(function(){
		
		//页面的显示或隐藏顺序
		var setUpSign = '${main.isSetUpPartyOrg}';
		isSetUpPartyOrg(setUpSign);
		if(setUpSign == 1 && setUpSign != ""){
			var hiddenFlag = '${main.partyOrgForm}';
			showChangInfo(hiddenFlag);
			if(hiddenFlag != 3){
				showLowerPartyInfo();
				showInstructorInfo();
			}
		}
		
		//书记所在单位
		var secretarySource = '${main.secretarySource}';
		secretarySourceSelect(secretarySource);
	});

	function showUpload(obj, action){
		var id = $("#"+obj.id.replace('file','')).val();
		var filename = $("#"+obj.id).val();
		var mainId = $("#mainId").val();
		var modular = $("#modular").val();
		var type = $("#type").val();
		var action = action;
		var url = ctx + '/socialorg/uploadFile?id='+id+'&mainTableId='+mainId+'&filename='+filename+
							'&sign='+obj.id+'&modular='+modular+'&type='+type+'&action='+action+'&method=look';
		//openWin("撤销", url,"50%","90%",function(){reloadData()});
		utils.e.openWin('cancelwin','文件上传',url,"60%","40%",function(){
			//reloadData()
		});
	}
	
	function showPartyInfo(orgIds){
		var url = ctx + '/socialorg/showPartyInfo?orgIds='+orgIds;
		utils.e.openWin('showPartyInfoWin','党员基本信息',url,"80%","40%",function(){
			//reloadData()
		});
	}
	
	function isSetUpPartyOrg(setUpSign){
		if(setUpSign == 0 && setUpSign != ""){
			hiddenAllsign(setUpSign);
		}else{
			showAllsign(setUpSign);
		}
	}
	
	/**
	 * 不同的建立方式<br>
	 * 党组织信息展示的控制
	 */
	function showChangInfo(hiddenFlag) {
		if (hiddenFlag == 0 || hiddenFlag == 1 || hiddenFlag == 2) {
			showAllsign(hiddenFlag);
			$("#divTwo").show();
			// 网格建 不显示换届信息
			if (hiddenFlag == 2) {
				$("#divTwo").hide();
			}
		} else {
			hiddenAllsign(hiddenFlag);
		}
	}
	
	function showInstructorInfo(){
		var setUpSign = '${main.isInstructor}';
		if(setUpSign == 0 && setUpSign != ""){
			$("#divOne").hide();
		}else{
			$("#divOne").show();
		}
	}
	
	function showLowerPartyInfo() {
		var hiddenFlag = '${partyOrgType}';
		if (hiddenFlag == 0 || hiddenFlag == 1) {
			$("#divEight").show();
		} else {
			$("#divEight").hide();
		}
	}
	
	function hiddenAllsign(flag){
		if(flag != 0 && flag != ""){
			document.getElementById("divOne").style.display='none';
		}
		document.getElementById("divTwo").style.display='none';
		document.getElementById("divThree").style.display='none';
		document.getElementById("divFour").style.display='none';
		document.getElementById("divFive").style.display='none';
		document.getElementById("divSix").style.display='none';
		document.getElementById("divSev").style.display='none';
		document.getElementById("divEight").style.display='none';
	}
	
	function showAllsign(flag){
		showInstructorInfo();
		document.getElementById("divTwo").style.display='';
		document.getElementById("divThree").style.display='';
		document.getElementById("divFour").style.display='';
		document.getElementById("divFive").style.display='';
		document.getElementById("divSix").style.display='';
		document.getElementById("divSev").style.display='';
		document.getElementById("divEight").style.display='';
	}
	
	function secretarySourceSelect(secretarySource){
		if(secretarySource == 2){
			$("#secretaryCompanyOne").show();
			$("#secretaryCompanyTwo").hide();
			document.getElementById("secretaryCompanyOne").disabled=true;
			document.getElementById("secretaryCompanyTwo").disabled=true;
		}else{
			$("#secretaryCompanyOne").hide();
			$("#secretaryCompanyTwo").show();
			document.getElementById("secretaryCompanyOne").disabled=true;
			document.getElementById("secretaryCompanyTwo").disabled=true;
		}
	}
</script>
</html>