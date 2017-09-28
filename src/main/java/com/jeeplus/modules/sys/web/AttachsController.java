package com.jeeplus.modules.sys.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.sys.entity.TAttachs;
import com.jeeplus.modules.sys.service.TAttachsService;


/**
 * 附件Controller
 * @author 罗学林
 * @version 2017-07-17
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/attach")
public class AttachsController extends BaseController {
	
	@Autowired
	private TAttachsService attachsService;
	
	@ModelAttribute
	public TAttachs get(@RequestParam(required=false) String id) {
		TAttachs entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = attachsService.get(id);
		}
		if (entity == null){
			entity = new TAttachs();
		}
		return entity;
	}
	
	/**
	 * 根据附件ID下载附件
	 * @param response
	 * @param redirectAttributes
	 * @return
	 * @throws Exception 
	 */
    @RequestMapping(value = "download")
    public void download(TAttachs attach, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
		if (attach.getId() != null && attach.getFileName() != null) {
			String path = attach.getFilePath(); // 附件路径
			String downloadFilename = attach.getFileName();
			
			downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");
			File file = new File(path);  
			if (file.exists()) {  
	            response.setContentLength((int) file.length());  
	            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);// 设置在下载框默认显示的文件名  
	            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流  
	            FileInputStream fileInputStream = new FileInputStream(file);  
	            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);  
	            byte[] b = new byte[bufferedInputStream.available()];  
	            bufferedInputStream.read(b);  
	            OutputStream outputStream = response.getOutputStream();  
	            outputStream.write(b);  
	            bufferedInputStream.close();  
	            outputStream.flush();  
	            outputStream.close();  
	        } else {
	        	response.setContentType("text/html");
	    		PrintWriter out = response.getWriter();
	    		out.println("文件不存在!");
	    		out.flush();
	    		out.close();
			}
		} else {
			response.setContentType("text/html");
    		PrintWriter out = response.getWriter();
    		out.println("文件不存在!");
    		out.flush();
    		out.close();
		}
    }
    
    /**
	 * 删除附件
	 */
    @ResponseBody
	@RequestMapping(value = "delete")
	public String delete(TAttachs attach, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		attachsService.delete(attach);
		return "succ";
	}
	
}
