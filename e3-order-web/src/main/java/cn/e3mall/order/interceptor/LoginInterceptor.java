package cn.e3mall.order.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * @date 2017年11月7日上午9:44:11
 * @Description: 用户登录拦截器
 * @authorAdministrator
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Value("${SSO_URL}")
	private String SSO_URL;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 从Cookie中取token，判断是否存在 ，
		String token = CookieUtils.getCookieValue(request, "token");
		if (StringUtils.isBlank(token)) {
			// 如果不存在，跳转到sso的登录页面。登录成功后，跳转回来到当然请求的url。
			response.sendRedirect(SSO_URL + "page/login?redirect=" + request.getRequestURL());
			return false;
		}
		// 如果 token存在，在redis中取出用户信息。
		E3Result e3 = tokenService.getUserByToken(token);
		if (e3.getStatus() != 200) {
			// 如果没有取到
			response.sendRedirect(SSO_URL + "page/login?redirect=" + request.getRequestURL());
			return false;
		}
		TbUser user = (TbUser) e3.getData();
		// 如果 取到 了用户信息，把用户信息写入request
		request.setAttribute("user", user);
		// 判断cookie中是否有购物车的数据，如果有，合并到服务端、然后放行。
		String jsonCartlist = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isNotBlank(jsonCartlist)) {
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCartlist, TbItem.class));
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
	throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
