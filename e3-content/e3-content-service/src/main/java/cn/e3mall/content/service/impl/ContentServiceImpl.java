package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;





import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;

/**
 * @date 2017年7月12日
 * @author Administrator
 * @project e3-content-service
 */
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_LIST}")//从properties中读取
	private String CONTENT_LIST;//缓存 中保存的key值,

	@Override
	public E3Result addContent(TbContent content) {
		
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insertSelective(content);
		//对数据库进行写操作之后,需要同步更新缓存.
		//做缓存同步,即删除缓存中对应的数据.
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContentListByCid(long cid) {
		// 先查询缓存 ，
		try {
			// 如果缓存中有，就直接从缓存中读取数据。
			String json = jedisClient.hget(CONTENT_LIST, cid + "");
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		// 如果缓存中没有，就查找数据 库。
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(cid);
		List<TbContent> contentList = contentMapper
				.selectByExampleWithBLOBs(example);
		// 查询数据库后，要把结果添加 到缓存中。
		try {
			jedisClient.hset(CONTENT_LIST, cid + "",
					JsonUtils.objectToJson(contentList));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contentList;
	}

}
