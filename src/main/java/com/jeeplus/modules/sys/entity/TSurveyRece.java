package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 问卷对象机构Entity
 * @author 罗学林
 * @version 2017-07-20
 */
public class TSurveyRece extends DataEntity<TSurveyRece> {
	
	private static final long serialVersionUID = 1L;
	private String surveyId;		// 问卷ID
	private String orgId;		// 机构ID
	private String orgName;		// 机构名称
	
	private String orgIds;
	private String orgNames;
	
	public TSurveyRece() {
		super();
	}

	public TSurveyRece(String id){
		super(id);
	}

	@ExcelField(title="问卷ID", align=2, sort=1)
	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}
	
	@ExcelField(title="机构ID", align=2, sort=2)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="机构名称", align=2, sort=3)
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	public String getOrgNames() {
		return orgNames;
	}

	public void setOrgNames(String orgNames) {
		this.orgNames = orgNames;
	}
	
	
}