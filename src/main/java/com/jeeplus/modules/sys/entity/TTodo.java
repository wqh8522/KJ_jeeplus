package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 待办事项Entity
 * @author luoxuelin
 * @version 2017-07-08
 */
public class TTodo extends DataEntity<TTodo> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String userCode;		// 用户编号（工号/学号）
	private String redirectModule;		// 重定向功能（字典项，参照数据字典）
	private String currStatus;		// 当前状态
	private String isRead;		// 是否阅读（0：否 1：是  ）
	private String dataId;		// 数据ID
	
	public TTodo() {
		super();
	}

	public TTodo(String id){
		super(id);
	}

	@ExcelField(title="标题", align=2, sort=1)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="用户编号（工号/学号）", align=2, sort=2)
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	@ExcelField(title="重定向功能（字典项，参照数据字典）", align=2, sort=3)
	public String getRedirectModule() {
		return redirectModule;
	}

	public void setRedirectModule(String redirectModule) {
		this.redirectModule = redirectModule;
	}
	
	@ExcelField(title="当前状态", align=2, sort=4)
	public String getCurrStatus() {
		return currStatus;
	}

	public void setCurrStatus(String currStatus) {
		this.currStatus = currStatus;
	}
	
	@ExcelField(title="是否阅读（0：否 1：是  ）", align=2, sort=5)
	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
	
	
}