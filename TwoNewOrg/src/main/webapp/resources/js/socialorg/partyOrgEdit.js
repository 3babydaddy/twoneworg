
$(function(){
	
	 validate();
		
	 initPartyOrgTree();
	 
	 var set = {
				nonSelectedText: '请选择',
				numberDisplayed: 10,
				allSelectedText:'全部'
	 };
	 var instructSize = $("#instructSize").val();
	 for(var s = 0; s < instructSize; s++){
		$("#zhidaodanwei"+s).multiselect(set);
	 }
	 if(instructSize == 0){
		 $("#zhidaodanwei0").multiselect(set);
	 }

	 //下级党组织信息
	 var s = 0;
	 $("#add_operate_lowerParty").click(function(){
		$(this).before($(this).prev().clone());
		if(s == 0){
			$(this).prev().append('<button type="button"  class="remove_operate_lowerParty btn btn-primary btn-sm glyphicon glyphicon-minus"></button>');
		}
		s++;
		$(this).prev().find(".operate_address").val("").trigger('change');
		$(this).prev().find("input[name=lowerPartyOrgName]").val("");
		$(this).prev().find("input[name=lowerPartyOrgType]").val("");
		$(this).prev().find("input[name=lowerPartyOrgSum]").val("");
	});
	
	$("#editForm").on('click','.remove_operate_lowerParty',function(){
			$(this).parent().remove();
		s--;
	});
	 //指导员信息
	 var t = 0;
	 $("#add_operate_instructor").click(function(){
		$(this).before($(this).prev().clone());
		if(t == 0){
			$(this).prev().append('<button type="button"  class="remove_operate_instructor btn btn-primary btn-sm glyphicon glyphicon-minus"></button>');
		}
		t++;
		$(this).prev().find(".operate_address").val("").trigger('change');
		$(this).prev().find("input[name=instructorName]").val("");
		$(this).prev().find("input[name=instructorJob]").val("");
		$(this).prev().find("select[id=zhidaodanwei"+(t-1)+"]").attr('id','zhidaodanwei'+t);
		$(this).prev().find("div[class=btn-group]")[0].style.display='none';
		$("#zhidaodanwei"+t).multiselect("destroy").val("").multiselect(set);
	
	});
	
	$("#editForm").on('click','.remove_operate_instructor',function(){
			$(this).parent().remove();
		t--;
	});
	 //换届信息
	 var i = 0;
		$("#add_operate_address").click(function(){
			$(this).before($(this).prev().clone());
			if(i == 0){
				$(this).prev().append('<button type="button"  class="remove_operate_address btn btn-primary btn-sm glyphicon glyphicon-minus"></button>');
			}
			i++;
			$(this).prev().find(".operate_address").val("").trigger('change');
			$(this).prev().find("input[id=partymbrInUnpublicNum"+(i-1)+"]").attr('id','partymbrInUnpublicNum'+i).val("");
			$(this).prev().find("input[id=filepartymbrUnderThirtyfiveNum"+(i-1)+"]").attr('id','filepartymbrUnderThirtyfiveNum'+i).val("");
			$(this).prev().find("input[id=partymbrUnderThirtyfiveNum"+(i-1)+"]").attr('id','partymbrUnderThirtyfiveNum'+i).val("");
		});
		
		$("#editForm").on('click','.remove_operate_address',function(){
				$(this).parent().remove();
				i--;
		});
		
		//党副信息
		var j = 0;
		$("#add_operate_party").click(function(){
			$(this).before($(this).prev().clone());
			if(j == 0){
				$(this).prev().append('<button type="button"  class="remove_operate_party btn btn-primary btn-sm glyphicon glyphicon-minus"></button>');
			}
			j++;
			$(this).prev().find(".operate_party").val("").trigger('change');
			$(this).prev().find("select[name=deputySecretaryType]").val("");
			$(this).prev().find("input[name=deputySecretaryName]").val("");
			$(this).prev().find("input[name=deputySecretaryBirthdayTxt]").val("");
			$(this).prev().find("select[name=deputySecretarySex]").val("");
			$(this).prev().find("select[name=deputySecretaryEducation]").val("");
			$(this).prev().find("select[name=deputySecretaryIsFullTime]").val("");
			$(this).prev().find("select[name=isBoardOfficer]").val("");
		});
		
		$("#editForm").on('click','.remove_operate_party',function(){
				$(this).parent().remove();
				j--;
		});
		//是否建立党组织
		$("#isSetUpPartyOrg").on('change', function(){
			var setUpSign = $("#isSetUpPartyOrg").val();
			isSetUpPartyOrg(setUpSign);
		});
		//党组织组建形式
		$("#partyOrgForm").on('change', function(){
			var hiddenFlag = $("#partyOrgForm").val();
			showChangInfo(hiddenFlag);
		});
		
		//根据党组织类型；隐藏或者显示下级党组织信息
		$("#partyOrgType").on('change', function(){
			showLowerPartyInfo();
		});
		//是否选派指导员，隐藏或者显示指导员信息
		$("#isInstructor").on('change', function(){
			showInstructorInfo();
		});
		//页面的显示或隐藏顺序
		var setUpSign = $("#isSetUpPartyOrg").val();
		isSetUpPartyOrg(setUpSign);
		if(setUpSign == 1 && setUpSign != ""){
			var hiddenFlag = $("#partyOrgForm").val();
			showChangInfo(hiddenFlag);
			if(hiddenFlag != 3){
				showLowerPartyInfo();
				showInstructorInfo();
			}
		}
		//书记所在单位
		$("#secretarySource").on('change', function(){
			secretarySourceSelect();
		});
		secretarySourceSelect();
		//点击列表页上的上报按钮事件
		if($("#clickSign").val() == 'clickSign'){
			submitReport();
		}
		
});

	function changeOrg(obj){
		$(obj).remove();
		
		//指导单位
		var site = new Array();
		var unitArray = $("button[name='btn_org_name']");
		window.parent.$("select[name='instructorUnitTxt']").empty();
		if(unitArray.length > 0){
			for(var r = 0; r < unitArray.length; r++){
				site.push({
		            label : unitArray[r].textContent,
		            value : unitArray[r].value
		        });
			}
		}
		$("select[name='instructorUnitTxt']").multiselect('dataprovider',site);
	}
	
	function getQueryParams() {
		var params = {};
		var fields =$('#editForm').serializeArray(); //自动序列化表单元素为JSON对象
		$.each( fields, function(i, field){
			params[field.name] = field.value; //设置查询参数
		}); 
	
	   return   params ; 
		
	}
	
	function reloadData(){
		var params = getQueryParams();
		
		$('#gridPanel').datagrid('options').queryParams = params;
		
		 $('#gridPanel').datagrid('reload');
	}
	
	function isSetUpPartyOrg(setUpSign){
		if(setUpSign == 0 && setUpSign != ""){
			document.getElementById("absencePartyOrgReasion").disabled=false;
			hiddenAllsign(setUpSign);
		}else{
			document.getElementById("absencePartyOrgReasion").disabled=true;
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
		var setUpSign = $("#isInstructor").val();
		if(setUpSign == 0 && setUpSign != ""){
			$("#divOne").hide();
		}else{
			$("#divOne").show();
		}
	}
	
	function showLowerPartyInfo() {
		var hiddenFlag = $("#partyOrgType").val();
		if (hiddenFlag == 0 || hiddenFlag == 1) {
			$("#divEight").show();
		} else {
			$("#divEight").hide();
		}
	}
	
	function hiddenAllsign(flag){
		if((flag == 0 && flag != "") || flag == 3){
			//document.getElementById("partyOrgForm").style.display='none';
			document.getElementById("partyOrgType").style.display='none';
			document.getElementById("isInstructor").style.display='none';
		}
		if(flag == 0 && flag != ""){
			$("#partyOrgForm").hide();
			$("#partyOrgTimeTxt").hide();
			$("#filepartyOrgAttachment").hide();
			$("#divOne").show();
		}else{
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
		if((flag == 0 && flag != "") || flag != 3){
			//document.getElementById("partyOrgForm").style.display='';
			document.getElementById("partyOrgType").style.display='';
			document.getElementById("isInstructor").style.display='';
			$("#partyOrgForm").show();
			$("#partyOrgTimeTxt").show();
			$("#filepartyOrgAttachment").show();
		}
		showInstructorInfo();
		document.getElementById("divTwo").style.display='';
		document.getElementById("divThree").style.display='';
		document.getElementById("divFour").style.display='';
		document.getElementById("divFive").style.display='';
		document.getElementById("divSix").style.display='';
		document.getElementById("divSev").style.display='';
		document.getElementById("divEight").style.display='';
	}
	
	function secretarySourceSelect(){
		var secretarySource = $("#secretarySource").val();
		if(secretarySource == 2){
			$("#secretaryCompanyOne").show();
			$("#secretaryCompanyTwo").hide();
			document.getElementById("secretaryCompanyOne").disabled=false;
			document.getElementById("secretaryCompanyTwo").disabled=true;
		}else{
			$("#secretaryCompanyOne").hide();
			$("#secretaryCompanyTwo").show();
			document.getElementById("secretaryCompanyOne").disabled=true;
			document.getElementById("secretaryCompanyTwo").disabled=false;
		}
	}
	
	function save(flag){
		//debugger;
		if(flag == 0){
			$("#reportHigher").val(0);
			submit(0);
		}else if(flag == 1){
			myconfirm('一旦上报信息不可修改，您确认要上报吗？',function(r){    
				if(r){
					submit(1);
				}
			});
		}
	}
	
	function submitReport(){
		$("#reportHigher").val(1);
		$("#editForm").submit();
	}
	
	function submit(flag){
		$(".btn").prop('disabled',true);
		var orgIds = "";
		//选择的组织数据
		var orgIdArray = document.getElementsByName('btn_org_name');
		if(orgIdArray.length > 0){
			for(var t = 0; t < orgIdArray.length; t++){
				if(orgIds == ""){
					orgIds = orgIdArray[t].value;
				}else{
					orgIds = orgIds + ',' + orgIdArray[t].value;
				}
			}
		}else{
			$.messager.alert("请选择建立党组织的企业！");
			return;
		}
		$("#partyOrgIds").val(orgIds);
		lowerPartyInfoConver();
		instructConver();
		changeInfoConver();
		deputySecConver();
		$.ajax({
			url:'../socialorg/partyorgedit',
			type : 'post',
			data:$("#editForm").serialize(),
			//dataType:'json',
			async: false,
			success:function(result){
				$(".btn").prop('disabled',false);
				if(result.status == 1){
			    	alertx(result.msg,function(){
			    		if(flag == 0)
			    			//window.location.reload();
			    			parent.utils.e.closeWin('editwin');
			    		else
			    			parent.utils.e.closeWin('editwin');
			    	});
				}else{
					alertx(result.msg);
				}
			},
			 error: function(XMLHttpRequest, textStatus, errorThrown) {
				 debugger;
			 },
		});
	}
	
	function validate(){
		$("#editForm").validate({
		    rules: {
		      orgIds:{
			        required: true
			      },
			  isSetUpPartyOrg:{
		    	  required: true
		      },
		      absencePartyOrgReasion:{
		    	  required: true
		      },
		      partyOrgForm:{
		    	  required: true
		      },
		      partyOrgType:{
		    	  required: true
		      },
		      isInstructor:{
		    	  required: true
		      },
		      instructorName:{
		    	  required: true
		      },
		      instructorUnitTxt:{
		    	  required: true
		      },
		      instructorJob:{
		    	  required: true
		      },
		      partyOrgTimeTxt:{
		    	  required: true
		      },
		      filepartyOrgAttachment:{
		    	  required: true
		      },
		      partyOrgName:{
		    	  required: true
		      },
		      partyOrgTel:{
		    	  required: true,
		    	  isMobile:true
		      },
		      secretaryName:{
		    	  required: true
		      },
		      secretaryBirthdayTxt:{
		    	  required: true
		      },
		      secretaryEducation:{
		    	  required: true
		      },
		      secretarySource:{
		    	  required: true
		      },
		      secretaryCompanyTxt:{
		    	  required: true
		      },
		      partTimeWorkers:{
		    	  required: true,
		    	  maxlength:8,
		    	  number: true
		      },
		      fullTimeWorkers:{
		    	  required: true,
		    	  maxlength:8,
		    	  number: true
		      },
		      stageArea:{
		    	  required: true,
		    	  maxlength:8,
		    	  number: true
		      },
		      otherShareStage:{
		    	  required: true
		      },
		      communityShareStage:{
		    	  required: true
		      },
		      otherInfo:{
		    	  required: true
		      },
		      isPartyIntoSchool:{
		    	  required: true
		      },
		      parentPartyOrgType:{
		    	  required: true
		      }
		    },
		    messages: {
		    	name: "请输入名称",
		    	isSetUpPartyOrg: {
		    		required: "请选择是否成立党组织"
			    },
			    absencePartyOrgReasion: {
		    		required: "请选择未建立党组织原因"
			    },
			    partyOrgForm: {
		    		required: "请选择党组织组建形式"
			    },
			    partyOrgType: {
		    		required: "请选择党组织类别"
			    },
			    isInstructor: {
		    		required: "请选择是否选派党建工作指导员或联络员"
			    },
			    instructorName: {
		    		required: "请填写指导员或联络员的姓名"
			    },
			    instructorUnitTxt: {
		    		required: "请填写指导员或联络员的单位"
			    },
			    instructorJob: {
		    		required: "请填写指导员或联络员职务"
			    },
			    partyOrgTimeTxt: {
		    		required: "请填写党组织成立时间"
			    },
			    filepartyOrgAttachment: {
		    		required: "请上传党组织成立相关附件"
			    },
			    partyOrgName: {
		    		required: "请输入党组织名称"
			    },
			    partyOrgTel: {
		    		required: "请输入党组织联系电话"
			    },
			    secretaryName: {
		    		required: "请输入书记姓名"
			    },
			    secretaryBirthdayTxt: {
		    		required: "请输入书记出生日期"
			    },
			    secretaryEducation: {
		    		required: "请选择书记学历"
			    },
			    secretarySource: {
		    		required: "请选择书记来源"
			    },
			    secretaryCompanyTxt: {
		    		required: "请选择书记所在单位"
			    },
			    partTimeWorkers: {
		    		required: "请输入兼职人数"
			    },
			    fullTimeWorkers: {
		    		required: "请输入专职人数"
			    },
			    stageArea: {
		    		required: "请输入单独建设活动场所面积"
			    },
			    otherShareStage: {
		    		required: "请选择与其他单位党组织共用活动场所"
			    },
			    communityShareStage: {
		    		required: "请选择与社区共用活动场所活动场所"
			    },
			    otherInfo: {
		    		required: "请输入其他"
			    },
			    isPartyIntoSchool: {
		    		required: "请选择是否纳入学校章程"
			    },
			    parentPartyOrgType: {
		    		required: "请选择隶属上级党组织类别"
			    }
		    },
		    submitHandler:function(form){
		    	save(1);
	        } 
		});
	}
	
	 //正则表达式验证的写法
    $.validator.addMethod("isMobile",function(value,element,params){  
        var doubles= /^[0-9]+$/;  
        return this.optional(element)||(doubles.test(value));  
    },"请填写正确的手机号"); 
	
	var departmentSetting = {
			check: {
				enable: false
			},
			data: {
				simpleData: {
					enable: true,
					idKey:'id',
					pIdKey:'parentPartyOrg'
				}
			},
			callback: {
	//			beforeClick: beforeClick,
				onClick: onClick
			}
		};
	
	function initPartyOrgTree(){
		$.ajax({
			url : "../basemanage/partyorginfoquery",
			type : "POST",
			data : {
				name : '',
				status:'1'
			},
			success : function(data) {
				var tree = $.fn.zTree.init($("#parentDepartMent"), departmentSetting, data);
				tree.expandAll(true);
				var higherDepartHid = $("#subjectionPartyId").val(); 
				var treeObj = $.fn.zTree.getZTreeObj("parentDepartMent");
				var nodes = treeObj.getNodeByParam('id', higherDepartHid);
				if(nodes != null){
					//$("#subjectionPartyName").attr("value", nodes.name);	
					$("#higherDepart").attr("value", nodes.name);
				}
			}
		})
	}
	
	function onClick(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("parentDepartMent"),
		nodes = zTree.getSelectedNodes(),
		nodesValue = "";
		nodesId = "";
		nodes.sort(function compare(a,b){return a.id-b.id;});
		for (var i=0, l=nodes.length; i<l; i++) {
			nodesValue += nodes[i].name + ",";
			nodesId += nodes[i].id + ",";
		}
		if (nodesValue.length > 0 ) {
			nodesValue = nodesValue.substring(0, nodesValue.length-1);		
			nodesId = nodesId.substring(0, nodesId.length-1);		
		}
		$("#higherDepart").attr("value", nodesValue);
		$("#subjectionPartyId").attr("value", nodesId);
		$("#subjectionPartyName").attr("value", nodesValue);
	}
	function isIE(){
		if (window.navigator.userAgent.indexOf("MSIE")>=1) 
		return true; 
		else
		return false; 
		}
	function showMenu() {
		var higherDepart = $("#higherDepart");
		var higherDepartOffset = $("#higherDepart").offset();
		if(isIE())
			$("#parentPartyOrg").css({left:higherDepartOffset.left + "px", "top":(higherDepartOffset.top + higherDepart.outerHeight())/2 + "px"}).slideDown("fast");
		else
			$("#parentPartyOrg").css({left:higherDepartOffset.left + "px", top:higherDepartOffset.top + higherDepart.outerHeight() + "px"}).slideDown("fast");
	
		$("#parentPartyOrg").css("zIndex",999);
		$("body").bind("mousedown", onBodyDown);
	}
	function hideMenu() {
		$("#parentPartyOrg").fadeOut("fast");
		$("body").unbind("mousedown", onBodyDown);
	}
	function onBodyDown(event) {
		if (!(event.target.id == "menuBtn" || event.target.id == "parentPartyOrg" || $(event.target).parents("#parentPartyOrg").length>0)) {
			hideMenu();
		}
	}
	
	function showUpload(obj, action){
		var id = $("#"+obj.id.replace('file','')).val();
		var filename = $("#"+obj.id).val();
		var mainId = $("#mainId").val();
		var modular = $("#modular").val();
		var type = $("#type").val();
		var action = action;
		var url = ctx + '/socialorg/uploadFile?id='+id+'&mainTableId='+mainId+'&filename='+filename+
							'&sign='+obj.id+'&modular='+modular+'&type='+type+'&action='+action+'&method=edit';
		//openWin("撤销", url,"50%","90%",function(){reloadData()});
		utils.e.openWin('cancelwin','文件上传',url,"60%","30%",function(){
			//reloadData()
		});
	}
	
	function showPartyInfo(orgIds){
		var url = ctx + '/socialorg/showPartyInfo?orgIds='+orgIds;
		utils.e.openWin('showPartyInfoWin','党员基本信息',url,"80%","30%",function(){
			//reloadData()
		});
	}
	
	function showOrgInfoPop(){
		var url = ctx + '/social/showOrgInfoPop';
		utils.e.openWin('showOrgInfoPop','组织信息',url,"60%","30%",function(){
			//reloadData()
		});
	}
	
	function lowerPartyInfoConver(){
		var LowerPartyArray = new Array();
		var lpoNames = document.getElementsByName("lowerPartyOrgName");
		var lpoTypes = document.getElementsByName("lowerPartyOrgType");
		var lpoSums = document.getElementsByName("lowerPartyOrgSum");
		for(var i = 0; i < lpoNames.length; i++){
			if(lpoNames[i].value != "" || lpoTypes[i].value != ""){
				var info = createLowerPartyInfo(lpoNames[i].value, lpoTypes[i].value,lpoSums[i].value);
				LowerPartyArray.push(info);
			}
		}
		//debugger;
		$("#lowerPartyList").val(JSON.stringify(LowerPartyArray));
	}
	
	function instructConver(){
		var instructArray = new Array();
		var selectArray = new Array();
		var names = document.getElementsByName("instructorName");
		var jobs = document.getElementsByName("instructorJob");
		for(var i = 0; i < names.length; i++){
			if(names[i].value != "" || jobs[i].value != "" || $("#zhidaodanwei"+[i]).val() != null){
				selectArray = $("#zhidaodanwei"+[i]).val();
				var selectStr = "";
				if(selectArray != null){
					for(var j = 0; j < selectArray.length; j++){
						if(selectStr == ""){
							selectStr = selectArray[j];
						}else{
							selectStr = selectStr + ',' + selectArray[j];
						}
					}
				}
				var info = createInstructInfo(names[i].value, jobs[i].value, selectStr);
				instructArray.push(info);
			}
		}
		$("#instructList").val(JSON.stringify(instructArray));
	}
	
	function changeInfoConver(){
		var changeArray = new Array();
		var changeTimes = document.getElementsByName("partymbrInUnpublicNum");
		var changeFiles = document.getElementsByName("partymbrUnderThirtyfiveNum");
		for(var i = 0; i < changeTimes.length; i++){
			if(changeTimes[i].value != "" || changeFiles[i].value != ""){
				var info = createChangeInfo(changeTimes[i].value, changeFiles[i].value);
				changeArray.push(info);
			}
		}
		//debugger;
		$("#changeList").val(JSON.stringify(changeArray));
	}
	
	function deputySecConver(){
		var deputySecArray = new Array();
		var types = document.getElementsByName("deputySecretaryType");
		var names = document.getElementsByName("deputySecretaryName");
		var birthdayTxts = document.getElementsByName("deputySecretaryBirthdayTxt");
		var sexs = document.getElementsByName("deputySecretarySex");
		var educations = document.getElementsByName("deputySecretaryEducation");
		var isFullTimes = document.getElementsByName("deputySecretaryIsFullTime");
		var isBoardOfficers = document.getElementsByName("isBoardOfficer");
		for(var i = 0; i < types.length; i++){
			if(types[i].value != "" || names[i].value != "" || birthdayTxts[i].value != "" || sexs[i].value != ""
					|| educations[i].value != "" || isFullTimes[i].value != "" || isBoardOfficers[i].value != ""){
				var info = createDuputySecInfo(types[i].value, names[i].value, birthdayTxts[i].value, sexs[i].value
						, educations[i].value, isFullTimes[i].value, isBoardOfficers[i].value);
				deputySecArray.push(info);
			}
		}
		$("#deputySecretaryList").val(JSON.stringify(deputySecArray));
	}
	
	//下级党组织信息实体类
	function createLowerPartyInfo(lowerPartyOrgName, lowerPartyOrgType, lowerPartyOrgSum){
		var LowerPartyInfo = new Object();
		LowerPartyInfo.lowerPartyOrgName = lowerPartyOrgName;
		LowerPartyInfo.lowerPartyOrgType = lowerPartyOrgType;
		LowerPartyInfo.lowerPartyOrgSum = lowerPartyOrgSum;
		return LowerPartyInfo;
	}
	
	//指导员信息实体类
	function createInstructInfo(instructorName, instructorJob, instructOrgs){
		var instructInfo = new Object();
		instructInfo.instructorName = instructorName;
		instructInfo.instructorJob = instructorJob;
		instructInfo.instructOrgs = instructOrgs;
		return instructInfo;
	}
	//换届信息实体类
	function createChangeInfo(changeTimeTxt, changeAttachmentId){
		var changeInfo = new Object();
		changeInfo.changeTimeTxt = changeTimeTxt;
		changeInfo.changeAttachmentId = changeAttachmentId;
		return changeInfo;
	}
	//党务信息实体类
	function createDuputySecInfo(deputySecretaryType, deputySecretaryName, deputySecretaryBirthdayTxt, deputySecretarySex,
								deputySecretaryEducation, deputySecretaryIsFullTime, isBoardOfficer){
		var duputySecInfo = new Object();
		duputySecInfo.deputySecretaryType = deputySecretaryType;
		duputySecInfo.deputySecretaryName = deputySecretaryName;
		duputySecInfo.deputySecretaryBirthdayTxt = deputySecretaryBirthdayTxt;
		duputySecInfo.deputySecretarySex = deputySecretarySex;
		duputySecInfo.deputySecretaryEducation = deputySecretaryEducation;
		duputySecInfo.deputySecretaryIsFullTime = deputySecretaryIsFullTime;
		duputySecInfo.isBoardOfficer = isBoardOfficer;
		return duputySecInfo;
	}
	
	function getElByName(name){
		return $("input[name="+name+"]");
	}
	function showDept(){
		showDeptTree("instructorUnit","createOrgTxt");
	}
	function showDept1(){
		showDeptTree("secretaryCompany","createOrgTxt1");
	}
