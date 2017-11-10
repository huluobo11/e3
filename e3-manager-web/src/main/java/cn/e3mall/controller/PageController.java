package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转
 * @date 2017年7月3日
 * @author Administrator
 * @project e3-manager-web
 */
@Controller
public class PageController {

	@RequestMapping("/")
	public String showIndex(){
		
		return "index";
	}
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		
		return page;
	}
}
