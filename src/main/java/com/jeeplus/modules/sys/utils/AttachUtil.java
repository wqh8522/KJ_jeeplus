package com.jeeplus.modules.sys.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.jeeplus.common.utils.UUIDUtil;
import com.jeeplus.modules.sys.entity.TAttachs;

/**
 * 附件保存工具类
 * @author 罗学林
 *
 */
public class AttachUtil {
	
	/**
	 * 附件保存工具类
	 * @author 罗学林
	 * @param file
	 * @return 返回附件表对象
	 * @throws Exception
	 */
	public static TAttachs save(MultipartFile file) throws Exception {
		TAttachs attach = new TAttachs();
		
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		path = path.substring(path.indexOf("/") + 1);
		path += "attachs" + File.separator;
		
		File outFolder = new File(path);
		if(!outFolder.exists()){
			outFolder.mkdirs();
		}
		String fileName = file.getOriginalFilename();
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		String savePath =  path + UUIDUtil.randomUUID() + "." + ext;
		
		byte[] bs = new byte[1024];
        int len;

        InputStream inputStream = file.getInputStream();
        OutputStream os = null;
        File tempFile = new File(path);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        os = new FileOutputStream(savePath);
        // 开始读取
        while ((len = inputStream.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        
        attach.setFileType(ext);
        attach.setFileName(fileName);
        attach.setFilePath(savePath);
        return attach;
	}
	
}
