package com.jeeplus.modules.sys.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TTodo;

/**
 * 待办事项DAO接口
 * @author luoxuelin
 * @version 2017-07-08
 */
@MyBatisDao
public interface TTodoDao extends CrudDao<TTodo> {

	public String findUnreadCount(String userid);
	
}