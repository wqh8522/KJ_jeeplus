package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.entity.TAttachs;
import com.jeeplus.modules.sys.dao.TAttachsDao;

/**
 * 项目附件Service
 * @author luoxuelin
 * @version 2017-07-15
 */
@Service
@Transactional(readOnly = true)
public class TAttachsService extends CrudService<TAttachsDao, TAttachs> {
	
	@Autowired
	private TAttachsDao dao;

	public TAttachs get(String id) {
		return super.get(id);
	}
	
	public List<TAttachs> findList(TAttachs tAttachs) {
		return super.findList(tAttachs);
	}
	
	public Page<TAttachs> findPage(Page<TAttachs> page, TAttachs tAttachs) {
		return super.findPage(page, tAttachs);
	}
	
	@Transactional(readOnly = false)
	public void save(TAttachs tAttachs) {
		super.save(tAttachs);
	}
	
	@Transactional(readOnly = false)
	public void delete(TAttachs tAttachs) {
		super.delete(tAttachs);
	}
	
	public List<TAttachs> findByModule(String moduleId) {
		return dao.findByModule(moduleId);
	}
	
}