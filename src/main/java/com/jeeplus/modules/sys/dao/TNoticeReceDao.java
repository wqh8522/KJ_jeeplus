package com.jeeplus.modules.sys.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TNoticeRece;

/**
 * 通知机构DAO接口
 * @author luoxuelin
 * @version 2017-07-15
 */
@MyBatisDao
public interface TNoticeReceDao extends CrudDao<TNoticeRece> {

	public List<TNoticeRece> findByNoticeid(String id);
	
	public void deleteByNoticeid(String id);
	
}