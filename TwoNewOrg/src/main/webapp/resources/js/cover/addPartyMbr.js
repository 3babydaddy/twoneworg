
$(function(){
	
	$("#editForm").validate({
	    rules: {
	      name:{
		        required: true
		      },
	      gender:{
	    	  required: true
	      },
	      birthday:{
	    	  required: true
	      },
	      education:{
	    	  required: true
	      }      
	      
	    },
	    messages: {
	    	name: "请输入姓名",
		    gender: {
		    	required: "请选择性别"
		    },
		    birthday: {
		    	required: "请选择出生日期"
		    },
		    education: {
		    	required: "请选择最高学历"
		    }
	    },
	    submitHandler:function(form){
	    	
	    	onSubmit();
        } 
	});
	var set = {
			nonSelectedText: '请选择',
			numberDisplayed: 10,
			allSelectedText:'全部'
		    };
	//$("#otherCondition").multiselect(set);
});
function save(){
	$("#editForm").submit();
}

function onSubmit(){
	$.post(ctx + "/cover/addPartymbr",getJsonParams("editForm"),function(result){
    	$(".btn").prop('disabled',false);
    	if(result.status == 1){
        	alertx(result.msg,function(){
        			parent.utils.e.closeWin('addpartymbrWin');
        	});
    	}else{
    		alertx(result.msg);
    	}

    });
}

function getChecked(checkName){
	var chk_value =[]; 
	$('input[name="'+checkName+'"]:checked').each(function(){ 
		chk_value.push($(this).val()); 
	}); 
	return chk_value.join(",");
}