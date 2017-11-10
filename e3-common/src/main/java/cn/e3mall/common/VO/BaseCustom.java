package cn.e3mall.common.VO;

import java.io.Serializable;
import java.util.List;

/**
 * @date 2017年7月3日
 * @author Administrator
 * @project e3-common
 */
public class BaseCustom implements Serializable{
private long total;
private List rows;
public long getTotal() {
	return total;
}
public void setTotal(long total) {
	this.total = total;
}
public List getRows() {
	return rows;
}
public void setRows(List rows) {
	this.rows = rows;
}


}
