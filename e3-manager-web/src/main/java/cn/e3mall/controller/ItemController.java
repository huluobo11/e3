package cn.e3mall.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.VO.BaseCustom;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

/**
 * @date 2017年6月29日
 * @author Administrator
 * @project e3-manager-web
 */
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
@RequestMapping(value="/item/{itemId}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public TbItem getItemById(@PathVariable  long itemId){
		TbItem itemById = itemService.getItemById(itemId);
		return itemById;
	}
	@RequestMapping("/item/list")
	@ResponseBody
	public BaseCustom getItemList(Integer page,Integer rows){
		return itemService.getItemList(page, rows);
	}
	
	@RequestMapping(value="item/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item,String desc){
		E3Result  result=itemService.addItem(item, desc);
		return result;
	}
}
