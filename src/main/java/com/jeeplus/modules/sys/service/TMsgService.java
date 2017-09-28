package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.entity.TMsg;
import com.jeeplus.modules.sys.dao.TMsgDao;

/**
 * 消息提醒Service
 * @author 罗学林
 * @version 2017-07-19
 */
@Service
@Transactional(readOnly = true)
public class TMsgService extends CrudService<TMsgDao, TMsg> {

	public TMsg get(String id) {
		return super.get(id);
	}
	
	public List<TMsg> findList(TMsg tMsg) {
		return super.findList(tMsg);
	}
	
	public Page<TMsg> findPage(Page<TMsg> page, TMsg tMsg) {
		return super.findPage(page, tMsg);
	}
	
	@Transactional(readOnly = false)
	public void save(TMsg tMsg) {
		super.save(tMsg);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMsg tMsg) {
		super.delete(tMsg);
	}
	
	/**
	 * 
	 * @Description: 获取未读消息数量
	 * @author 罗学林
	 * @date 2017年7月23日 下午4:18:08 
	 * @return
	 */
	public String findUnreadCount(String userid) {
		return dao.findUnreadCount(userid);
	}
	
	/**
	 * 新增一条提醒消息
	 * @param msgType	消息类型	
	 * @param userid	用户ID
	 * @param content	消息内容
	 * @param redirectUrl	跳转地址
	 */
	public void addMsg(String msgType, String userid, String content, String redirectUrl) {
		TMsg msg = new TMsg();
		msg.setMsgType(msgType);
		msg.setUserId(userid);
		msg.setMsgContent(content);
		msg.setRedirectUrl(redirectUrl);
		msg.setIsRead("0");
		save(msg);
	}
}