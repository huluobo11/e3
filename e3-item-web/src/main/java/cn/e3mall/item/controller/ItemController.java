package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

/**
 * @date 2017年10月26日上午9:30:04
 * @Description: TODO
 * @authorAdministrator
 */
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable("itemId") long itemId, Model model) {
		TbItem tbItem = itemService.getItemById(itemId);
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
		Item item = new Item(tbItem);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", tbItemDesc);

		return "item";
	}
}
