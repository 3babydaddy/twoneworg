<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/index/include.jsp"%>
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
			<input type="hidden" id="orgIds" name="orgIds" value="${orgIds}" />
		</form>
	</div>
	<div data-options="region:'center'" id="gridPanel"  style="height: auto;width: 100%">
	</div>
</div>
<script type="text/javascript">
/*************************党员信息******************************/
//var mainId = ${id};
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
			url : ctx+'/unpublic/showPartyInfo',
			queryParams : {name:$("#name").val(), orgIds:$("#orgIds").val()},
			title: '党员列表',
			rownumbers:true,
			fitColumns : true,
			striped : true,
			singleSelect : true,
			remoteSort: true,
			pagination:true,
			nowrap:false,
			columns : [ [
			     {field :"id",hidden:true}
				,{field :"name",title :"姓名",width :"10%", align:"center"}
	            ,{field :"gender",title :"性别",width :"5%", align:"center"}   
	            ,{field :"birthday",title :"出生日期",width :"10%", align:"center",formatter:Common.DateFormatter}
	            ,{field :"education",title :"学历",width :"10%", align:"center"}
	            ,{field :"partymbrInUnpublicIs",title :"组织关系在非公企业",width :"12%", align:"center"}   
	            ,{field :"partymbrMiddleManagerIs",title :"中层管理人员",width :"8%", align:"center"}   
	            ,{field :"partymbrOnMiddletechIs",title :"中高级专业技术人员",width :"12%", align:"center"}   
	            ,{field :"partymbrFrontlineIs",title :"生产经营一线职工",width :"12%", align:"center"}   
	            ,{field :"partymbrNotinUnpublicIs",title :"组织关系不在非公企业",width :"12%", align:"center"}   
	            ,{field :"partymbrInVillageIs",title :"农村党员",width :"8%", align:"center"}   
	         ] ],
	      onLoadSuccess : function(data) {

//	    	  $("a[name='editBtn']").linkbutton({text:'编辑'})
	      }
	};
	
	if('edit' == '${method}' && '${main.status}' != '2'){
		options.toolbar=[{
		
		}];
	}
	
	$('#gridPanel').datagrid(options);
	
	
} 


function getCheckedRow(){
	var row = $("#gridPanel").datagrid('getSelected');
	return row;
}
</script>
</body>
</html>