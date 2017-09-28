package com.jeeplus.modules.sys.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TNotice;

/**
 * 新闻通知DAO接口
 * @author luoxuelin
 * @version 2017-07-15
 */
@MyBatisDao
public interface TNoticeDao extends CrudDao<TNotice> {
	
	public List<TNotice> findUserNoticeList(TNotice notice);
	
}