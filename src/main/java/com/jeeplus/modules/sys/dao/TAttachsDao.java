package com.jeeplus.modules.sys.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TAttachs;

/**
 * 项目附件DAO接口
 * @author luoxuelin
 * @version 2017-07-15
 */
@MyBatisDao
public interface TAttachsDao extends CrudDao<TAttachs> {
	
	public List<TAttachs> findByModule(String moduleId);
	
}