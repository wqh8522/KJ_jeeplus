package com.jeeplus.modules.sys.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TSurvey;

/**
 * 调查问卷DAO接口
 * @author 罗学林
 * @version 2017-07-19
 */
@MyBatisDao
public interface TSurveyDao extends CrudDao<TSurvey> {

	public List<TSurvey> findUserList(TSurvey survey);
	
}