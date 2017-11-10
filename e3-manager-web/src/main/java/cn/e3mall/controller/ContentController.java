package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * @date 2017年7月12日
 * @author Administrator
 * @project e3-content-service
 */
@Controller
public class ContentController {
@Autowired
private ContentService contentService;
@RequestMapping(value="/content/save",method=RequestMethod.POST)
@ResponseBody
public E3Result addContent(TbContent content){
	return contentService.addContent(content);
}
}
