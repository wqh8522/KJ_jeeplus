package com.jeeplus.modules.iim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.iim.dao.MyCalendarDao;
import com.jeeplus.modules.iim.entity.MyCalendar;
import com.jeeplus.modules.sys.utils.UserUtils;


/**
 * 日历Service
 * 
 * @version 2016-04-19
 */
@Service
@Transactional(readOnly = true)
public class MyCalendarService extends CrudService<MyCalendarDao, MyCalendar> {

	public MyCalendar get(String id) {
		return super.get(id);
	}
	
	public List<MyCalendar> findList(MyCalendar myCalendar) {
		return super.findList(myCalendar);
	}
	
	public Page<MyCalendar> findPage(Page<MyCalendar> page, MyCalendar myCalendar) {
		return super.findPage(page, myCalendar);
	}
	
	@Transactional(readOnly = false)
	public void save(MyCalendar myCalendar) {
		super.save(myCalendar);
	}
	
	@Transactional(readOnly = false)
	public void delete(MyCalendar myCalendar) {
		super.delete(myCalendar);
	}
	
	/**
	 * 添加日程
	 * @param title		标题
	 * @param start		开始时间
	 * @param end		结束时间
	 * @param isallday	是否全天
	 * @param userid	用户ID
	 */
	public void addCalendar(String title, String start, String end, String isallday, String userid) {
		String[] colors = { "#360", "#f30", "#06c" };
		int index = (int) (Math.random() * colors.length);
		MyCalendar myCalendar = new MyCalendar();
		myCalendar.setTitle(title);
		myCalendar.setStart(start);
		myCalendar.setEnd(end);
		myCalendar.setAdllDay(isallday);
		myCalendar.setColor(colors[index]);
		myCalendar.setUser(UserUtils.get(userid));
		save(myCalendar);
	}
	
}