/**
 * 
 */
package cn.e3mall.common.VO;

import java.io.Serializable;

/**
 * @date 2017年7月4日
 * @author Administrator
 * @project e3-common
 */
public class TreeNode implements Serializable{
	private static final long serialVersionUID = 1L;
	private long id;
	private String  text;
	private String state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}