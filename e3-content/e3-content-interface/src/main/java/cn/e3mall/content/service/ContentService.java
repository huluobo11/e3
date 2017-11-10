/**
 * 
 */
package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbContent;

/**
 * @date 2017年7月12日
 * @author Administrator
 * @project e3-content-interface
 */
public interface ContentService {
E3Result addContent(TbContent content);
List<TbContent>getContentListByCid(long cid);
}
