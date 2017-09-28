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
import com.jeeplus.modules.sys.entity.TSurveyQus;
import com.jeeplus.modules.sys.service.TSurveyQusService;

/**
 * 问卷所属试题Controller
 * @author 罗学林
 * @version 2017-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/tSurveyQus")
public class TSurveyQusController extends BaseController {

	@Autowired
	private TSurveyQusService tSurveyQusService;
	
	@ModelAttribute
	public TSurveyQus get(@RequestParam(required=false) String id) {
		TSurveyQus entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSurveyQusService.get(id);
		}
		if (entity == null){
			entity = new TSurveyQus();
		}
		return entity;
	}
	
	/**
	 * 问卷所属试题列表页面
	 */
	@RequiresPermissions("sys:tSurveyQus:list")
	@RequestMapping(value = {"list", ""})
	public String list(TSurveyQus tSurveyQus, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TSurveyQus> page = tSurveyQusService.findPage(new Page<TSurveyQus>(request, response), tSurveyQus); 
		model.addAttribute("page", page);
		return "modules/sys/tSurveyQusList";
	}

	/**
	 * 查看，增加，编辑问卷所属试题表单页面
	 */
	@RequiresPermissions(value={"sys:tSurveyQus:view","sys:tSurveyQus:add","sys:tSurveyQus:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSurveyQus tSurveyQus, Model model) {
		model.addAttribute("tSurveyQus", tSurveyQus);
		return "modules/sys/tSurveyQusForm";
	}

	/**
	 * 保存问卷所属试题
	 */
	@RequiresPermissions(value={"sys:tSurveyQus:add","sys:tSurveyQus:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TSurveyQus tSurveyQus, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tSurveyQus)){
			return form(tSurveyQus, model);
		}
		if(!tSurveyQus.getIsNewRecord()){//编辑表单保存
			TSurveyQus t = tSurveyQusService.get(tSurveyQus.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tSurveyQus, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tSurveyQusService.save(t);//保存
		}else{//新增表单保存
			tSurveyQusService.save(tSurveyQus);//保存
		}
		addMessage(redirectAttributes, "保存问卷所属试题成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQus/?repage";
	}
	
	/**
	 * 删除问卷所属试题
	 */
	@RequiresPermissions("sys:tSurveyQus:del")
	@RequestMapping(value = "delete")
	public String delete(TSurveyQus tSurveyQus, RedirectAttributes redirectAttributes) {
		tSurveyQusService.delete(tSurveyQus);
		addMessage(redirectAttributes, "删除问卷所属试题成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQus/?repage";
	}
	
	/**
	 * 批量删除问卷所属试题
	 */
	@RequiresPermissions("sys:tSurveyQus:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tSurveyQusService.delete(tSurveyQusService.get(id));
		}
		addMessage(redirectAttributes, "删除问卷所属试题成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQus/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:tSurveyQus:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSurveyQus tSurveyQus, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷所属试题"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TSurveyQus> page = tSurveyQusService.findPage(new Page<TSurveyQus>(request, response, -1), tSurveyQus);
    		new ExportExcel("问卷所属试题", TSurveyQus.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出问卷所属试题记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQus/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:tSurveyQus:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TSurveyQus> list = ei.getDataList(TSurveyQus.class);
			for (TSurveyQus tSurveyQus : list){
				try{
					tSurveyQusService.save(tSurveyQus);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条问卷所属试题记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条问卷所属试题记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入问卷所属试题失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQus/?repage";
    }
	
	/**
	 * 下载导入问卷所属试题数据模板
	 */
	@RequiresPermissions("sys:tSurveyQus:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷所属试题数据导入模板.xlsx";
    		List<TSurveyQus> list = Lists.newArrayList(); 
    		new ExportExcel("问卷所属试题数据", TSurveyQus.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQus/?repage";
    }
	
	
	

}