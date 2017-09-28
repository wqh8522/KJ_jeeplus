package com.jeeplus.modules.sys.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.entity.TSysLcjl;
import com.jeeplus.modules.sys.dao.TSysLcjlDao;

/**
 * 流程记录Service
 * @author luoxuelin
 * @version 2017-08-11
 */
@Service
@Transactional(readOnly = true)
public class TSysLcjlService extends CrudService<TSysLcjlDao, TSysLcjl> {

	public TSysLcjl get(String id) {
		return super.get(id);
	}
	
	public List<TSysLcjl> findList(TSysLcjl tSysLcjl) {
		return super.findList(tSysLcjl);
	}
	
	public Page<TSysLcjl> findPage(Page<TSysLcjl> page, TSysLcjl tSysLcjl) {
		return super.findPage(page, tSysLcjl);
	}
	
	@Transactional(readOnly = false)
	public void save(TSysLcjl tSysLcjl) {
		super.save(tSysLcjl);
	}
	
	@Transactional(readOnly = false)
	public void delete(TSysLcjl tSysLcjl) {
		super.delete(tSysLcjl);
	}
	
	/**
	 * 根据dataId获取处理流程记录
	 * @author luoxuelin
	 * @date 2017年8月11日 下午4:26:26 
	 * @param sjid	数据ID
	 * @return	流程记录列表
	 */
	public List<TSysLcjl> getDataRecords(String sjid) {
		return dao.getByDataId(sjid);
	}
	
	/**
	 * 添加一条处理记录
	 * @author luoxuelin
	 * @date 2017年8月11日 下午4:30:47 
	 * @param sjid	数据ID
	 * @param clzt 处理状态
	 * @param clyj	处理意见
	 * @param clr	处理人
	 * @param clrm 处理人名
	 */
	public void addRecord(String sjid, String clzt, String clyj, String clr, String clrm) {
		TSysLcjl record = new TSysLcjl();
		record.setSjid(sjid);
		record.setClzt(clzt);
		record.setClyj(clyj);
		record.setClr(clrm);
		record.setClrm(clrm);
		record.setCreateDate(new Date());
		save(record);
	}	
	
}