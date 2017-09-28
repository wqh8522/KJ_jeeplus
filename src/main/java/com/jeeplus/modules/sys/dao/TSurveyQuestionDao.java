package com.jeeplus.modules.sys.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TSurveyQuestion;

/**
 * 问卷试题DAO接口
 * @author 罗学林
 * @version 2017-07-19
 */
@MyBatisDao
public interface TSurveyQuestionDao extends CrudDao<TSurveyQuestion> {

	public List<TSurveyQuestion> findListBySurvey(TSurveyQuestion tSurveyQuestion);
	
	public List<TSurveyQuestion> findListByTypeAndCount(TSurveyQuestion tSurveyQuestion);
	
}