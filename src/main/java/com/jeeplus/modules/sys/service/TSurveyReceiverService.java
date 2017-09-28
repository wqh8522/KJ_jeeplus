package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.entity.TSurveyReceiver;
import com.jeeplus.modules.sys.dao.TSurveyReceiverDao;

/**
 * 问卷调查人Service
 * @author 问卷调查人
 * @version 2017-07-26
 */
@Service
@Transactional(readOnly = true)
public class TSurveyReceiverService extends CrudService<TSurveyReceiverDao, TSurveyReceiver> {

	public TSurveyReceiver get(String id) {
		return super.get(id);
	}
	
	public List<TSurveyReceiver> findList(TSurveyReceiver tSurveyReceiver) {
		return super.findList(tSurveyReceiver);
	}
	
	public Page<TSurveyReceiver> findPage(Page<TSurveyReceiver> page, TSurveyReceiver tSurveyReceiver) {
		return super.findPage(page, tSurveyReceiver);
	}
	
	@Transactional(readOnly = false)
	public void save(TSurveyReceiver tSurveyReceiver) {
		super.save(tSurveyReceiver);
	}
	
	@Transactional(readOnly = false)
	public void delete(TSurveyReceiver tSurveyReceiver) {
		super.delete(tSurveyReceiver);
	}
	
	public String findUnreadNum(String surveyId) {
		return dao.findUnreadNum(surveyId);
	}
	
	
}