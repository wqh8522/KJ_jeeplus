package com.jeeplus.modules.sys.entity;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 调查问卷Entity
 * @author 罗学林
 * @version 2017-07-19
 */
public class TSurvey extends DataEntity<TSurvey> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String createOrg;		// 创建机构
	private String isPublish;		// 是否发布
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	
	private List<TSurveyQuestion> qusList;
	private String isFinish;
	private String zrs;
	
	private String orgIds;
	private String orgNames;
	
	public TSurvey() {
		super();
	}

	public TSurvey(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="创建机构", align=2, sort=8)
	public String getCreateOrg() {
		return createOrg;
	}

	public void setCreateOrg(String createOrg) {
		this.createOrg = createOrg;
	}
	
	@ExcelField(title="是否发布", align=2, sort=9)
	public String getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开始时间", align=2, sort=10)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=11)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<TSurveyQuestion> getQusList() {
		return qusList;
	}

	public void setQusList(List<TSurveyQuestion> qusList) {
		this.qusList = qusList;
	}

	public String getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}

	public String getZrs() {
		return zrs;
	}

	public void setZrs(String zrs) {
		this.zrs = zrs;
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