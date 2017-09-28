package com.jeeplus.modules.iim.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.iim.entity.MailCompose;

/**
 * 发件箱DAO接口
 * 
 * @version 2015-11-15
 */
@MyBatisDao
public interface MailComposeDao extends CrudDao<MailCompose> {
	public int getCount(MailCompose entity);
	
	public List<String> getDetail(MailCompose entity);
	
	public List<String> getDetailId(MailCompose entity);


}