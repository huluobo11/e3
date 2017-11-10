package cn.e3mall.sso.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.sso.service.TokenService;

/**
 * @date 2017年11月2日上午9:49:24
 * @Description: 根据token查询用户信息
 * @authorAdministrator
 */
@Controller
public class TokenController {
	@Autowired
	private TokenService tokenService;

/*	@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback) {
		// 判断是否为jsonp请求。
		E3Result result = tokenService.getUserByToken(token);
		if (StringUtils.isNotBlank(callback)) {
			return callback + "(" + JsonUtils.objectToJson(result) + ");";
		}
		return JsonUtils.objectToJson(result);
	}*/
	
	@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {
		// 判断是否为jsonp请求。
		E3Result result = tokenService.getUserByToken(token);
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return JsonUtils.objectToJson(result);
	}
}
