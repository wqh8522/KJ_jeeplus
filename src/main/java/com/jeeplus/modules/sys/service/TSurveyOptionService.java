package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.entity.TSurveyOption;
import com.jeeplus.modules.sys.dao.TSurveyOptionDao;

/**
 * 问卷试题选项Service
 * @author 罗学林
 * @version 2017-07-19
 */
@Service
@Transactional(readOnly = true)
public class TSurveyOptionService extends CrudService<TSurveyOptionDao, TSurveyOption> {

	public TSurveyOption get(String id) {
		return super.get(id);
	}
	
	public List<TSurveyOption> findList(TSurveyOption tSurveyOption) {
		return super.findList(tSurveyOption);
	}
	
	public Page<TSurveyOption> findPage(Page<TSurveyOption> page, TSurveyOption tSurveyOption) {
		return super.findPage(page, tSurveyOption);
	}
	
	@Transactional(readOnly = false)
	public void save(TSurveyOption tSurveyOption) {
		super.save(tSurveyOption);
	}
	
	@Transactional(readOnly = false)
	public void delete(TSurveyOption tSurveyOption) {
		super.delete(tSurveyOption);
	}
	
	@Transactional(readOnly = false)
	public void deleteByStid(String id) {
		dao.deleteByStid(id);
	}
	
	
}