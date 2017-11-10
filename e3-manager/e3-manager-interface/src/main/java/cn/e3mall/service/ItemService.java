package cn.e3mall.service;

import cn.e3mall.common.VO.BaseCustom;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

/**
 * @date 2017年6月29日
 * @author Administrator
 * @project e3-manager-interface
 */
public interface ItemService {
TbItem getItemById(long itemId);
BaseCustom  getItemList(int page,int rows);
E3Result addItem(TbItem item,String desc);
public TbItemDesc getItemDescById(long itemId);
}
