<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
	<title>年度党建情况详情页</title>
	<%@ include file="/WEB-INF/views/index/detailHeader.jsp"%>
    <script type="text/javascript" src="<c:url value='/resources/plugins/bootstrap-select/bootstrap-select.js'/>"></script>    
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/plugins/bootstrap-select/bootstrap-select.css'/>">    
	<script type="text/javascript" src="<c:url value='/resources/plugins/jquery.citys.js'/>"></script>
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
						<td style="text-align: right;">党组织名称：</td>
		                <td style="text-align: left;"><input class="form-control" disabled="disabled" name="partyOrgName" value="${main.partyOrgName }" /></td>
						<td style="text-align: right;"><font color="red">*</font>党务工作者是否参加1<br/>次以上培训：</td>
						<td style="text-align: left;">
							<select class="form-control" name="isPartyMemberTrain" id="isPartyMemberTrain" disabled="disabled">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${yesNoList}">
									<option value="${it.code }" <c:if test="${main.isPartyMemberTrain == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
						</td>
						<td style="text-align: right;"><font color="red">*</font>近三年新发展党员数：</td>  
						<td style="text-align: left;">${main.threeYearsMember }</td>
					</tr>
					<tr>
						<td style="text-align: right;"><font color="red">*</font>近三年处置不合格党员数：</td>
						<td style="text-align: left;">${main.threeYearsUnqualifieds }</td>
						<td style="text-align: right;"><font color="red">*</font>是否对党员轮训一遍：</td>
						<td style="text-align: left;">
							<select class="form-control" name="isTrainingInRotation" id="isTrainingInRotation" disabled="disabled">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${yesNoList}">
									<option value="${it.code }" <c:if test="${main.isTrainingInRotation == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
						</td>
						<td style="text-align: right;">党建工作经费总额：</td>
						<td style="text-align: left;">${main.totalPayDues }</td>
				</table>
		   </div>
		</div>
		<div class="panel panel-info">
			<div class="panel-heading">
		      <h3 class="panel-title">经费来源</h3>
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
						<td style="text-align: right;"><font color="red">*</font>市区财政支出数额(元)：</td>
						<td style="text-align: left;">${main.cityFunds }</td>
						
						<td style="text-align: right;"><font color="red">*</font>区管党费支持数额(元)：</td>
						<td style="text-align: left;">${main.districtFunds }</td>
						
						<td style="text-align: right;"><font color="red">*</font>社会组织管理经费支持数额(元)：</td>
						<td style="text-align: left;">${main.socialOrgFunds }</td>
					</tr>
					<tr>
						<td style="text-align: right;"><font color="red">*</font>其他(说明)：</td>
						<td style="text-align: left;">${main.otherContent }</td>
						
						<td style="text-align: right;"><font color="red">*</font>每季度召开党员大会(次)：</td>
						<td style="text-align: left;">${main.quarterPartyMeetingTimes }</td>
						
						<td style="text-align: right;"><font color="red">*</font>每月召开支部委员会(次)：</td>
						<td style="text-align: left;">${main.monthPartyMeetingTimes }</td>
					</tr>
					<tr>
						<td style="text-align: right;"><font color="red">*</font>是否每月召开1~2次党小组会：</td>
						<td style="text-align: left;">
							<select class="form-control" name="partyMeetingMonth" id="partyMeetingMonth" disabled="disabled">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${yesNoList}">
									<option value="${it.code }" <c:if test="${main.partyMeetingMonth == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
						</td>
						
						<td style="text-align: right;"><font color="red">*</font>组织党员听党课(次)：</td>
						<td style="text-align: left;">${main.listenPartyClass }</td>
						
						<td style="text-align: right;"><font color="red">*</font>召开组织生活会的次数：</td>
						<td style="text-align: left;">${main.lifeMeetingTimes }</td>
					</tr>
					<tr>
						<td style="text-align: right;"><font color="red">*</font>开展民主评议党员的党支部数：</td>
						<td style="text-align: left;">${main.developPartyBranchNo }</td>
						
						<td style="text-align: right;"><font color="red">*</font>上年度党员缴纳党费总额：</td>
						<td style="text-align: left;">${main.yearTotalPayDues }</td>
						
						<td style="text-align: right;"><font color="red">*</font>年检结果：</td>
						<td style="text-align: left;">
							<select class="form-control" name="annualSurvey" id="annualSurvey" disabled="disabled">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${annualSurveyList}">
									<option value="${it.code }" <c:if test="${main.annualSurvey == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align: right;"><font color="red">*</font>党建工作经费是否列入学校年度经费预算：</td>
						<td style="text-align: left;">
							<select class="form-control" name="isIntoManage" id="isIntoManage" disabled="disabled">
								<option value="">--请选择--</option>
								<c:forEach var="it" items="${yesNoList}">
									<option value="${it.code }" <c:if test="${main.partyMeetingMonth == it.code }"> selected="selected"</c:if>>${it.value }</option>
								</c:forEach>
							</select>
						</td>
					</tr>
				</table>
		   </div>
		</div>
		<input type="hidden" id="reportHigher" name="reportHigher"/>
		<input type="hidden" id="id" name="id" value="${main.id }"/>
		<input type="hidden" id="partyOrgId" name="partyOrgId" value="${main.partyOrgId }"/>
		</form>
		<div align="center" border="false" style="position:fixed;right:10px;bottom:10px;" id="returnDiv">
    		<div class="btn-group">
			  <button type="button" class="btn btn-primary" onclick="javascript:parent.utils.e.closeWin('lookwin');">关闭窗口</button>
			</div>
    	</div>
	
</body>
</html>