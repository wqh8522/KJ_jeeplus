package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 问卷调查人Entity
 * @author 问卷调查人
 * @version 2017-07-26
 */
public class TSurveyReceiver extends DataEntity<TSurveyReceiver> {
	
	private static final long serialVersionUID = 1L;
	private String surveyId;		// 问卷id
	private String userId;		// 用户ID
	
	public TSurveyReceiver() {
		super();
	}

	public TSurveyReceiver(String id){
		super(id);
	}

	@ExcelField(title="问卷id", align=2, sort=1)
	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}
	
	@ExcelField(title="用户ID", align=2, sort=2)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}