package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

/**
 * @date 2017年11月1日下午2:29:07
 * @Description: TODO
 * @authorAdministrator
 */
@Service
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	private TbUserMapper tbUserMapper;

	@Override
	public E3Result checkData(String param, int type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		switch (type) {
		case 1:
			criteria.andUsernameEqualTo(param);
			break;
		case 2:
			criteria.andPhoneEqualTo(param);
			break;
		case 3:
			criteria.andEmailEqualTo(param);
			break;
		default:
			return E3Result.ok(false);
		}
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if (null != list && list.size() > 0) {
			return E3Result.ok(false);
		} else {
			return E3Result.ok(true);
		}
	}

	@Override
	public E3Result register(TbUser user) {
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone())) {
			return E3Result.build(400, "用户数据不完整");
		}
		E3Result e1 = checkData(user.getUsername(), 1);
		E3Result e2 = checkData(user.getPhone(), 2);
		if (!(boolean) e1.getData()) {
			return E3Result.build(400, "此用户名已经被占用");
		}
		if (!(boolean) e2.getData()) {
			return E3Result.build(400, "此手机号已经被占用");
		}
		user.setCreated(new Date());
		user.setUpdated(new Date());
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(password);
		tbUserMapper.insertSelective(user);
		return E3Result.ok();
	}

}
