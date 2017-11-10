package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.VO.TreeNode;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.content.service.ContentCategoryService;

/**
 * @date 2017年7月11日
 * @author Administrator
 * @project e3-manager-web
 */
@Controller
public class ContentCatController {
	@Autowired
private ContentCategoryService contentCategoryService;
	@RequestMapping("/content/category/list")
	@ResponseBody
public List<TreeNode> getContentCatList(@RequestParam(name="id",defaultValue="0") Long parentId){
		List<TreeNode> nodeList = contentCategoryService.getContentCategoryList(parentId);
		
		return nodeList;
}
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContentCategory(Long parentId,String name){
		E3Result result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
}
