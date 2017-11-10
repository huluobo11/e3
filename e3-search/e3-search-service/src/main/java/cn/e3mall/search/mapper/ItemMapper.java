package cn.e3mall.search.mapper;

import java.util.List;

import cn.e3mall.common.VO.SearchItem;

/**
 * @date 2017年7月13日
 * @author Administrator
 * @project e3-search-service
 */
public interface ItemMapper {
List<SearchItem> getItemList();
SearchItem getItemById(long id);
}
