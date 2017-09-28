package com.jeeplus.modules.sys.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TSurveyOption;

/**
 * 问卷试题选项DAO接口
 * @author 罗学林
 * @version 2017-07-19
 */
@MyBatisDao
public interface TSurveyOptionDao extends CrudDao<TSurveyOption> {

	public void deleteByStid(String id);
	
}