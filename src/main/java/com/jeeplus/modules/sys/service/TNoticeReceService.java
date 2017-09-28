package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.dao.TNoticeReceDao;
import com.jeeplus.modules.sys.entity.TNoticeRece;

/**
 * 通知机构Service
 * @author luoxuelin
 * @version 2017-07-15
 */
@Service
@Transactional(readOnly = true)
public class TNoticeReceService extends CrudService<TNoticeReceDao, TNoticeRece> {

	public TNoticeRece get(String id) {
		return super.get(id);
	}
	
	public List<TNoticeRece> findList(TNoticeRece tNoticeRece) {
		return super.findList(tNoticeRece);
	}
	
	public Page<TNoticeRece> findPage(Page<TNoticeRece> page, TNoticeRece tNoticeRece) {
		return super.findPage(page, tNoticeRece);
	}
	
	@Transactional(readOnly = false)
	public void save(TNoticeRece tNoticeRece) {
		super.save(tNoticeRece);
	}
	
	@Transactional(readOnly = false)
	public void delete(TNoticeRece tNoticeRece) {
		super.delete(tNoticeRece);
	}
	
	public List<TNoticeRece> findByNoticeid(String noticeid) {
		return dao.findByNoticeid(noticeid);
	}
	
	@Transactional(readOnly = false)
	public void deleteByNotice(String id) {
		dao.deleteByNoticeid(id);;
	}
	
}