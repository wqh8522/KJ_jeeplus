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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.sys.entity.TSurveyOption;
import com.jeeplus.modules.sys.service.TSurveyOptionService;

/**
 * 问卷试题选项Controller
 * @author 罗学林
 * @version 2017-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/tSurveyOption")
public class TSurveyOptionController extends BaseController {

	@Autowired
	private TSurveyOptionService tSurveyOptionService;
	
	@ModelAttribute
	public TSurveyOption get(@RequestParam(required=false) String id) {
		TSurveyOption entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSurveyOptionService.get(id);
		}
		if (entity == null){
			entity = new TSurveyOption();
		}
		return entity;
	}
	
	/**
	 * 问卷试题选项列表页面
	 */
	@RequiresPermissions("sys:tSurveyOption:list")
	@RequestMapping(value = {"list", ""})
	public String list(TSurveyOption tSurveyOption, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TSurveyOption> page = tSurveyOptionService.findPage(new Page<TSurveyOption>(request, response), tSurveyOption); 
		model.addAttribute("page", page);
		return "modules/sys/tSurveyOptionList";
	}

	/**
	 * 查看，增加，编辑问卷试题选项表单页面
	 */
	@RequiresPermissions(value={"sys:tSurveyOption:view","sys:tSurveyOption:add","sys:tSurveyOption:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSurveyOption tSurveyOption, Model model) {
		model.addAttribute("tSurveyOption", tSurveyOption);
		return "modules/sys/tSurveyOptionForm";
	}

	/**
	 * 保存问卷试题选项
	 */
	@RequiresPermissions(value={"sys:tSurveyOption:add","sys:tSurveyOption:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TSurveyOption tSurveyOption, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tSurveyOption)){
			return form(tSurveyOption, model);
		}
		if(!tSurveyOption.getIsNewRecord()){//编辑表单保存
			TSurveyOption t = tSurveyOptionService.get(tSurveyOption.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tSurveyOption, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tSurveyOptionService.save(t);//保存
		}else{//新增表单保存
			tSurveyOptionService.save(tSurveyOption);//保存
		}
		addMessage(redirectAttributes, "保存问卷试题选项成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyOption/?repage";
	}
	
	/**
	 * 删除问卷试题选项
	 */
	@RequiresPermissions("sys:tSurveyOption:del")
	@RequestMapping(value = "delete")
	public String delete(TSurveyOption tSurveyOption, RedirectAttributes redirectAttributes) {
		tSurveyOptionService.delete(tSurveyOption);
		addMessage(redirectAttributes, "删除问卷试题选项成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyOption/?repage";
	}
	
	/**
	 * 批量删除问卷试题选项
	 */
	@RequiresPermissions("sys:tSurveyOption:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tSurveyOptionService.delete(tSurveyOptionService.get(id));
		}
		addMessage(redirectAttributes, "删除问卷试题选项成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyOption/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:tSurveyOption:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSurveyOption tSurveyOption, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷试题选项"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TSurveyOption> page = tSurveyOptionService.findPage(new Page<TSurveyOption>(request, response, -1), tSurveyOption);
    		new ExportExcel("问卷试题选项", TSurveyOption.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出问卷试题选项记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyOption/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:tSurveyOption:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TSurveyOption> list = ei.getDataList(TSurveyOption.class);
			for (TSurveyOption tSurveyOption : list){
				try{
					tSurveyOptionService.save(tSurveyOption);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条问卷试题选项记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条问卷试题选项记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入问卷试题选项失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyOption/?repage";
    }
	
	/**
	 * 下载导入问卷试题选项数据模板
	 */
	@RequiresPermissions("sys:tSurveyOption:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷试题选项数据导入模板.xlsx";
    		List<TSurveyOption> list = Lists.newArrayList(); 
    		new ExportExcel("问卷试题选项数据", TSurveyOption.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyOption/?repage";
    }
	
	
	

}