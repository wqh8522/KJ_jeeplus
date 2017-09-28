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
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.sys.entity.TSurveyType;
import com.jeeplus.modules.sys.service.TSurveyTypeService;
import com.jeeplus.modules.sys.utils.LogUtils;

/**
 * 问卷调查题库Controller
 * @author 罗学林
 * @version 2017-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/tSurveyType")
public class TSurveyTypeController extends BaseController {

	@Autowired
	private TSurveyTypeService tSurveyTypeService;
	
	@ModelAttribute
	public TSurveyType get(@RequestParam(required=false) String id) {
		TSurveyType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSurveyTypeService.get(id);
		}
		if (entity == null){
			entity = new TSurveyType();
		}
		return entity;
	}
	
	/**
	 * 问卷调查题库列表页面
	 */
	@RequiresPermissions("sys:tSurveyType:list")
	@RequestMapping(value = {"list", ""})
	public String list(TSurveyType tSurveyType, HttpServletRequest request, HttpServletResponse response, Model model) {
		CacheUtils.remove("currQusTypeId");
		Page<TSurveyType> page = tSurveyTypeService.findPage(new Page<TSurveyType>(request, response), tSurveyType); 
		model.addAttribute("page", page);
		return "modules/sys/tSurveyTypeList";
	}

	/**
	 * 查看，增加，编辑问卷调查题库表单页面
	 */
	@RequiresPermissions(value={"sys:tSurveyType:view","sys:tSurveyType:add","sys:tSurveyType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSurveyType tSurveyType, Model model) {
		model.addAttribute("tSurveyType", tSurveyType);
		return "modules/sys/tSurveyTypeForm";
	}

	/**
	 * 保存问卷调查题库
	 */
	@RequiresPermissions(value={"sys:tSurveyType:add","sys:tSurveyType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TSurveyType tSurveyType, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tSurveyType)){
			return form(tSurveyType, model);
		}
		String loginfo = "";
		if(!tSurveyType.getIsNewRecord()){//编辑表单保存
			TSurveyType t = tSurveyTypeService.get(tSurveyType.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tSurveyType, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tSurveyTypeService.save(t);//保存
			loginfo = "修改题库["+ t.getName() +"]";
		}else{//新增表单保存
			loginfo = "新增题库["+ tSurveyType.getName() +"]";
			tSurveyType.setCreateOrg(tSurveyType.getCurrentUser().getOffice().getId());
			tSurveyTypeService.save(tSurveyType);//保存
		}
		
		LogUtils.saveLog(request, loginfo);
		addMessage(redirectAttributes, "保存问卷调查题库成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyType/?repage";
	}
	
	/**
	 * 删除问卷调查题库
	 */
	@RequiresPermissions("sys:tSurveyType:del")
	@RequestMapping(value = "delete")
	public String delete(TSurveyType tSurveyType, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		tSurveyTypeService.delete(tSurveyType);
		String loginfo = "删除问卷调查题库";
		LogUtils.saveLog(request, loginfo);
		addMessage(redirectAttributes, "删除问卷调查题库成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyType/?repage";
	}
	
	/**
	 * 批量删除问卷调查题库
	 */
	@RequiresPermissions("sys:tSurveyType:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			TSurveyType tSurveyType = tSurveyTypeService.get(id);
			tSurveyTypeService.delete(tSurveyType);
			String loginfo = "删除问卷调查题库";
			LogUtils.saveLog(request, loginfo);
		}
		addMessage(redirectAttributes, "删除问卷调查题库成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyType/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:tSurveyType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSurveyType tSurveyType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷调查题库"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TSurveyType> page = tSurveyTypeService.findPage(new Page<TSurveyType>(request, response, -1), tSurveyType);
    		new ExportExcel("问卷调查题库", TSurveyType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		String loginfo = "导出问卷调查题库";
			LogUtils.saveLog(request, loginfo);
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出问卷调查题库记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyType/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:tSurveyType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TSurveyType> list = ei.getDataList(TSurveyType.class);
			for (TSurveyType tSurveyType : list){
				try{
					tSurveyTypeService.save(tSurveyType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条问卷调查题库记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条问卷调查题库记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入问卷调查题库失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyType/?repage";
    }
	
	/**
	 * 下载导入问卷调查题库数据模板
	 */
	@RequiresPermissions("sys:tSurveyType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷调查题库数据导入模板.xlsx";
    		List<TSurveyType> list = Lists.newArrayList(); 
    		new ExportExcel("问卷调查题库数据", TSurveyType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyType/?repage";
    }
	
	
	

}