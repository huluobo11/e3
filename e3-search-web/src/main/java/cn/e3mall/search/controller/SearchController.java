package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.VO.SearchResult;
import cn.e3mall.search.service.SearchService;

/**
 * @date 2017年7月14日
 * @author Administrator
 * @project e3-search-web
 */
@Controller
public class SearchController {
	@Value("${SEARCH_RESULT_ROWS}")
	private int SEARCH_RESULT_ROWS;
	@Autowired
	private SearchService searchService;

	@RequestMapping("/search")
	public String searchItemList(String keyword,
			@RequestParam(defaultValue = "1") Integer page, Model model)
			throws Exception {
		keyword=new String(keyword.getBytes("iso8859-1"),"utf-8");
		SearchResult searchResult = searchService.search(keyword, page,SEARCH_RESULT_ROWS);
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recordCount", searchResult.getRecordCount());
		model.addAttribute("itemList", searchResult.getItemList());
		
		return "search";
	}

}
