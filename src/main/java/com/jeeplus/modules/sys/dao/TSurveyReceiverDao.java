package com.jeeplus.modules.sys.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TSurveyReceiver;

/**
 * 问卷调查人DAO接口
 * @author 问卷调查人
 * @version 2017-07-26
 */
@MyBatisDao
public interface TSurveyReceiverDao extends CrudDao<TSurveyReceiver> {

	public String findUnreadNum(String surveyId);
	
}