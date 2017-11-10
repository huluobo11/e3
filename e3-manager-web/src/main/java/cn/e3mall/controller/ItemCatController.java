package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.VO.TreeNode;
import cn.e3mall.service.ItemCatService;

/**
 * @date 2017年7月4日
 * @author Administrator
 * @project e3-manager-web
 */
@Controller
public class ItemCatController {
	@Autowired 
private ItemCatService itemCatService;

	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<TreeNode> getItemCatList(@RequestParam(name="id",defaultValue="0")Long parentId){
		List<TreeNode> list = itemCatService.getItemCatList(parentId);
		
		return list;
		
		
	}
}
