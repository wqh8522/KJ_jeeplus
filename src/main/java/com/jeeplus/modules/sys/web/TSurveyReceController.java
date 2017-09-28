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
import com.jeeplus.modules.sys.entity.TSurveyRece;
import com.jeeplus.modules.sys.service.TSurveyReceService;

/**
 * 问卷对象机构Controller
 * @author 罗学林
 * @version 2017-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/tSurveyRece")
public class TSurveyReceController extends BaseController {

	@Autowired
	private TSurveyReceService tSurveyReceService;
	
	@ModelAttribute
	public TSurveyRece get(@RequestParam(required=false) String id) {
		TSurveyRece entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSurveyReceService.get(id);
		}
		if (entity == null){
			entity = new TSurveyRece();
		}
		return entity;
	}
	
	/**
	 * 问卷对象机构列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(TSurveyRece tSurveyRece, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<TSurveyRece> list = tSurveyReceService.findList(tSurveyRece);
		String ids = "";
		String names = "";
		for (TSurveyRece rece : list) {
			ids += rece.getOrgId() + ",";
			names += rece.getOrgName() + ",";
		}
		if (ids.length() > 0) {
			ids = ids.substring(0, ids.length() - 1);
			names = names.substring(0, names.length() - 1);
		}
		tSurveyRece.setOrgIds(ids);
		tSurveyRece.setOrgNames(names);
		model.addAttribute("tSurveyRece", tSurveyRece);
		return "modules/sys/tSurveyReceForm";
	}

	/**
	 * 查看，增加，编辑问卷对象机构表单页面
	 */
	@RequiresPermissions(value={"sys:tSurveyRece:view","sys:tSurveyRece:add","sys:tSurveyRece:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSurveyRece tSurveyRece, Model model) {
		model.addAttribute("tSurveyRece", tSurveyRece);
		return "modules/sys/tSurveyReceForm";
	}

	/**
	 * 保存问卷对象机构
	 */
	@RequestMapping(value = "save")
	public String save(TSurveyRece tSurveyRece, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tSurveyRece)){
			return form(tSurveyRece, model);
		}
		if(!tSurveyRece.getIsNewRecord()){//编辑表单保存
			TSurveyRece t = tSurveyReceService.get(tSurveyRece.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tSurveyRece, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tSurveyReceService.save(t);//保存
		}else{//新增表单保存
			tSurveyReceService.save(tSurveyRece);//保存
		}
		addMessage(redirectAttributes, "保存问卷对象机构成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurvey/list?repage";
	}
	
	/**
	 * 删除问卷对象机构
	 */
	@RequiresPermissions("sys:tSurveyRece:del")
	@RequestMapping(value = "delete")
	public String delete(TSurveyRece tSurveyRece, RedirectAttributes redirectAttributes) {
		tSurveyReceService.delete(tSurveyRece);
		addMessage(redirectAttributes, "删除问卷对象机构成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyRece/?repage";
	}
	
	/**
	 * 批量删除问卷对象机构
	 */
	@RequiresPermissions("sys:tSurveyRece:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tSurveyReceService.delete(tSurveyReceService.get(id));
		}
		addMessage(redirectAttributes, "删除问卷对象机构成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyRece/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:tSurveyRece:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSurveyRece tSurveyRece, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷对象机构"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TSurveyRece> page = tSurveyReceService.findPage(new Page<TSurveyRece>(request, response, -1), tSurveyRece);
    		new ExportExcel("问卷对象机构", TSurveyRece.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出问卷对象机构记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyRece/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:tSurveyRece:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TSurveyRece> list = ei.getDataList(TSurveyRece.class);
			for (TSurveyRece tSurveyRece : list){
				try{
					tSurveyReceService.save(tSurveyRece);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条问卷对象机构记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条问卷对象机构记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入问卷对象机构失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyRece/?repage";
    }
	
	/**
	 * 下载导入问卷对象机构数据模板
	 */
	@RequiresPermissions("sys:tSurveyRece:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷对象机构数据导入模板.xlsx";
    		List<TSurveyRece> list = Lists.newArrayList(); 
    		new ExportExcel("问卷对象机构数据", TSurveyRece.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyRece/?repage";
    }
	
	
	

}