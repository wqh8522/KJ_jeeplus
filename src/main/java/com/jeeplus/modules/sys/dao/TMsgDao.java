package com.jeeplus.modules.sys.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TMsg;

/**
 * 消息提醒DAO接口
 * @author 罗学林
 * @version 2017-07-19
 */
@MyBatisDao
public interface TMsgDao extends CrudDao<TMsg> {

	public String findUnreadCount(String userid);
	
}