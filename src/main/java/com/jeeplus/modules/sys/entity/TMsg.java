package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 消息提醒Entity
 * @author 罗学林
 * @version 2017-07-19
 */
public class TMsg extends DataEntity<TMsg> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户ID
	private String msgType;		// 消息类型
	private String msgContent;		// 消息内容
	private String redirectUrl;		// 跳转地址
	private String isRead;		// 是否阅读
	
	public TMsg() {
		super();
	}

	public TMsg(String id){
		super(id);
	}

	@ExcelField(title="用户ID", align=2, sort=1)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@ExcelField(title="消息类型", align=2, sort=2)
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@ExcelField(title="消息内容", align=2, sort=3)
	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	
	@ExcelField(title="跳转地址", align=2, sort=4)
	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	@ExcelField(title="是否阅读", align=2, sort=5)
	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	
}