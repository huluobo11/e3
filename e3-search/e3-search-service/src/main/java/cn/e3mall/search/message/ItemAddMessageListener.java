package cn.e3mall.search.message;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.VO.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;

/**
 * 监听商品添加事件。
 * 
 * @date 2017年10月24日下午4:10:48
 * @Description: TODO
 * @authorAdministrator
 */
public class ItemAddMessageListener implements MessageListener {
@Autowired
private ItemMapper itemMapper;
@Autowired
private SolrServer solrServer;
	@Override
	public void onMessage(Message message) {
		try {
			//先等待事务提交，再发送消息，
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 从商品中取id
			TextMessage textMessage = (TextMessage) message;
			String text = "0";
			text = textMessage.getText();
			long itemId = new Long(text);
			SearchItem item = itemMapper.getItemById(itemId);
			// 创建文档对象 ，
			SolrInputDocument solrInputDocument = new SolrInputDocument();
			solrInputDocument.addField("id", item.getId());
			// 向文档中添加域。
			solrInputDocument.addField("item_title",item.getTitle());
			solrInputDocument.addField("item_sell_point",item.getSell_point() );
			solrInputDocument.addField("item_price",item.getPrice() );
			solrInputDocument.addField("item_image",item.getImage() );
			solrInputDocument.addField("item_category_name",item.getCatagory_name() );
			solrServer.add(solrInputDocument);
			solrServer.commit();
		} catch (JMSException | SolrServerException | IOException e) {
			e.printStackTrace();
		}
		// 添加文档到索引库
	}

}
