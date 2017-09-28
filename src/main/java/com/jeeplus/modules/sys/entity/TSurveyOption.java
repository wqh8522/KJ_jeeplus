package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 问卷试题选项Entity
 * @author 罗学林
 * @version 2017-07-19
 */
public class TSurveyOption extends DataEntity<TSurveyOption> {
	
	private static final long serialVersionUID = 1L;
	private String stid;		// 试题id
	private String bh;		// 编号
	private String option;		// 选项
	
	private String check;
	private String rs;
	private String bl;
	
	public TSurveyOption() {
		super();
	}

	public TSurveyOption(String id){
		super(id);
	}

	@ExcelField(title="试题id", align=2, sort=7)
	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}
	
	@ExcelField(title="编号", align=2, sort=8)
	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}
	
	@ExcelField(title="选项", align=2, sort=9)
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getRs() {
		return rs;
	}

	public void setRs(String rs) {
		this.rs = rs;
	}

	public String getBl() {
		return bl;
	}

	public void setBl(String bl) {
		this.bl = bl;
	}
	
	
}