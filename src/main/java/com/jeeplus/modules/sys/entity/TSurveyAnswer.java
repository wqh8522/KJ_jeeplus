package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 问卷答案Entity
 * @author 罗学林
 * @version 2017-07-20
 */
public class TSurveyAnswer extends DataEntity<TSurveyAnswer> {
	
	private static final long serialVersionUID = 1L;
	private String wjid;		// 问卷ID
	private String stid;		// 试题ID
	private String stda;		// 试题答案
	private String userId;		// 用户ID
	
	public TSurveyAnswer() {
		super();
	}

	public TSurveyAnswer(String id){
		super(id);
	}

	@ExcelField(title="问卷ID", align=2, sort=1)
	public String getWjid() {
		return wjid;
	}

	public void setWjid(String wjid) {
		this.wjid = wjid;
	}
	
	@ExcelField(title="试题ID", align=2, sort=2)
	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}
	
	@ExcelField(title="试题答案", align=2, sort=3)
	public String getStda() {
		return stda;
	}

	public void setStda(String stda) {
		this.stda = stda;
	}
	
	@ExcelField(title="用户ID", align=2, sort=4)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}