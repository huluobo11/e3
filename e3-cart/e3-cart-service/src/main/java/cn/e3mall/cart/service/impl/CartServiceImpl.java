package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

/**
 * @date 2017年11月2日下午5:26:47
 * @Description: 购物车处理服务
 * @authorAdministrator
 */
@Service
public class CartServiceImpl implements CartService {
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private TbItemMapper tbItemMapper;

	@Override
	public E3Result addCart(long userId, long itemId, int num) {
		// 先看是否已经 存在 ，如果存在，数量增加，不存在 就添加上。
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
		if (hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(num + item.getNum());
			jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		item.setNum(num);
		String image = item.getImage();
		if (StringUtils.isNoneBlank(image)) {
			item.setImage(image.split(",")[0]);
		}
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		List<String> json = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		List<TbItem>itemList=new ArrayList<>();
		for (String string : json) {
			TbItem tbItem = JsonUtils.jsonToPojo(string, TbItem.class);
			itemList.add(tbItem);
		}
		return itemList;
	}

	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId,itemId+"");
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		item.setNum(num);
		jedisClient.hset(REDIS_CART_PRE + ":" + userId,itemId+"",JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCart(long userId, long itemId) {
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId,itemId+"");
		return E3Result.ok();
	}

	@Override
	public E3Result clearCartItem(long userId) {
		jedisClient.del(REDIS_CART_PRE + ":" + userId);
		return E3Result.ok();
	}

}
