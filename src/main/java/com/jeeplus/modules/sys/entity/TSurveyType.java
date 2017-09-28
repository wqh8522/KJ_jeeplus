package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 问卷调查题库Entity
 * @author 罗学林
 * @version 2017-07-19
 */
public class TSurveyType extends DataEntity<TSurveyType> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String createOrg;		// 创建机构
	private String sfgk;	// 是否公开
	private String sfqy;	// 是否启用
	
	private String singlenum;	// 单选题数量
	private String multinum;	// 多选题数量
	
	public TSurveyType() {
		super();
	}

	public TSurveyType(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=1)
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

	public String getSinglenum() {
		return singlenum;
	}

	public void setSinglenum(String singlenum) {
		this.singlenum = singlenum;
	}

	public String getMultinum() {
		return multinum;
	}

	public void setMultinum(String multinum) {
		this.multinum = multinum;
	}

	public String getSfgk() {
		return sfgk;
	}

	public void setSfgk(String sfgk) {
		this.sfgk = sfgk;
	}

	public String getSfqy() {
		return sfqy;
	}

	public void setSfqy(String sfqy) {
		this.sfqy = sfqy;
	}
	
}