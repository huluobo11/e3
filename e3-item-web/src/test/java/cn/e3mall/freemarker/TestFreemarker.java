package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @date 2017年10月27日下午4:32:09
 * @Description: TODO
 * @authorAdministrator
 */
public class TestFreemarker {
	@Test
	public void ss() throws Exception{
		// 1.创建一个模板文件
		// 2.创建一个Configuration对象
		Configuration configuration=new Configuration(Configuration.getVersion());
		// 3.设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("C:/workSpace/e3-item-web/src/main/webapp/WEB-INF/"));
		// 4.设置模板文件的编码格式utf-8
		configuration.setDefaultEncoding("utf-8");
		// 5.加载一个模板文件，创建一个模板对象
		Template template = configuration.getTemplate("ff.ftl");
		// 6.创建一个数据集，pojo或者map
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("hello", "java");
		// 7.创建一个Writer对象，指定输出的文件路径以及文件名
		FileWriter fileWriter = new FileWriter(new File("E:/AA/aa.txt"));
		// 8.生成静态页面
		template.process(hashMap, fileWriter);
		// 9.关闭流。
		fileWriter.close();
	}
}
