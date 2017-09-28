package com.jeeplus.modules.sys.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sys.entity.TSysLcjl;

/**
 * 流程记录DAO接口
 * @author luoxuelin
 * @version 2017-08-11
 */
@MyBatisDao
public interface TSysLcjlDao extends CrudDao<TSysLcjl> {

	public List<TSysLcjl> getByDataId(String sjid);
	
}