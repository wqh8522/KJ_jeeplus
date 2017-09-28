package com.jeeplus.modules.sys.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.modules.sys.dao.TSurveyDao;
import com.jeeplus.modules.sys.entity.TSurvey;
import com.jeeplus.modules.sys.entity.TSurveyQuestion;
import com.jeeplus.modules.sys.entity.TSurveyQus;
import com.jeeplus.modules.sys.entity.TSurveyRece;
import com.jeeplus.modules.sys.entity.TSurveyReceiver;
import com.jeeplus.modules.sys.entity.User;

/**
 * 调查问卷Service
 * @author 罗学林
 * @version 2017-07-19
 */
@Service
@Transactional(readOnly = true)
public class TSurveyService extends CrudService<TSurveyDao, TSurvey> {
	
	@Autowired
	private TSurveyReceService receService;
	@Autowired
	private TSurveyQusService qusService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private TTodoService todoService;
	@Autowired
	private TMsgService msgService;
	@Autowired
	private TSurveyReceiverService receiverService;

	public TSurvey get(String id) {
		return super.get(id);
	}
	
	public List<TSurvey> findList(TSurvey tSurvey) {
		return super.findList(tSurvey);
	}
	
	public Page<TSurvey> findPage(Page<TSurvey> page, TSurvey tSurvey) {
		tSurvey.getSqlMap().put("dsf", dataScopeFilter(tSurvey.getCurrentUser(), "o", "u", true));
		return super.findPage(page, tSurvey);
	}
	
	@Transactional(readOnly = false)
	public void save(TSurvey tSurvey) {
		super.save(tSurvey);
		
		// 保存试题
		qusService.deleteBySurvey(tSurvey.getId());
		LinkedHashMap<String, TSurveyQuestion> choosed = (LinkedHashMap<String, TSurveyQuestion>) CacheUtils.get("quesSel");
		int i = 1;
		for (Map.Entry<String, TSurveyQuestion> entry : choosed.entrySet()) {  
			TSurveyQuestion ques = entry.getValue();
			TSurveyQus qus = new TSurveyQus();
			qus.setSurveyId(tSurvey.getId());
			qus.setStid(ques.getId());
			qus.setXh(i);
			
			qusService.save(qus);
			
			i ++;
		}
		
		CacheUtils.remove("quesSel");
		
		// 保存接收对象
		// 删除原有数据
		TSurveyRece tSurveyRece = new TSurveyRece();
		tSurveyRece.setSurveyId(tSurvey.getId());
		receService.delete(tSurveyRece);
		
		String[] orgids = tSurvey.getOrgIds().split(",");
		String[] orgNames = tSurvey.getOrgNames().split(",");
		for (int j = 0; j < orgids.length; j ++) {
			String orgId = orgids[j];
			TSurveyRece rece = new TSurveyRece();
			rece.setSurveyId(tSurveyRece.getSurveyId());
			rece.setOrgId(orgids[j]);
			rece.setOrgName(orgNames[j]);
			receService.save(rece);
			
			if (tSurvey.getIsPublish().equals("1")) {
				// 添加待办
				List<User> users = systemService.findUserByOfficeId(orgId);
				// 删除旧数据
				TSurveyReceiver receiver = new TSurveyReceiver();
				receiverService.delete(receiver);
				for (User user : users) {
					// 添加待办
					todoService.addTodo(user.getId(), tSurvey.getName(), "/sys/tSurvey/mylist", "待完成");
					// 添加消息
					msgService.addMsg("2", user.getId(), "您有一份调查问卷待完成！", "/sys/tSurvey/mylist");
					TSurveyReceiver receiver2 = new TSurveyReceiver();
					receiver2.setSurveyId(tSurvey.getId());
					receiver2.setUserId(user.getId());
					receiverService.save(receiver2);
				}
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TSurvey tSurvey) {
		dao.deleteByLogic(tSurvey);
	}
	
	@Transactional(readOnly = false)
	public void update(TSurvey tSurvey) {
		super.save(tSurvey);
	}
	
	/**
	 * 
	 * @Description: 获取用户问卷列表
	 * @author 罗学林
	 * @date 2017年7月20日 上午3:45:17 
	 * @param page
	 * @param tSurvey
	 * @return
	 */
	public Page<TSurvey> findUserSurveyPage(Page<TSurvey> page, TSurvey tSurvey) {
		tSurvey.setPage(page);
		page.setList(dao.findUserList(tSurvey));
		return page;
	}
	
}