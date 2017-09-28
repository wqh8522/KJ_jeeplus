package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.entity.TSurveyQus;
import com.jeeplus.modules.sys.dao.TSurveyQusDao;

/**
 * 问卷所属试题Service
 * @author 罗学林
 * @version 2017-07-20
 */
@Service
@Transactional(readOnly = true)
public class TSurveyQusService extends CrudService<TSurveyQusDao, TSurveyQus> {

	public TSurveyQus get(String id) {
		return super.get(id);
	}
	
	public List<TSurveyQus> findList(TSurveyQus tSurveyQus) {
		return super.findList(tSurveyQus);
	}
	
	public Page<TSurveyQus> findPage(Page<TSurveyQus> page, TSurveyQus tSurveyQus) {
		return super.findPage(page, tSurveyQus);
	}
	
	@Transactional(readOnly = false)
	public void save(TSurveyQus tSurveyQus) {
		super.save(tSurveyQus);
	}
	
	@Transactional(readOnly = false)
	public void delete(TSurveyQus tSurveyQus) {
		super.delete(tSurveyQus);
	}
	
	/**
	 * 
	 * @Description: 根据问卷ID删除问卷试题
	 * @author 罗学林
	 * @date 2017年7月20日 上午2:45:17 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void deleteBySurvey(String id) {
		dao.deleteBySurvey(id);
	}
	
}