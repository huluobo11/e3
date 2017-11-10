package cn.e3mall.order.pojo;

import java.io.Serializable;
import java.util.List;

import cn.e3mall.pojo.TbOrder;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

/**
 * @date 2017年11月7日上午10:55:52
 * @Description: TODO
 * @authorAdministrator
 */
public class OrderInfo extends TbOrder implements Serializable {
private List<TbOrderItem> orderItems;
private TbOrderShipping tbOrderShipping;
public List<TbOrderItem> getOrderItems() {
	return orderItems;
}
public void setOrderItems(List<TbOrderItem> orderItems) {
	this.orderItems = orderItems;
}
public TbOrderShipping getTbOrderShipping() {
	return tbOrderShipping;
}
public void setTbOrderShipping(TbOrderShipping tbOrderShipping) {
	this.tbOrderShipping = tbOrderShipping;
}

}
