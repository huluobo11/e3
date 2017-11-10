package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

/**
 * @date 2017年11月7日上午11:21:01
 * @Description: TODO
 * @authorAdministrator
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;

	@Value("${ORDER_DETAIL_ID_GEN}")
	private String ORDER_DETAIL_ID_GEN;
	@Override
	public E3Result createOrder(OrderInfo orderInfo) {
		// 用redis，生成 订单号
		if (!jedisClient.exists(ORDER_ID_GEN_KEY)) {
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId=String.valueOf(jedisClient.incr(ORDER_ID_GEN_KEY));
		// 补全OrderInfo的属性
		orderInfo.setStatus(1);//1表示 未付款。
		orderInfo.setOrderId(orderId); 
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		// 插入订单表
		tbOrderMapper.insert(orderInfo);
		// 插入订单明细表
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			String order_detail_id = jedisClient.incr(ORDER_DETAIL_ID_GEN).toString();
			tbOrderItem.setId(order_detail_id);
			tbOrderItem.setOrderId(orderId);
			tbOrderItemMapper.insert(tbOrderItem);
		}
		TbOrderShipping tbOrderShipping = orderInfo.getTbOrderShipping();
		tbOrderShipping.setOrderId(orderId);
		tbOrderShipping.setCreated(new Date());
		tbOrderShipping.setUpdated(new Date());
		// 插入订单物流表。
		tbOrderShippingMapper.insert(tbOrderShipping);
		//清空购物车
		// 返回订单号
		return E3Result.ok(orderId );
	}

}
