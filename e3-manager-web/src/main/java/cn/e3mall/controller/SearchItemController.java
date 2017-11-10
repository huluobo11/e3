package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.search.service.SearchItemService;

/**
 * @date 2017年7月13日
 * @author Administrator
 * @project e3-manager-web
 */
@Controller
public class SearchItemController {
	@Autowired
	private SearchItemService searchItemService;
@RequestMapping("/index/item/import")
@ResponseBody
public E3Result importItemList(){
	E3Result result = searchItemService.importAllItems();
	return result;
}
}
