/**
 * 
 */
package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.VO.BaseCustom;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.IDUtils;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemDescExample;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;

/**
 * @date 2017年6月29日
 * @author Administrator
 * @project e3-manager-service
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_INFO}")
	private String ITEM_INFO;

	@Value("${ITEM_CHCHE_EXPIRE}")
	private Integer ITEM_CHCHE_EXPIRE;

	@Override
	public TbItem getItemById(long itemId) {
		// 查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		example.createCriteria().andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null & list.size() > 0) {
			// 添加到缓存
			try {
				jedisClient.set(ITEM_INFO + ":" + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)));
				// 设置过期时间
				jedisClient.expire(ITEM_INFO + ":" + itemId + ":BASE", ITEM_CHCHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}

	@Override
	public BaseCustom getItemList(int page, int rows) {
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		BaseCustom baseCustom = new BaseCustom();
		baseCustom.setRows(list);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		baseCustom.setTotal(pageInfo.getTotal());
		return baseCustom;
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {
		final long genItemId = IDUtils.genItemId();
		item.setId(genItemId);
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemMapper.insert(item);
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(genItemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(genItemId + "");
				return message;
			}
		});
		return E3Result.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		// 查询缓存
				try {
					String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":DESC");
					if (StringUtils.isNotBlank(json)) {
						TbItemDesc desc = JsonUtils.jsonToPojo(json,TbItemDesc.class);
						return desc;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		TbItemDescExample example = new TbItemDescExample();
		example.createCriteria().andItemIdEqualTo(itemId);
		List<TbItemDesc> list = itemDescMapper.selectByExampleWithBLOBs(example);
		if (list != null & list.size() > 0) {
			// 添加到缓存
						try {
							jedisClient.set(ITEM_INFO + ":" + itemId + ":DESC", JsonUtils.objectToJson(list.get(0)));
							// 设置过期时间
							jedisClient.expire(ITEM_INFO + ":" + itemId + ":DESC", ITEM_CHCHE_EXPIRE);
						} catch (Exception e) {
							e.printStackTrace();
						}
			return list.get(0);
		}
		return null;
	}

}
