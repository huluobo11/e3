package cn.e3mall.sso.service;

import cn.e3mall.common.util.E3Result;

/**
 * @date 2017年11月2日上午9:37:48
 * @Description: TODO
 * @authorAdministrator
 */
public interface TokenService {
E3Result getUserByToken(String token);
}
