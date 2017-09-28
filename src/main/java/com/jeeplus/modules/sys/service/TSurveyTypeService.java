package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.entity.TSurveyType;
import com.jeeplus.modules.sys.dao.TSurveyTypeDao;

/**
 * 问卷调查题库Service
 * @author 罗学林
 * @version 2017-07-19
 */
@Service
@Transactional(readOnly = true)
public class TSurveyTypeService extends CrudService<TSurveyTypeDao, TSurveyType> {

	public TSurveyType get(String id) {
		return super.get(id);
	}
	
	public List<TSurveyType> findList(TSurveyType tSurveyType) {
		tSurveyType.getSqlMap().put("dsf", dataScopeFilter(tSurveyType.getCurrentUser(), "o", "u", true));
		return super.findList(tSurveyType);
	}
	
	public Page<TSurveyType> findPage(Page<TSurveyType> page, TSurveyType tSurveyType) {
		tSurveyType.getSqlMap().put("dsf", dataScopeFilter(tSurveyType.getCurrentUser(), "o", "u", true));
		return super.findPage(page, tSurveyType);
	}
	
	@Transactional(readOnly = false)
	public void save(TSurveyType tSurveyType) {
		super.save(tSurveyType);
	}
	
	@Transactional(readOnly = false)
	public void delete(TSurveyType tSurveyType) {
		super.delete(tSurveyType);
	}
	
	
	public TSurveyType findDetail(String id) {
		return dao.findDetail(id);
	}
	
}