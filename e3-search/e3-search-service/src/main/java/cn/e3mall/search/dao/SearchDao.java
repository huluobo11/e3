package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.VO.SearchItem;
import cn.e3mall.common.VO.SearchResult;

/**
 * @date 2017年7月14日
 * @author Administrator
 * @project e3-search-service
 */
@Repository
public class SearchDao {
	@Autowired
	private SolrServer solrServer;
	public SearchResult search(SolrQuery query) throws SolrServerException {
		QueryResponse response = solrServer.query(query);
		SolrDocumentList solrDocumentList = response.getResults();
		long numFound = solrDocumentList.getNumFound();
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		 SearchResult result=new SearchResult();
		result.setRecordCount(numFound);
		List<SearchItem> itemList=new ArrayList<>();
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem searchItem=new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setCatagory_name((String) solrDocument.get("item_catagory_name"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title="";
			if(null!=list && list.size()>0){
				title=list.get(0);
			}else{
				title=(String) solrDocument.get("item_title");
			}
			searchItem.setTitle(title);
			itemList.add(searchItem);
		}
		result.setItemList(itemList);
		return result;
		
	}
}
