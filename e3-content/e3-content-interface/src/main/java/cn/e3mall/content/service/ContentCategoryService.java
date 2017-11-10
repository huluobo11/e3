package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.VO.TreeNode;
import cn.e3mall.common.util.E3Result;

/**
 * @date 2017年7月11日
 * @author Administrator
 * @project e3-content-interface
 */
public interface ContentCategoryService {
List<TreeNode> getContentCategoryList(long parentId);
E3Result addContentCategory(long parentId,String name);
}
