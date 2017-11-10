package cn.e3mall.controller;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.standard.Media;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import cn.e3mall.common.util.FtpClient;
import cn.e3mall.common.util.JsonUtils;

/**
 * @date 2017年7月10日
 * @author Administrator
 * @project e3-manager-web
 */
@Controller
public class PictureController {
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8")
	@ResponseBody
	public String fileUpload(MultipartFile uploadFile) {
		try {
			//1、取文件的扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			//String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			//File file = new File(originalFilename);
			//uploadFile. transferTo(file);
			InputStream inputStream = uploadFile.getInputStream();
			System.out.println(inputStream);
			FtpClient FtpClient = new FtpClient();
			FtpClient.uploadFile("ftp","Lqy199685",originalFilename,inputStream);
			//4、拼接返回的url和ip地址，拼装成完整的url
			String url =IMAGE_SERVER_URL+ originalFilename;
			//5、返回map
			Map<String,Object> result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			String json=JsonUtils.objectToJson(result);
			System.out.println("------------"+json);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			//5、返回map
			Map<String,Object> result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			String json=JsonUtils.objectToJson(result);
			System.out.println("+++++++++");
			return json;
		}
	}

}
