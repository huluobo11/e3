package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;

/**
 * @date 2017年11月1日下午4:35:10
 * @Description: TODO
 * @authorAdministrator
 */
@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public E3Result login(String username, String password) {
		// 1.判断用户名和密码是否正确
		TbUserExample example = new TbUserExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andUsernameEqualTo(username);
		List<TbUser> list = tbUserMapper.selectByExample(example);
		// 2.如果不正确，登录失败。
		if (list == null || list.size() < 1) {
			return E3Result.build(400, "用户名或密码错误");
		}
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
		if (!md5DigestAsHex.equals(list.get(0).getPassword())) {
			return E3Result.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		user.setPassword(null);
		// 3.如果正确，生成token，把用户信息写入redis，设置过期时间
		String token = UUID.randomUUID().toString();
		jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		return E3Result.ok(token);
	}

}
