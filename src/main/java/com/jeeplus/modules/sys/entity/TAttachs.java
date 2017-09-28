package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 项目附件Entity
 * @author luoxuelin
 * @version 2017-07-15
 */
public class TAttachs extends DataEntity<TAttachs> {
	
	private static final long serialVersionUID = 1L;
	private String fileName;		// 文件名
	private String moduleId;		// 模块ID
	private String filePath;		// 文件路径
	private String fileType;		// 文件格式
	
	public TAttachs() {
		super();
	}

	public TAttachs(String id){
		super(id);
	}

	@ExcelField(title="文件名", align=2, sort=1)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@ExcelField(title="模块ID", align=2, sort=2)
	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	
	@ExcelField(title="文件路径", align=2, sort=3)
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@ExcelField(title="文件格式", align=2, sort=4)
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
}