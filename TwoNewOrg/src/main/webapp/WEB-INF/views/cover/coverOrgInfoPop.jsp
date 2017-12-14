<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--<%@ include file="/WEB-INF/views/index/include.jsp"%>--%>
<%@ include file="/WEB-INF/views/index/detailHeader.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
<head>
	<title></title>
</head>	
<body >
<div class="easyui-layout" style="width:100%;height:100%;" >
	<div data-options="region:'north'" class="easyui-panel" title="查询" style="width:100%;height: 90px;" align="center">
	  <form id="queryForm" action="" name="queryForm" class="easyui-form" method="post" >		
			<table cellpadding="5" border="0" cellspacing="0">
				<colgroup>
			 		<col width="100"/>
			 		<col width="120"/>
			 		<col width="100"/>
			 		<col width="120"/>
			 		<col width="100"/>
			 		<col width="120"/>
			 	</colgroup>
				<tr>
					<td align="right">姓　　名：</td>
                    <td align="left">
                        <input type="text" class="easyui-textbox" id="name" name="name"/>
                    </td>
                    <td   align="right" colspan="6" style="margin-right: 15px;"><a href="javascript:void(0)"
                        class="easyui-linkbutton" icon="icon-search" id="searchBtn">查询</a></td>
                    <td align="right"></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center'" id="gridPanel"  style="height: auto;width: 100%">
	</div>
	<div align="center" border="false" style="position:fixed;right:10px;bottom:10px;" id="returnDiv">
   		<div class="btn-group">
   		  <button type="button" class="btn btn-primary" onclick="javascript:save();" id="saveBtn">确认覆盖</button>
		  <button type="button" class="btn btn-primary" onclick="javascript:parent.utils.e.closeWin('coverwin');">关闭窗口</button>
		</div>
   	</div>
</div>
<script type="text/javascript">
/*************************党员信息******************************/
var mainId = '${coverPartyOrgId}';
var method = '${method}';
$(function(){
	loadData();
	
	$("#searchBtn").click(function(){
		reloadData();
	});
});

function getQueryParams() {
	var params = {};
	var fields =$('#queryForm').serializeArray(); //自动序列化表单元素为JSON对象
	$.each( fields, function(i, field){
		params[field.name] = field.value; //设置查询参数
	}); 

	//params["id"] = mainId;
   return   params ; 
	
}

function reloadData(){
	
	$('#gridPanel').datagrid('options').queryParams = getQueryParams();
	
	 $('#gridPanel').datagrid('reload');
}

function loadData(){
	var options = {
			url : ctx+'/cover/coverPartyOrg',
			queryParams : {name:$("#name").val(),coverPartyOrgId:mainId,method:method},
			title: '组织列表',
			rownumbers:true,
			fitColumns : true,
			striped : true,
			singleSelect : false,
			remoteSort: true,
			pagination:true,
			nowrap:false,
			columns : [ [
			     {field :"id",hidden:true}
			    ,{field :"ck",title :"选择",checkbox:true}
				,{field :"name",title :"企业名称",width :"10%", align:"center"}
	            ,{field :"orgType",title :"组织类型",width :"12%", align:"center"}   
	            ,{field :"createOrgTxt",title :"创建单位",width :"15%", align:"center"}
	            ,{field :"orgClass",title :"组织类别",width :"12%", align:"center"}
	         ] ],
	      onLoadSuccess : function(data) {

//	    	  $("a[name='editBtn']").linkbutton({text:'编辑'})
	      }
	};
	
	$('#gridPanel').datagrid(options);
} 

function save(){
	var row = $("#gridPanel").datagrid('getSelections');
	var orgIds = converData(row);
	$.ajax({
		url:ctx + '/cover/saveCoverInfo',
		type:'post',
		data:{mainId:mainId, orgIds:orgIds},
		dataType:'json',
		success:function(result){
			if(result.status ==1){
				alertx(result.msg,function(i){
					parent.utils.e.closeWin('coverwin');
				});
			}
			else
				alertx(result.msg);
		}
	});
}

function converData(orgIdArray){
	var orgIds = "";
	if(method == 'cover'){
		if(orgIdArray.length < 1){
			layer.alert('请选择一条记录！')
			return;
		}
		for(var i = 0; i < orgIdArray.length; i++){
			if(orgIds == ""){
				orgIds = orgIdArray[i].id;
			}else{
				orgIds = orgIds + ',' + orgIdArray[i].id;
			}
		}
	}else if(method == 'edit'){
		if(orgIdArray.length > 0){
			for(var i = 0; i < orgIdArray.length; i++){
				if(orgIds == ""){
					orgIds = orgIdArray[i].id;
				}else{
					orgIds = orgIds + ',' + orgIdArray[i].id;
				}
			}
		}
	}
	return orgIds;
}

function getCheckedRow(){
	var row = $("#gridPanel").datagrid('getSelected');
	return row;
}
</script>
</body>
</html>