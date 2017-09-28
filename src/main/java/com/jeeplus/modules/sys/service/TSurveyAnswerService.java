package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.dao.TSurveyAnswerDao;
import com.jeeplus.modules.sys.entity.TSurveyAnswer;

/**
 * 问卷答案Service
 * @author 罗学林
 * @version 2017-07-20
 */
@Service
@Transactional(readOnly = true)
public class TSurveyAnswerService extends CrudService<TSurveyAnswerDao, TSurveyAnswer> {

	public TSurveyAnswer get(String id) {
		return super.get(id);
	}
	
	public List<TSurveyAnswer> findList(TSurveyAnswer tSurveyAnswer) {
		return super.findList(tSurveyAnswer);
	}
	
	public Page<TSurveyAnswer> findPage(Page<TSurveyAnswer> page, TSurveyAnswer tSurveyAnswer) {
		return super.findPage(page, tSurveyAnswer);
	}
	
	@Transactional(readOnly = false)
	public void save(TSurveyAnswer tSurveyAnswer) {
		super.save(tSurveyAnswer);
	}
	
	@Transactional(readOnly = false)
	public void saveList(List<TSurveyAnswer> answers) {
		for (TSurveyAnswer answer : answers) {
			super.save(answer);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TSurveyAnswer tSurveyAnswer) {
		super.delete(tSurveyAnswer);
	}
	
	/**
	 * 
	 * @Description: 查询选项是否被选中
	 * @author 罗学林
	 * @date 2017年7月20日 上午4:34:08 
	 * @param answer
	 * @return
	 */
	public TSurveyAnswer findAnswer(TSurveyAnswer answer) {
		return dao.findAnswer(answer);
	}
	
	public List<TSurveyAnswer> findUserAnswer(TSurveyAnswer answer) {
		return dao.findUserAnswer(answer);
	}
	
	/**
	 * 
	 * @Description: 根据问卷ID查询问卷参与人数
	 * @author 罗学林
	 * @date 2017年7月23日 下午4:37:56 
	 * @param wjid
	 * @return
	 */
	public String findJoinCount(String wjid) {
		return dao.findJoinCount(wjid);
	}
	
}