package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.dao.TSurveyQuestionDao;
import com.jeeplus.modules.sys.entity.TSurveyOption;
import com.jeeplus.modules.sys.entity.TSurveyQuestion;

/**
 * 问卷试题Service
 * @author 罗学林
 * @version 2017-07-19
 */
@Service
@Transactional(readOnly = true)
public class TSurveyQuestionService extends CrudService<TSurveyQuestionDao, TSurveyQuestion> {
	
	@Autowired
	private TSurveyOptionService optionService;
	
	public TSurveyQuestion get(String id) {
		return super.get(id);
	}
	
	public List<TSurveyQuestion> findList(TSurveyQuestion tSurveyQuestion) {
		return super.findList(tSurveyQuestion);
	}
	
	public Page<TSurveyQuestion> findPage(Page<TSurveyQuestion> page, TSurveyQuestion tSurveyQuestion) {
		tSurveyQuestion.getSqlMap().put("dsf", dataScopeFilter(tSurveyQuestion.getCurrentUser(), "o", "u", true));
		return super.findPage(page, tSurveyQuestion);
	}
	
	@Transactional(readOnly = false)
	public void save(TSurveyQuestion tSurveyQuestion) {
		super.save(tSurveyQuestion);
		
		if (tSurveyQuestion.getOptions().size() > 0) {
			for (TSurveyOption option : tSurveyQuestion.getOptions()) {
				option.setStid(tSurveyQuestion.getId());
				optionService.save(option);
			}
		} else {
			String[] bhs = tSurveyQuestion.getBhs();
			String[] xxs = tSurveyQuestion.getXxs();
			if (!tSurveyQuestion.getIsNewRecord()) {
				optionService.deleteByStid(tSurveyQuestion.getId());
			}
			for (int i = 0; i < bhs.length; i++) {
				TSurveyOption option = new TSurveyOption();
				option.setBh(bhs[i]);
				option.setOption(xxs[i]);
				option.setCreateBy(tSurveyQuestion.getCreateBy());
				option.setCreateDate(tSurveyQuestion.getCreateDate());
				option.setDelFlag("0");
				option.setStid(tSurveyQuestion.getId());
				option.setUpdateBy(tSurveyQuestion.getUpdateBy());
				option.setUpdateDate(tSurveyQuestion.getUpdateDate());
				
				optionService.save(option);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TSurveyQuestion tSurveyQuestion) {
		dao.deleteByLogic(tSurveyQuestion);
	}
	
	/**
	 * 根据问卷ID查询所有试题
	 * @author 罗学林
	 * @param surveyId
	 * @return
	 */
	public List<TSurveyQuestion> findListBySurvey(String surveyId) {
		TSurveyQuestion tSurveyQuestion = new TSurveyQuestion();
		tSurveyQuestion.setSurveyId(surveyId);
		return dao.findListBySurvey(tSurveyQuestion);
	}
	
	/**
	 * 根据题库ID、题目类型、题目数量获取题目列表
	 * @author 罗学林
	 * @date 2017年7月23日 下午10:16:44 
	 * @param type
	 * @param typeId
	 * @param count
	 * @return
	 */
	public List<TSurveyQuestion> findListByTypeAndCount(String type, String typeId, String count) {
		TSurveyQuestion qus = new TSurveyQuestion();
		qus.setType(type);
		qus.setTypeId(typeId);
		List<TSurveyQuestion> list = dao.findListByTypeAndCount(qus);
		if (list.size() >= Integer.parseInt(count)) {
			return list.subList(0, Integer.parseInt(count));
		} else {
			return list;
		}
	} 
}