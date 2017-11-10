package cn.e3mall.solrJ.test;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * @date 2017年7月13日
 * @author Administrator
 * @project e3-search-service
 */
public class TestSolrJ {
	@Test
	public void addDocument() throws Exception {
		// 创建一个solrServer对象，创建一个连接
		SolrServer solrServer = new HttpSolrServer("http://115.159.149.66:8888/solr-4.10.3/collection1");
		// 创建一个文档，solrInputDocument
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		// 向文档中添加域。文档中必须包含id域。所有的域 的名称 必须 要包含在schema.xml中定义 。
		solrInputDocument.addField("id", "doc01 ");
		solrInputDocument.addField("item_title", "测试商品01 ");
		solrInputDocument.addField("item_price", 1000);
		// 把文档写入索引库
		solrServer.add(solrInputDocument);
		// 提交
		solrServer.commit();
	}
	@Test
	public void delDocument() throws Exception {
		// 创建一个solrServer对象，创建一个连接
		SolrServer solrServer = new HttpSolrServer("http://115.159.149.66:8888/solr-4.10.3/collection1");
		
		//solrServer.deleteById("doc01");
		solrServer.deleteByQuery("id:doc01");
		// 提交
		solrServer.commit();
	}
	@Test
	public void queryIndex() throws Exception {
		// 创建一个solrServer对象，创建一个连接
		SolrServer solrServer = new HttpSolrServer("http://115.159.149.66:8888/solr-4.10.3/collection1");
		//创建一个solrQuery对象
		SolrQuery solrQuery=new SolrQuery();
		//设置查询条件
		//solrQuery.setQuery("*:*");
		solrQuery.set("q", "*:*");
		//执行查询queryResponse
		QueryResponse response = solrServer.query(solrQuery);
		//取出文档列表
		SolrDocumentList results = response.getResults();
		//取出查询结果的总记录数
		long size = results.getNumFound();
		System.out.println(size);
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println(solrDocument.get("item_sell_point"));
		}
		//遍历文档列表，从域中取值。
	}
	@Test
	public void queryIndexMore() throws Exception {
		// 创建一个solrServer对象，创建一个连接
		SolrServer solrServer = new HttpSolrServer("http://115.159.149.66:8888/solr-4.10.3/collection1");
		//创建一个solrQuery对象
		SolrQuery solrQuery=new SolrQuery();
		//设置查询条件
		solrQuery.setQuery("手机");
		solrQuery.setStart(0);
		solrQuery.setRows(20);
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em>");
		solrQuery.setHighlightSimplePost("<em/>");
		//执行查询queryResponse
		QueryResponse response = solrServer.query(solrQuery);
		//取出文档列表
		SolrDocumentList results = response.getResults();
		//取出查询结果的总记录数
		long size = results.getNumFound();
		System.out.println(size);
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			
			String title="";
			Map<String, Map<String, List<String>>> highMap = response.getHighlighting();
			 List<String> list = highMap.get(solrDocument.get("id")).get("item_title");
			 if(list!=null && list.size()>0){
				  title = list.get(0); 
			 }else{
				 title=(String) solrDocument.get("item_title");
			 }
			 System.out.println("______________"+title);
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println(solrDocument.get("item_sell_point"));
		}
		//遍历文档列表，从域中取值。
	}
}
