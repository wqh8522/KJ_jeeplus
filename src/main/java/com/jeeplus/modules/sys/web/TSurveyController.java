package com.jeeplus.modules.sys.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.sys.entity.TSurvey;
import com.jeeplus.modules.sys.entity.TSurveyQuestion;
import com.jeeplus.modules.sys.service.TSurveyQuestionService;
import com.jeeplus.modules.sys.service.TSurveyService;
import com.jeeplus.modules.sys.utils.LogUtils;

/**
 * 调查问卷Controller
 * @author 罗学林
 * @version 2017-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/tSurvey")
public class TSurveyController extends BaseController {

	@Autowired
	private TSurveyService tSurveyService;
	@Autowired
	private TSurveyQuestionService qusService;
	
	@ModelAttribute
	public TSurvey get(@RequestParam(required=false) String id) {
		TSurvey entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSurveyService.get(id);
		}
		if (entity == null){
			entity = new TSurvey();
		}
		return entity;
	}
	
	/**
	 * 调查问卷列表页面
	 */
	@RequiresPermissions("sys:tSurvey:list")
	@RequestMapping(value = {"list", ""})
	public String list(TSurvey tSurvey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TSurvey> page = tSurveyService.findPage(new Page<TSurvey>(request, response), tSurvey); 
		model.addAttribute("page", page);
		
		CacheUtils.remove("quesSel");
		return "modules/sys/tSurveyList";
	}

	/**
	 * 查看，增加，编辑调查问卷表单页面
	 */
	@RequiresPermissions(value={"sys:tSurvey:view","sys:tSurvey:add","sys:tSurvey:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSurvey tSurvey, Model model) {
		model.addAttribute("tSurvey", tSurvey);
		
		// 初始化试题缓存
		List<TSurveyQuestion> qusList = new ArrayList<TSurveyQuestion>();
		if (tSurvey.getId() != null) {
			qusList = qusService.findListBySurvey(tSurvey.getId());
		}
		LinkedHashMap<String, TSurveyQuestion> choosed = new LinkedHashMap<String, TSurveyQuestion>();
		for (TSurveyQuestion qus : qusList) {
			choosed.put(qus.getId(), qus);
		}
		CacheUtils.put("quesSel", choosed);
		model.addAttribute("qusList", qusList);
		return "modules/sys/tSurveyForm";
	}

	/**
	 * 保存调查问卷
	 */
	@RequiresPermissions(value={"sys:tSurvey:add","sys:tSurvey:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TSurvey tSurvey, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tSurvey)){
			return form(tSurvey, model);
		}
		String loginfo = "";
		if(!tSurvey.getIsNewRecord()){//编辑表单保存
			TSurvey t = tSurveyService.get(tSurvey.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tSurvey, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			loginfo = "修改问卷组卷["+ t.getName() + "]";
			tSurveyService.save(t);//保存
		}else{//新增表单保存
			loginfo = "新增问卷组卷["+ tSurvey.getName() + "]";
			tSurvey.setIsPublish("0");
			tSurvey.setCreateOrg(tSurvey.getCurrentUser().getOffice().getId());
			tSurveyService.save(tSurvey);//保存
		}
		
		LogUtils.saveLog(request, loginfo);
		addMessage(redirectAttributes, "保存调查问卷成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurvey/list?repage";
	}
	
	/**
	 * 删除调查问卷
	 */
	@RequiresPermissions("sys:tSurvey:del")
	@RequestMapping(value = "delete")
	public String delete(TSurvey tSurvey, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		tSurveyService.delete(tSurvey);
		String loginfo = "删除问卷组卷["+ tSurvey.getName() +"]";
		LogUtils.saveLog(request, loginfo);
		addMessage(redirectAttributes, "删除调查问卷成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurvey/list?repage";
	}
	
	/**
	 * 批量删除调查问卷
	 */
	@RequiresPermissions("sys:tSurvey:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			TSurvey tSurvey = tSurveyService.get(id);
			String loginfo = "删除问卷组卷["+ tSurvey.getName() +"]";
			LogUtils.saveLog(request, loginfo);
			tSurveyService.delete(tSurvey);
		}
		addMessage(redirectAttributes, "删除调查问卷成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurvey/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:tSurvey:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSurvey tSurvey, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "调查问卷"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TSurvey> page = tSurveyService.findPage(new Page<TSurvey>(request, response, -1), tSurvey);
    		new ExportExcel("调查问卷", TSurvey.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出调查问卷记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurvey/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:tSurvey:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TSurvey> list = ei.getDataList(TSurvey.class);
			for (TSurvey tSurvey : list){
				try{
					tSurveyService.save(tSurvey);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条调查问卷记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条调查问卷记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入调查问卷失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurvey/list?repage";
    }
	
	/**
	 * 下载导入调查问卷数据模板
	 */
	@RequiresPermissions("sys:tSurvey:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "调查问卷数据导入模板.xlsx";
    		List<TSurvey> list = Lists.newArrayList(); 
    		new ExportExcel("调查问卷数据", TSurvey.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurvey/list?repage";
    }
	
	/**
	 * 将选中试题加入缓存
	 * @author 罗学林
	 * @param qusid
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addqus")
	public String addqus(String qusid, RedirectAttributes redirectAttributes) {
		LinkedHashMap<String, TSurveyQuestion> choosed = (LinkedHashMap<String, TSurveyQuestion>) CacheUtils.get("quesSel");
		TSurveyQuestion qus = qusService.get(qusid);
		if (qus != null && qus.getTitle() != null) {
			choosed.put(qus.getId(), qus);
		}
		
		CacheUtils.put("quesSel", choosed);
		return "succ";
	}
	
	/**
	 * 
	 * <p>根据用户填写参数自动组卷</p>
	 * @author 罗学林
	 * @date 2017年7月23日 下午10:24:18 
	 * @param qusid
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "autosave")
	public String autosave(String qusid, RedirectAttributes redirectAttributes) {
		LinkedHashMap<String, TSurveyQuestion> choosed = (LinkedHashMap<String, TSurveyQuestion>) CacheUtils.get("quesSel"); // 已选题目
		
		String[] datas = qusid.split("@");
		String[] ids = datas[0].split(",");
		String[] singles = datas[1].split(",");
		String[] multis = datas[2].split(",");
		for (int i = 0; i < ids.length; i++) {
			if (!singles[i].equals("0")) { // 查询单选题
				List<TSurveyQuestion> list = qusService.findListByTypeAndCount("1", ids[i], singles[i]);
				for (TSurveyQuestion qus : list) {
					if (qus != null && qus.getTitle() != null) {
						choosed.put(qus.getId(), qus);
					}
				}
			}
			if (!multis[i].equals("0")) { // 查询多选题
				List<TSurveyQuestion> list = qusService.findListByTypeAndCount("2", ids[i], multis[i]);
				for (TSurveyQuestion qus : list) {
					if (qus != null && qus.getTitle() != null) {
						choosed.put(qus.getId(), qus);
					}
				}
			}
		}
		
		CacheUtils.put("quesSel", choosed);
		return "succ";
	}
	
	/**
	 * 将选中试题从缓存中移除
	 * @author 罗学林
	 * @param qusid
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "removequs")
	public String removequs(String qusid, RedirectAttributes redirectAttributes) {
		LinkedHashMap<String, TSurveyQuestion> choosed = (LinkedHashMap<String, TSurveyQuestion>) CacheUtils.get("quesSel");
		if (choosed.containsKey(qusid)) {
			choosed.remove(qusid);
		}
		
		CacheUtils.put("quesSel", choosed);
		return "succ";
	}
	
	/**
	 * 获取已选中试题列表
	 * @author 罗学林
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "choosed")
	public String choosed(RedirectAttributes redirectAttributes) {
		LinkedHashMap<String, TSurveyQuestion> choosed = (LinkedHashMap<String, TSurveyQuestion>) CacheUtils.get("quesSel");
		JSONArray array = new JSONArray();
		for (Map.Entry<String, TSurveyQuestion> entry : choosed.entrySet()) {  
			TSurveyQuestion qus = entry.getValue();
			JSONObject obj = new JSONObject();
			obj.put("typeName", qus.getTypeName());
			obj.put("title", qus.getTitle());
			obj.put("id", qus.getId());
			
			array.add(obj);
		}
		return array.toString();
	}
	
	@RequestMapping(value = {"mylist", ""})
	public String mylist(TSurvey tSurvey, HttpServletRequest request, HttpServletResponse response, Model model) {
		tSurvey.setCreateBy(tSurvey.getCurrentUser());
		Page<TSurvey> page = tSurveyService.findUserSurveyPage(new Page<TSurvey>(request, response), tSurvey); 
		model.addAttribute("page", page);
		return "modules/sys/mySurveyList";
	}
	

}