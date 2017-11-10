package cn.e3mall.common.VO;

import java.io.Serializable;

/**
 * @date 2017年7月13日
 * @author Administrator
 * @project e3-common
 */
public class SearchItem implements Serializable{
	private static final long serialVersionUID = 1L;
private String id;
private String title;
private String sell_point;
private long price;
private String image;
private String  catagory_name;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getSell_point() {
	return sell_point;
}
public void setSell_point(String sell_point) {
	this.sell_point = sell_point;
}
public long getPrice() {
	return price;
}
public void setPrice(long price) {
	this.price = price;
}
public String getImage() {
	return image;
}
public void setImage(String image) {
	this.image = image;
}
public String getCatagory_name() {
	return catagory_name;
}
public void setCatagory_name(String catagory_name) {
	this.catagory_name = catagory_name;
}
public String[] getImages(){
	if(null!=image && !"".equals(image)){
		return image.split(",");
	}
	return null;
}
}
