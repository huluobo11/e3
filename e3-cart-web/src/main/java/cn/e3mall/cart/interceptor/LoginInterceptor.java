package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * @date 2017年11月2日下午4:55:59
 * @Description: 判断是否登录。
 * @authorAdministrator
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Autowired
	private TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 执行handler前执行，true放行，
		// 1.判断是否已经登录
		String token = CookieUtils.getCookieValue(request, "E3-TOKEN");
		// 未登录 ，就放行。
		if (StringUtils.isBlank(token)) {
			return true;
		}
		E3Result e3 = tokenService.getUserByToken(token);
		if (e3.getStatus() != 200) {
			return true;
		} 
		// 已经登录，调用 sso系统，取出用户信息，如果没有取到，说明登录过期，放行。
		// 取到了用户信息，把用户信息放入request中。放行。
		// 到handler以后，再在request中取user，
		TbUser user = (TbUser)e3.getData();
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
	throws Exception {
		// handler执行后，返回modelandview前。

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// 执行handler后执行，返回modelandview后。
	}

}
