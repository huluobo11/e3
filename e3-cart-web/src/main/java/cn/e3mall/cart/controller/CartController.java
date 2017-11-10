package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

/**
 * @date 2017年11月2日下午3:22:16
 * @Description: 购物车操作
 * @authorAdministrator
 */
@Controller
public class CartController {
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;

	@RequestMapping("/cart/add/{itemId}")
	public String cartAdd(@PathVariable Long itemId, @RequestParam(defaultValue = "1") int num, HttpServletResponse response,
	HttpServletRequest request) {
		// 判断用户是否已经 登录
		Object user = request.getAttribute("user");
		if (user != null && user instanceof TbUser) {
			TbUser tbUser = (TbUser) user;
			// 登录了，就放在redis中、
			cartService.addCart(tbUser.getId(), itemId, num);
			return "cartSuccess";
		}
		// 从cookie中取购物车列表
		List<TbItem> itemList = getListCookie(request);
		boolean flag = false;
		for (TbItem tbItem : itemList) {
			if (tbItem.getId() == itemId.longValue()) {
				// 找到商品。数量相加。
				tbItem.setNum(num + tbItem.getNum());
				flag = true;
				break;
			}
		}
		if (!flag) {
			TbItem tbItem = itemService.getItemById(itemId);
			tbItem.setNum(num);
			String image = tbItem.getImage();
			if (StringUtils.isNoneBlank(image)) {
				String[] images = image.split(",");
				tbItem.setImage(images[0]);
			}
			// 把商品添加到购物车中。
			itemList.add(tbItem);
		}
		// 写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(itemList), COOKIE_CART_EXPIRE, true);
		return "cartSuccess";
	}

	private List<TbItem> getListCookie(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isBlank(json)) {
			return new ArrayList<>();
		} else {
			List<TbItem> jsonToList = JsonUtils.jsonToList(json, TbItem.class);
			return jsonToList;
		}
	}

	@RequestMapping("/cart/cart")
	public String cartlist(HttpServletRequest request, HttpServletResponse response) {
		List<TbItem> cartList = getListCookie(request);
		// 判断用户是否为登录状态
		// 如果是登录状态，检查cookie中是否有商品，如果 有，则把它合并到redis中
		// 之后再清空cookie。
		Object user = request.getAttribute("user");
		if (user != null && user instanceof TbUser) {
			TbUser tbUser = (TbUser) user;
			cartService.mergeCart(tbUser.getId(), cartList);
			CookieUtils.deleteCookie(request, response, "cart");
			cartList = cartService.getCartList(tbUser.getId());
		}
		// 如果没有登录，就从cookie中取出列表
		request.setAttribute("cartList", cartList);
		return "cart";
	}

	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId, @PathVariable int num, HttpServletRequest request, HttpServletResponse response) {
		// 判断用户 是否已经登录了。
		Object user = request.getAttribute("user");
		if (user != null && user instanceof TbUser) {
			// 如果 已经登录，
			TbUser tbUser = (TbUser) user;
			return cartService.updateCartNum(tbUser.getId(), itemId, num);
		}
		List<TbItem> itemList = getListCookie(request);
		for (TbItem tbItem : itemList) {
			if (tbItem.getId() == itemId.longValue()) {
				// 找到商品。数量相加。
				tbItem.setNum(num);
				break;
			}
		}
		// 写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(itemList), COOKIE_CART_EXPIRE, true);
		return E3Result.ok();
	}

	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCart(@PathVariable long itemId, HttpServletRequest request, HttpServletResponse response) {
		// 判断用户 是否已经登录了。
		Object user = request.getAttribute("user");
		if (user != null && user instanceof TbUser) {
			// 如果 已经登录，
			TbUser tbUser = (TbUser) user;
			cartService.deleteCart(tbUser.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		// 如果没有 登录
		List<TbItem> itemList = getListCookie(request);
		for (TbItem tbItem : itemList) {
			if (itemId == tbItem.getId()) {
				itemList.remove(tbItem);
				break;
			}
		}
		// 写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(itemList), COOKIE_CART_EXPIRE, true);
		return "redirect:/cart/cart.html";
	}
}
