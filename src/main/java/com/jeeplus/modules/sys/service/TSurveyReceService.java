package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.dao.TSurveyReceDao;
import com.jeeplus.modules.sys.entity.TSurveyRece;

/**
 * 问卷对象机构Service
 * @author 罗学林
 * @version 2017-07-20
 */
@Service
@Transactional(readOnly = true)
public class TSurveyReceService extends CrudService<TSurveyReceDao, TSurveyRece> {
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private TTodoService todoService;
	@Autowired
	private TMsgService msgService;

	public TSurveyRece get(String id) {
		return super.get(id);
	}
	
	public List<TSurveyRece> findList(TSurveyRece tSurveyRece) {
		return super.findList(tSurveyRece);
	}
	
	public Page<TSurveyRece> findPage(Page<TSurveyRece> page, TSurveyRece tSurveyRece) {
		return super.findPage(page, tSurveyRece);
	}
	
	@Transactional(readOnly = false)
	public void save(TSurveyRece tSurveyRece) {
		
		// 删除原有数据
//		delete(tSurveyRece);
//		
//		String[] orgids = tSurveyRece.getOrgIds().split(",");
//		String[] orgNames = tSurveyRece.getOrgNames().split(",");
//		for (int i = 0; i < orgids.length; i ++) {
//			String orgId = orgids[i];
//			TSurveyRece rece = new TSurveyRece();
//			rece.setSurveyId(tSurveyRece.getSurveyId());
//			rece.setOrgId(orgids[i]);
//			rece.setOrgName(orgNames[i]);
//			super.save(rece);
//			
//			TSurvey survey = surveyService.get(tSurveyRece.getSurveyId());
//			survey.setIsPublish("1");
//			surveyService.update(survey);
//			
//			// 添加待办
//			List<User> users = systemService.findUserByOfficeId(orgId);
//			for (User user : users) {
//				// 添加待办
//				todoService.addTodo(user.getId(), survey.getName(), "/sys/tSurvey/mylist", "待完成");
//				// 添加消息
//				msgService.addMsg("2", user.getId(), "您有一份调查问卷待完成！", "/sys/tSurvey/mylist");
//			}
//		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TSurveyRece tSurveyRece) {
		super.delete(tSurveyRece);
	}
	
	
	
	
}