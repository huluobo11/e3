/**
 * 
 */
package cn.e3mall.service;

import java.util.List;

import cn.e3mall.common.VO.TreeNode;

/**
 * @date 2017年7月4日
 * @author Administrator
 * @project e3-manager-interface
 */
public interface ItemCatService {
List<TreeNode> getItemCatList(long parentId); 
}
