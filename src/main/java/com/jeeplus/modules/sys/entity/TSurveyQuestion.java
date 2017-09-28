package com.jeeplus.modules.sys.entity;


import java.util.List;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 问卷试题Entity
 * @author 罗学林
 * @version 2017-07-19
 */
public class TSurveyQuestion extends DataEntity<TSurveyQuestion> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 题目类型
	private String typeId;		// 所属题库
	private String title;		// 
	private String createOrg;		// 
	
	private String[] bhs;	// 编号
	private String[] xxs;	// 选项内容
	private String typeName;
	
	private String sfjr;
	private String surveyId;
	
	private List<TSurveyOption> options;
	
	public TSurveyQuestion() {
		super();
	}

	public TSurveyQuestion(String id){
		super(id);
	}

	@ExcelField(title="题目类型", align=2, sort=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="所属题库", align=2, sort=2)
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	@ExcelField(title="备注信息", align=2, sort=9)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="逻辑删除标记（0：显示；1：隐藏）", align=2, sort=10)
	public String getCreateOrg() {
		return createOrg;
	}

	public void setCreateOrg(String createOrg) {
		this.createOrg = createOrg;
	}

	public String[] getBhs() {
		return bhs;
	}

	public void setBhs(String[] bhs) {
		this.bhs = bhs;
	}

	public String[] getXxs() {
		return xxs;
	}

	public void setXxs(String[] xxs) {
		this.xxs = xxs;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSfjr() {
		return sfjr;
	}

	public void setSfjr(String sfjr) {
		this.sfjr = sfjr;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public List<TSurveyOption> getOptions() {
		return options;
	}

	public void setOptions(List<TSurveyOption> options) {
		this.options = options;
	}
	
	
	
}