package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 通知机构Entity
 * @author luoxuelin
 * @version 2017-07-15
 */
public class TNoticeRece extends DataEntity<TNoticeRece> {
	
	private static final long serialVersionUID = 1L;
	private String noticeId;		// 通知ID
	private String orgId;		// 机构ID
	private String orgName;	// 机构名称
	
	public TNoticeRece() {
		super();
	}

	public TNoticeRece(String id){
		super(id);
	}

	@ExcelField(title="通知ID", align=2, sort=1)
	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	
	@ExcelField(title="机构ID", align=2, sort=2)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
	
}