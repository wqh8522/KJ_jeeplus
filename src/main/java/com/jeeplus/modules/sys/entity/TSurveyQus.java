package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 问卷所属试题Entity
 * @author 罗学林
 * @version 2017-07-20
 */
public class TSurveyQus extends DataEntity<TSurveyQus> {
	
	private static final long serialVersionUID = 1L;
	private String surveyId;		// 问卷ID
	private String stid;		// 试题id
	private Integer xh;		// 序号
	
	public TSurveyQus() {
		super();
	}

	public TSurveyQus(String id){
		super(id);
	}

	@ExcelField(title="问卷ID", align=2, sort=1)
	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}
	
	@ExcelField(title="试题id", align=2, sort=2)
	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}
	
	@ExcelField(title="序号", align=2, sort=3)
	public Integer getXh() {
		return xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}
	
}