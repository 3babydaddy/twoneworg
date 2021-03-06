package com.tf.base.common.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tf.base.common.constants.CommonConstants;

import net.sf.json.JSONObject;



@Controller
public class CitysController {

	@RequestMapping(value="/citys/getList")
	@ResponseBody
	public JSONObject getList(String level){
		
		String cityPath = "";
		String str = "";
		if("1".equals(level)){
			cityPath = CommonConstants.ROOT_DIR + "/resources/data/cityAreaWithOnly.json";
			
		}else if("2".equals(level)){
			
			cityPath = CommonConstants.ROOT_DIR + "/resources/data/cityWithOnly.json";
		}else if("3".equals(level)){
			cityPath = CommonConstants.ROOT_DIR + "/resources/data/city.json";
			
		}else{
			
			return new JSONObject();
		}
		
		try {
			str = FileUtils.readFileToString(new File(cityPath),"utf-8");
			
			return JSONObject.fromObject(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONObject();
	}
	@RequestMapping(value="/citys/getList/{code}")
	@ResponseBody
	public JSONObject getListByCode(@PathVariable("code")String code){
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		String cityPath = CommonConstants.ROOT_DIR + "/resources/data/" +code+ ".json";
		String str = "";
		try {
			str = FileUtils.readFileToString(new File(cityPath),"utf-8");
			
			return JSONObject.fromObject(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	public static String getListByCity(){
		
		String cityPath = CommonConstants.ROOT_DIR + "/resources/data/city.json";
		String str = "";
		try {
			str = FileUtils.readFileToString(new File(cityPath),"utf-8");
			
			return str.replace("{", "").replace("}", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
