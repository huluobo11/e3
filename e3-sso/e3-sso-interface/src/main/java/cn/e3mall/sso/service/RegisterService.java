package cn.e3mall.sso.service;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbUser;

/**
 * @date 2017年11月1日下午2:27:37
 * @Description: TODO
 * @authorAdministrator
 */
public interface RegisterService {
	E3Result checkData(String param, int type);

	/** 
	* @Description: TODO
	*  @param user
	*  @return    
	*  E3Result
	*/
	E3Result register(TbUser user);
}
