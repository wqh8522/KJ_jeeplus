package com.jeeplus.modules.sys.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TSurveyAnswer;

/**
 * 问卷答案DAO接口
 * @author 罗学林
 * @version 2017-07-20
 */
@MyBatisDao
public interface TSurveyAnswerDao extends CrudDao<TSurveyAnswer> {

	public TSurveyAnswer findAnswer(TSurveyAnswer answer);
	
	public List<TSurveyAnswer> findUserAnswer(TSurveyAnswer answer);
	
	public String findJoinCount(String wjid);
	
}