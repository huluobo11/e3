package cn.e3mall.search.service;

import cn.e3mall.common.VO.SearchResult;

/**
 * @date 2017年7月14日
 * @author Administrator
 * @project e3-search-interface
 */
public interface SearchService {
SearchResult search(String keyword,int page,int rows) throws Exception;
}
