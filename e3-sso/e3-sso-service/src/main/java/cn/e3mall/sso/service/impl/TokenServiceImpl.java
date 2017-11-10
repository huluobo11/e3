package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * @date 2017年11月2日上午9:39:02
 * @Description: TODO
 * @authorAdministrator
 */
@Service
public class TokenServiceImpl implements TokenService {
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public E3Result getUserByToken(String token) {
		String json = jedisClient.get("SESSION:" + token);
		boolean flag = StringUtils.isBlank(json);
		if (json == null || flag) {
			return E3Result.build(401, "用户登录已经过期！");
		} else {
			jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
			TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
			return E3Result.ok(user);
		}
	}
}
