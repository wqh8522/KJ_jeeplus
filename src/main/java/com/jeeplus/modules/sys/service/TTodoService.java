package com.jeeplus.modules.sys.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.dao.TTodoDao;
import com.jeeplus.modules.sys.entity.TTodo;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 待办事项Service
 * @author luoxuelin
 * @version 2017-07-08
 */
@Service
@Transactional(readOnly = true)
public class TTodoService extends CrudService<TTodoDao, TTodo> {

	public TTodo get(String id) {
		return super.get(id);
	}
	
	public List<TTodo> findList(TTodo tTodo) {
		return super.findList(tTodo);
	}
	
	public Page<TTodo> findPage(Page<TTodo> page, TTodo todo) {
		return super.findPage(page, todo);
	}
	
	@Transactional(readOnly = false)
	public void save(TTodo tTodo) {
		super.save(tTodo);
	}
	
	@Transactional(readOnly = false)
	public void delete(TTodo tTodo) {
		super.delete(tTodo);
	}
	
	/**
	 * 
	 * @Description: 获取未读待办数量
	 * @author 罗学林
	 * @date 2017年7月23日 下午4:18:08 
	 * @return
	 */
	public String findUnreadCount(String userid) {
		return dao.findUnreadCount(userid);
	}
	
	/**
	 * 添加待办事项
	 * @param userid	接收用户ID
	 * @param title		待办名称
	 * @param redirectUrl	跳转地址
	 * @param currStatus	当前状态
	 */
	public void addTodo(String userid, String title, String redirectUrl, String currStatus) {
		User user = UserUtils.getUser();
		TTodo todo = new TTodo();
		todo.setUserCode(userid);
		todo.setTitle(title);
		todo.setRedirectModule(redirectUrl);
		todo.setCurrStatus(currStatus);
		todo.setIsRead("0");
		todo.setCreateBy(user);
		todo.setCreateDate(new Date());
		todo.setDelFlag("0");
		todo.setUpdateBy(user);
		save(todo);
	}
	
	/**
	 * 保存待办处理状态
	 * @author luoxuelin
	 * @date 2017年8月11日 上午11:23:58 
	 * @param dataId	处理数据ID
	 * @param status	当前状态
	 */
	public void saveTodoStatus(String dataId, String status) {
		TTodo todo = dao.findUniqueByProperty("dataId", dataId);
		todo.setCurrStatus(status);
		save(todo);
	}
	
	
}