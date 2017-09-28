package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sys.entity.TAttachs;
import com.jeeplus.modules.sys.entity.TMsg;
import com.jeeplus.modules.sys.entity.TNotice;
import com.jeeplus.modules.sys.entity.TNoticeRece;
import com.jeeplus.modules.sys.entity.TTodo;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.dao.TNoticeDao;

/**
 * 新闻通知Service
 * @author luoxuelin
 * @version 2017-07-15
 */
@Service
@Transactional(readOnly = true)
public class TNoticeService extends CrudService<TNoticeDao, TNotice> {
	
	@Autowired
	private TAttachsService attachsService;
	@Autowired
	private TNoticeReceService receService;
	@Autowired
	private TTodoService todoService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private TMsgService msgService;

	public TNotice get(String id) {
		return super.get(id);
	}
	
	public List<TNotice> findList(TNotice tNotice) {
		return super.findList(tNotice);
	}
	
	public Page<TNotice> findPage(Page<TNotice> page, TNotice tNotice) {
		tNotice.getSqlMap().put("dsf", dataScopeFilter(tNotice.getCurrentUser(), "o", "u", true));
		return super.findPage(page, tNotice);
	}
	
	public Page<TNotice> findAllPage(Page<TNotice> page, TNotice tNotice) {
		
		return super.findPage(page, tNotice);
	}
	
	@Transactional(readOnly = false)
	public void save(TNotice notice, List<TAttachs> attachs) {
		super.save(notice);
		
		// 保存附件信息
		for (TAttachs attach : attachs) {
			attach.setModuleId(notice.getId());
			attachsService.save(attach);
		}
		
		// 保存接收单位信息
		if (notice.getType().equals("1")) {
			// 清空原数据
			receService.deleteByNotice(notice.getId());;
			
			String[] orgids = notice.getReceIds().split(",");
			String[] orgNames = notice.getReceNames().split(",");
			for (int i = 0; i < orgids.length; i ++) {
				String orgId = orgids[i];
				TNoticeRece rece = new TNoticeRece();
				rece.setOrgId(orgId);
				rece.setNoticeId(notice.getId());
				rece.setOrgName(orgNames[i]);
				receService.save(rece);
				
				// 添加待办
				if (notice.getIsPublish().equals("1")) {
					List<User> users = systemService.findUserByOfficeId(orgId);
					for (User user : users) {
						TTodo todo = new TTodo();
						todo.setUserCode(user.getId());
						todo.setTitle(notice.getTitle());
						todo.setRedirectModule("/sys/notice/mylist");
						todo.setCurrStatus("待阅读");
						todo.setIsRead("0");
						todo.setDataId(notice.getId());
						todo.setCreateBy(notice.getCreateBy());
						todo.setCreateDate(notice.getCreateDate());
						todo.setDelFlag(notice.getDelFlag());
						todo.setUpdateBy(notice.getUpdateBy());
						
						todoService.save(todo);
						
						TMsg msg = new TMsg();
						msg.setMsgType("1");
						msg.setUserId(user.getId());
						msg.setMsgContent("您有一条新通知需要您查阅！");
						msg.setRedirectUrl("/sys/notice/mylist");
						msg.setIsRead("0");
						msgService.save(msg);
					}
				}
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TNotice tNotice) {
		super.delete(tNotice);
	}
	
	public Page<TNotice> findUserNoticePage(Page<TNotice> page, TNotice tNotice) {
		tNotice.setPage(page);
		page.setList(dao.findUserNoticeList(tNotice));
		return page;
	} 
	
}