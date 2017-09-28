package com.jeeplus.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.sys.entity.TTodo;
import com.jeeplus.modules.sys.service.TTodoService;

/**
 * 待办事项Controller
 * @author luoxuelin
 * @version 2017-07-08
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/todo")
public class TTodoController extends BaseController {

	@Autowired
	private TTodoService tTodoService;
	
	@ModelAttribute
	public TTodo get(@RequestParam(required=false) String id) {
		TTodo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tTodoService.get(id);
		}
		if (entity == null){
			entity = new TTodo();
		}
		return entity;
	}
	
	/**
	 * 待办事项列表页面
	 */
	@RequiresPermissions("sys:todo:list")
	@RequestMapping(value = {"list", ""})
	public String list(TTodo tTodo, HttpServletRequest request, HttpServletResponse response, Model model) {
		tTodo.setUserCode(tTodo.getCurrentUser().getNo());
		Page<TTodo> page = tTodoService.findPage(new Page<TTodo>(request, response), tTodo); 
		model.addAttribute("page", page);
		return "modules/sys/todoList";
	}

	/**
	 * 查看，增加，编辑待办事项表单页面
	 */
	@RequiresPermissions(value={"sys:todo:view","sys:todo:add","sys:todo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TTodo tTodo, Model model) {
		model.addAttribute("todo", tTodo);
		return "modules/sys/tTodoForm";
	}
	
	/**
	 * 处理待办
	 * @param todo
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"sys:todo:view","sys:todo:add","sys:todo:edit"},logical=Logical.OR)
	@RequestMapping(value = "deal")
	public String deal(TTodo todo, Model model) {
		todo.setIsRead("1");
		tTodoService.save(todo);
		return "redirect:" + Global.getAdminPath() + todo.getRedirectModule();
	}

	/**
	 * 保存待办事项
	 */
	@RequiresPermissions(value={"sys:todo:add","sys:todo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TTodo tTodo, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tTodo)){
			return form(tTodo, model);
		}
		if(!tTodo.getIsNewRecord()){//编辑表单保存
			TTodo t = tTodoService.get(tTodo.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tTodo, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tTodoService.save(t);//保存
		}else{//新增表单保存
			tTodoService.save(tTodo);//保存
		}
		addMessage(redirectAttributes, "保存待办事项成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tTodo/?repage";
	}
	
	/**
	 * 删除待办事项
	 */
	@RequiresPermissions("sys:todo:del")
	@RequestMapping(value = "delete")
	public String delete(TTodo tTodo, RedirectAttributes redirectAttributes) {
		tTodoService.delete(tTodo);
		addMessage(redirectAttributes, "删除待办事项成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tTodo/?repage";
	}
	
	/**
	 * 批量删除待办事项
	 */
	@RequiresPermissions("sys:todo:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tTodoService.delete(tTodoService.get(id));
		}
		addMessage(redirectAttributes, "删除待办事项成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tTodo/?repage";
	}
	

}