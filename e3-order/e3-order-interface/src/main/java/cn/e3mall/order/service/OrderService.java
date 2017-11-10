package cn.e3mall.order.service;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.order.pojo.OrderInfo;

/**
 * @date 2017年11月7日上午11:13:29
 * @Description: TODO
 * @authorAdministrator
 */
public interface OrderService {
	E3Result createOrder(OrderInfo orderInfo);
}
