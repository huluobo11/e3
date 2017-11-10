package cn.e3mall.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * @date 2017年11月1日上午8:52:52
 * @Description: 监听商品添加的消息，生成静态页面。
 * @authorAdministrator
 */
public class HtmlGenListener implements MessageListener {
@Autowired
private ItemService itemService;
@Autowired
private FreeMarkerConfigurer freeMarkerConfigurer;
@Value("${HTML_GEN_PATH}")
private  String HTML_GEN_PATH;
	

	@Override
	public void onMessage(Message message) {
		// 创建一个模板
		// 从消息中取出id
		try {
			TextMessage textMessage=(TextMessage) message;
			String text= textMessage.getText();
			//先等待添加商品的事务提交。
			Thread.sleep(1000);
			// 查询商品信息，
			Long itemId=new Long(text);
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			//创建一个数据集，保存数据
			HashMap<Object, Object> hashMap = new HashMap<>();
			hashMap.put("item", item);
			hashMap.put("itemDesc", itemDesc);
			//加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//创建一个输出流。指定输出目录和文件名
			FileWriter out = new FileWriter(new File(HTML_GEN_PATH+itemId+".html"));
			//生成静态页面，关闭流。
			template.process(hashMap, out);
		} catch (JMSException | IOException | TemplateException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
