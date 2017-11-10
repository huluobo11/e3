package cn.e3mall.sso.service;

import cn.e3mall.common.util.E3Result;

/**
 * @date 2017年11月1日下午4:30:55
 * @Description: TODO
 * @authorAdministrator
 */
public interface LoginService {
	E3Result login(String username, String password);
}
