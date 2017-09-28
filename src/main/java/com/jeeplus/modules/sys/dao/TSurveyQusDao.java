package com.jeeplus.modules.sys.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TSurveyQus;

/**
 * 问卷所属试题DAO接口
 * @author 罗学林
 * @version 2017-07-20
 */
@MyBatisDao
public interface TSurveyQusDao extends CrudDao<TSurveyQus> {

	public void deleteBySurvey(String surveyId);
	
}