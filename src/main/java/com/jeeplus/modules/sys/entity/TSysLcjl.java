package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 流程记录Entity
 * @author luoxuelin
 * @version 2017-08-11
 */
public class TSysLcjl extends DataEntity<TSysLcjl> {
	
	private static final long serialVersionUID = 1L;
	private String sjid;		// 数据ID
	private String clzt;		// 处理状态
	private String clyj;		// 处理意见
	private String clr;		// 处理人
	private String clrm;		// 处理人名称
	
	public TSysLcjl() {
		super();
	}

	public TSysLcjl(String id){
		super(id);
	}

	@ExcelField(title="数据ID", align=2, sort=1)
	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}
	
	@ExcelField(title="处理状态", align=2, sort=2)
	public String getClzt() {
		return clzt;
	}

	public void setClzt(String clzt) {
		this.clzt = clzt;
	}
	
	@ExcelField(title="处理意见", align=2, sort=3)
	public String getClyj() {
		return clyj;
	}

	public void setClyj(String clyj) {
		this.clyj = clyj;
	}
	
	@ExcelField(title="处理人", align=2, sort=4)
	public String getClr() {
		return clr;
	}

	public void setClr(String clr) {
		this.clr = clr;
	}
	
	@ExcelField(title="处理人名称", align=2, sort=5)
	public String getClrm() {
		return clrm;
	}

	public void setClrm(String clrm) {
		this.clrm = clrm;
	}
	
}