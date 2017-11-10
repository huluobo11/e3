package cn.e3mall.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.VO.SearchItem;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchItemService;

/**
 * @date 2017年7月13日
 * @author Administrator
 * @project e3-search-service
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {
	@Autowired
	private ItemMapper itemMapper;
@Autowired
private SolrServer solrServer;
	@Override
	public E3Result importAllItems() {
		// 查询商品列表 ，
		try {
			List<SearchItem> itemList = itemMapper.getItemList();
			for (SearchItem item : itemList) {
				// 创建文档对象 ，
				SolrInputDocument solrInputDocument=new SolrInputDocument();
				// 向文档中添加域。
				solrInputDocument.addField("id",item.getId() );
				solrInputDocument.addField("item_title",item.getTitle());
				solrInputDocument.addField("item_sell_point",item.getSell_point() );
				solrInputDocument.addField("item_price",item.getPrice() );
				solrInputDocument.addField("item_image",item.getImage() );
				solrInputDocument.addField("item_category_name",item.getCatagory_name() );
				// 把文档加入索引库。
				solrServer.add(solrInputDocument);
				// 提交。
				solrServer.commit();
			}
			return E3Result.ok();
		} catch (SolrServerException e) {
			e.printStackTrace();
			return E3Result.build(500, "数据 导入时发生异常");
		} catch (IOException e) {
			e.printStackTrace();
			return E3Result.build(500, "数据 导入时发生异常");
		}
	}

}
