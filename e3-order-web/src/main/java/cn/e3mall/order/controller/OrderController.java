package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

/**
 * @date 2017年11月7日上午9:22:44
 * @Description: 订单管理
 * @authorAdministrator
 */
@Controller
public class OrderController {
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;

	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request) {
		Object obj = request.getAttribute("user");
		if (obj instanceof TbUser) {
			TbUser user = (TbUser) obj;
			// 取收货地址
			// 取支付方式
			// 取订单列表
			List<TbItem> cartList = cartService.getCartList(user.getId());
			request.setAttribute("cartList", cartList);
		}
		return "order-cart";
	}

	@RequestMapping(value = "/order/create", method = RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
		// 取出用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		// 把用户id添加到orderInfo中去
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		E3Result e3 = orderService.createOrder(orderInfo);
		if (e3.getStatus() == 200) {
			// 生成订单，并且删除购物车
			E3Result e3Result = cartService.clearCartItem(user.getId());
		}
		// 把订单号返回给页面
		request.setAttribute("orderId", e3.getData());
		request.setAttribute("payment", orderInfo.getPayment());
		return "success";

	}

}
