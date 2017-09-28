package com.jeeplus.modules.sys.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TSurveyType;

/**
 * 问卷调查题库DAO接口
 * @author 罗学林
 * @version 2017-07-19
 */
@MyBatisDao
public interface TSurveyTypeDao extends CrudDao<TSurveyType> {

	public TSurveyType findDetail(String id);
	
}