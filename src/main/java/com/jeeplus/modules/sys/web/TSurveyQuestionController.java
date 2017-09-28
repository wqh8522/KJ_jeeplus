package com.jeeplus.modules.sys.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
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

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.sys.entity.TSurveyOption;
import com.jeeplus.modules.sys.entity.TSurveyQuestion;
import com.jeeplus.modules.sys.entity.TSurveyType;
import com.jeeplus.modules.sys.service.TSurveyOptionService;
import com.jeeplus.modules.sys.service.TSurveyQuestionService;
import com.jeeplus.modules.sys.service.TSurveyTypeService;
import com.jeeplus.modules.sys.utils.LogUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 问卷试题Controller
 * @author 罗学林
 * @version 2017-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/tSurveyQuestion")
public class TSurveyQuestionController extends BaseController {

	@Autowired
	private TSurveyQuestionService tSurveyQuestionService;
	@Autowired
	private TSurveyTypeService typeService;
	@Autowired
	private TSurveyOptionService optionService;
	
	@ModelAttribute
	public TSurveyQuestion get(@RequestParam(required=false) String id) {
		TSurveyQuestion entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSurveyQuestionService.get(id);
		}
		if (entity == null){
			entity = new TSurveyQuestion();
		}
		return entity;
	}
	
	/**
	 * 问卷试题列表页面
	 */
	@RequiresPermissions("sys:tSurveyType:list")
	@RequestMapping(value = {"list", ""})
	public String list(TSurveyQuestion question, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("currQusTypeId", question.getTypeId());
		CacheUtils.put("currQusTypeId", question.getTypeId());
		Page<TSurveyQuestion> page = tSurveyQuestionService.findPage(new Page<TSurveyQuestion>(request, response), question); 
		model.addAttribute("page", page);
		
		// 题库列表
		TSurveyType type = new TSurveyType();
		type.setCurrentUser(question.getCurrentUser());
		List<TSurveyType> typeList = typeService.findList(type);
		model.addAttribute("typeList", typeList);
		return "modules/sys/tSurveyQuestionList";
	}
	
	@RequiresPermissions("sys:tSurveyType:list")
	@RequestMapping(value = {"select", ""})
	public String select(TSurveyQuestion tSurveyQuestion, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TSurveyQuestion> page = tSurveyQuestionService.findPage(new Page<TSurveyQuestion>(request, response), tSurveyQuestion); 
		
		// 题库列表
		TSurveyType type = new TSurveyType();
		type.setCurrentUser(tSurveyQuestion.getCurrentUser());
		List<TSurveyType> typeList = typeService.findList(type);
		model.addAttribute("typeList", typeList);
		
		LinkedHashMap<String, TSurveyQuestion> choosed = (LinkedHashMap<String, TSurveyQuestion>) CacheUtils.get("quesSel");
		for (TSurveyQuestion qus : page.getList()) {
			if (choosed.containsKey(qus.getId())) {
				qus.setSfjr("1");
			} else {
				qus.setSfjr("0");
			}
		}
		
		model.addAttribute("page", page);
		model.addAttribute("tSurveyQuestion", tSurveyQuestion);
		return "modules/sys/surveyQuestionSelect";
	}
	
	/**
	 * 
	 * @Description: 自动组卷表单
	 * @author 罗学林
	 * @date 2017年7月23日 下午6:06:08 
	 * @param tSurveyQuestion
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:tSurveyType:list")
	@RequestMapping(value = {"autoform", ""})
	public String autoform(TSurveyQuestion tSurveyQuestion, HttpServletRequest request, HttpServletResponse response, Model model) {
		// 题库列表
		TSurveyType type = new TSurveyType();
		type.setCurrentUser(tSurveyQuestion.getCurrentUser());
		List<TSurveyType> typeList = typeService.findList(type);
		for (TSurveyType ty : typeList) {
			TSurveyType temp = typeService.findDetail(ty.getId());
			ty.setSinglenum(temp.getSinglenum());
			ty.setMultinum(temp.getMultinum());
		}
		
		model.addAttribute("typeList", typeList);
		
		return "modules/sys/surveyQuestionAutoSelect";
	}

	/**
	 * 查看，增加，编辑问卷试题表单页面
	 */
	@RequiresPermissions(value={"sys:tSurveyType:view","sys:tSurveyType:add","sys:tSurveyType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSurveyQuestion tSurveyQuestion, Model model) {
		model.addAttribute("tSurveyQuestion", tSurveyQuestion);
		
		// 题库列表
		TSurveyType type = new TSurveyType();
		type.setCurrentUser(tSurveyQuestion.getCurrentUser());
		List<TSurveyType> typeList = typeService.findList(type);
		model.addAttribute("typeList", typeList);
		
		List<TSurveyOption> options = new ArrayList<TSurveyOption>();
		if (!tSurveyQuestion.getIsNewRecord()) {
			TSurveyOption tSurveyOption = new TSurveyOption();
			tSurveyOption.setStid(tSurveyQuestion.getId());
			options = optionService.findList(tSurveyOption);
		}
		model.addAttribute("optionList", options);
		return "modules/sys/tSurveyQuestionForm";
	}

	/**
	 * 保存问卷试题
	 */
	@RequiresPermissions(value={"sys:tSurveyType:add","sys:tSurveyType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TSurveyQuestion tSurveyQuestion, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tSurveyQuestion)){
			return form(tSurveyQuestion, model);
		}
		String loginfo = "";
		if(!tSurveyQuestion.getIsNewRecord()){//编辑表单保存
			TSurveyQuestion t = tSurveyQuestionService.get(tSurveyQuestion.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tSurveyQuestion, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tSurveyQuestionService.save(t);//保存
			loginfo = "修改问卷试题["+ t.getTitle() +"]";
		}else{//新增表单保存
			loginfo = "新增问卷试题["+ tSurveyQuestion.getTitle() +"]";
			tSurveyQuestion.setCreateOrg(tSurveyQuestion.getCurrentUser().getOffice().getId());
			tSurveyQuestionService.save(tSurveyQuestion);//保存
		}
		LogUtils.saveLog(request, loginfo);
		addMessage(redirectAttributes, "保存问卷试题成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQuestion/list?typeId=" + tSurveyQuestion.getTypeId();
	}
	
	/**
	 * 删除问卷试题
	 */
	@RequiresPermissions("sys:tSurveyType:del")
	@RequestMapping(value = "delete")
	public String delete(TSurveyQuestion tSurveyQuestion, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		tSurveyQuestionService.delete(tSurveyQuestion);
		String loginfo = "删除问卷试题["+ tSurveyQuestion.getTitle() +"]";
		LogUtils.saveLog(request, loginfo);
		addMessage(redirectAttributes, "删除问卷试题成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQuestion/list?typeId=" + CacheUtils.get("currQusTypeId");
	}
	
	/**
	 * 批量删除问卷试题
	 */
	@RequiresPermissions("sys:tSurveyType:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			TSurveyQuestion tSurveyQuestion = tSurveyQuestionService.get(id);
			String loginfo = "删除问卷试题["+ tSurveyQuestion.getTitle() +"]";
			LogUtils.saveLog(request, loginfo);
			tSurveyQuestionService.delete(tSurveyQuestion);
		}
		addMessage(redirectAttributes, "删除问卷试题成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQuestion/list?typeId=" + CacheUtils.get("currQusTypeId");
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:tSurveyType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSurveyQuestion tSurveyQuestion, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷试题"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TSurveyQuestion> page = tSurveyQuestionService.findPage(new Page<TSurveyQuestion>(request, response, -1), tSurveyQuestion);
    		new ExportExcel("问卷试题", TSurveyQuestion.class).setDataList(page.getList()).write(response, fileName).dispose();
    		String loginfo = "导出问卷试题";
			LogUtils.saveLog(request, loginfo);
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出问卷试题记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQuestion/list?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:tSurveyType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			String[] alpa = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Object[]> list = new ArrayList<Object[]>();
			
			int hs = ei.getLastDataRowNum();
			int lh = ei.getLastCellNum();
			for (int i = 2; i < hs; i++) {
				Object[] objs = new Object[ei.getLastCellNum()];
				for (int j = 0; j < lh; j++) {
					objs[j] = ei.getCellValue(ei.getRow(i), j);
				}
				list.add(objs);
			}
			
			for (Object[] objs : list){
				try{
					if (objs[0] == null || objs[1] == null || objs[2] == null) {
						throw new Exception();
					} else {
						String type = objs[0].toString();
						String title = objs[1].toString();
						TSurveyQuestion qus = new TSurveyQuestion();
						qus.setType(type);
						qus.setTypeId(CacheUtils.get("currQusTypeId").toString());
						qus.setTitle(title);
						qus.setCreateBy(UserUtils.getUser());
						qus.setUpdateBy(UserUtils.getUser());
						qus.setDelFlag("0");
						qus.setCreateDate(new Date());
						qus.setUpdateDate(new Date());
						
						List<TSurveyOption> options = new ArrayList<TSurveyOption>();
						for (int i = 2; i < objs.length; i++) {
							if (objs[i] == null || objs[i].toString().equals("")) {
								break;
							} else {
								TSurveyOption option = new TSurveyOption();
								option.setBh(alpa[i - 2]);
								option.setOption(objs[i].toString());
								option.setCreateBy(UserUtils.getUser());
								option.setUpdateBy(UserUtils.getUser());
								option.setDelFlag("0");
								option.setCreateDate(new Date());
								option.setUpdateDate(new Date());
								
								options.add(option);
							}
						}
						
						qus.setOptions(options);
						tSurveyQuestionService.save(qus);
						successNum++;
					} 
					
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			String loginfo = "导入问卷试题";
			LogUtils.saveLog(request, loginfo);
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条问卷试题记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条问卷试题记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入问卷试题失败！失败信息："+e.getMessage());
			e.printStackTrace();
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQuestion/list?typeId=" + CacheUtils.get("currQusTypeId");
    }
	
	/**
	 * 下载导入问卷试题数据模板
	 */
	@RequiresPermissions("sys:tSurveyType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷试题数据导入模板.xlsx";
            String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
    		path = path.substring(path.indexOf("/") + 1);
    		path += "attachs" + File.separator + "问卷试题导入模板.xlsx";
    		fileName = URLEncoder.encode(fileName, "UTF-8");
    		File file = new File(path);
    		response.setContentLength((int) file.length());  
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);// 设置在下载框默认显示的文件名  
            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流  
            FileInputStream fileInputStream = new FileInputStream(file);  
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);  
            byte[] b = new byte[bufferedInputStream.available()];  
            bufferedInputStream.read(b);  
            OutputStream outputStream = response.getOutputStream();  
            outputStream.write(b);  
            bufferedInputStream.close();  
            outputStream.flush();  
            outputStream.close();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyQuestion/list?typeId=" + CacheUtils.get("currQusTypeId");
    }
	
	
	

}