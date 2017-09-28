package com.jeeplus.modules.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.sys.entity.TMsg;
import com.jeeplus.modules.sys.service.TMsgService;

/**
 * 消息提醒Controller
 * @author 罗学林
 * @version 2017-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/tMsg")
public class TMsgController extends BaseController {

	@Autowired
	private TMsgService tMsgService;
	
	@ModelAttribute
	public TMsg get(@RequestParam(required=false) String id) {
		TMsg entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMsgService.get(id);
		}
		if (entity == null){
			entity = new TMsg();
		}
		return entity;
	}
	
	/**
	 * 消息提醒列表页面
	 */
	@RequiresPermissions("sys:tMsg:list")
	@RequestMapping(value = {"list", ""})
	public String list(TMsg tMsg, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMsg> page = tMsgService.findPage(new Page<TMsg>(request, response), tMsg); 
		model.addAttribute("page", page);
		return "modules/sys/tMsgList";
	}
	
	@RequestMapping(value = "deal")
	public String deal(TMsg msg, Model model) {
		msg.setIsRead("1");
		tMsgService.save(msg);
		return "redirect:" + Global.getAdminPath() + msg.getRedirectUrl();
	}

	/**
	 * 查看，增加，编辑消息提醒表单页面
	 */
	@RequiresPermissions(value={"sys:tMsg:view","sys:tMsg:add","sys:tMsg:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TMsg tMsg, Model model) {
		model.addAttribute("tMsg", tMsg);
		return "modules/sys/tMsgForm";
	}

	/**
	 * 保存消息提醒
	 */
	@RequiresPermissions(value={"sys:tMsg:add","sys:tMsg:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TMsg tMsg, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tMsg)){
			return form(tMsg, model);
		}
		if(!tMsg.getIsNewRecord()){//编辑表单保存
			TMsg t = tMsgService.get(tMsg.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tMsg, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tMsgService.save(t);//保存
		}else{//新增表单保存
			tMsgService.save(tMsg);//保存
		}
		addMessage(redirectAttributes, "保存消息提醒成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tMsg/?repage";
	}
	
	/**
	 * 删除消息提醒
	 */
	@RequiresPermissions("sys:tMsg:del")
	@RequestMapping(value = "delete")
	public String delete(TMsg tMsg, RedirectAttributes redirectAttributes) {
		tMsgService.delete(tMsg);
		addMessage(redirectAttributes, "删除消息提醒成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tMsg/?repage";
	}
	
	/**
	 * 批量删除消息提醒
	 */
	@RequiresPermissions("sys:tMsg:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tMsgService.delete(tMsgService.get(id));
		}
		addMessage(redirectAttributes, "删除消息提醒成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tMsg/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:tMsg:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TMsg tMsg, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "消息提醒"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TMsg> page = tMsgService.findPage(new Page<TMsg>(request, response, -1), tMsg);
    		new ExportExcel("消息提醒", TMsg.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出消息提醒记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tMsg/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:tMsg:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TMsg> list = ei.getDataList(TMsg.class);
			for (TMsg tMsg : list){
				try{
					tMsgService.save(tMsg);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条消息提醒记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条消息提醒记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入消息提醒失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tMsg/?repage";
    }
	
	/**
	 * 下载导入消息提醒数据模板
	 */
	@RequiresPermissions("sys:tMsg:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "消息提醒数据导入模板.xlsx";
    		List<TMsg> list = Lists.newArrayList(); 
    		new ExportExcel("消息提醒数据", TMsg.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tMsg/?repage";
    }
	
	
	

}