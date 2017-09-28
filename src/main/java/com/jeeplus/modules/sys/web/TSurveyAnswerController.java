package com.jeeplus.modules.sys.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.jeeplus.modules.sys.entity.TSurvey;
import com.jeeplus.modules.sys.entity.TSurveyAnswer;
import com.jeeplus.modules.sys.entity.TSurveyOption;
import com.jeeplus.modules.sys.entity.TSurveyQuestion;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.service.TSurveyAnswerService;
import com.jeeplus.modules.sys.service.TSurveyOptionService;
import com.jeeplus.modules.sys.service.TSurveyQuestionService;
import com.jeeplus.modules.sys.service.TSurveyReceiverService;
import com.jeeplus.modules.sys.service.TSurveyService;
import com.jeeplus.modules.sys.utils.LogUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 问卷答案Controller
 * @author 罗学林
 * @version 2017-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/tSurveyAnswer")
public class TSurveyAnswerController extends BaseController {

	@Autowired
	private TSurveyAnswerService tSurveyAnswerService;
	@Autowired
	private TSurveyService surveyService;
	@Autowired
	private TSurveyQuestionService quesService;
	@Autowired
	private TSurveyOptionService optionService;
	@Autowired
	private TSurveyReceiverService receiverService;
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public TSurveyAnswer get(@RequestParam(required=false) String id) {
		TSurveyAnswer entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tSurveyAnswerService.get(id);
		}
		if (entity == null){
			entity = new TSurveyAnswer();
		}
		return entity;
	}
	
	/**
	 * 问卷答案列表页面
	 */
	@RequiresPermissions("sys:tSurveyAnswer:list")
	@RequestMapping(value = {"list", ""})
	public String list(TSurveyAnswer tSurveyAnswer, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TSurveyAnswer> page = tSurveyAnswerService.findPage(new Page<TSurveyAnswer>(request, response), tSurveyAnswer); 
		model.addAttribute("page", page);
		return "modules/sys/tSurveyAnswerList";
	}

	/**
	 * 查看，增加，编辑问卷答案表单页面
	 */
	@RequiresPermissions(value={"sys:tSurveyAnswer:view","sys:tSurveyAnswer:add","sys:tSurveyAnswer:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TSurveyAnswer tSurveyAnswer, Model model) {
		model.addAttribute("tSurveyAnswer", tSurveyAnswer);
		return "modules/sys/tSurveyAnswerForm";
	}

	/**
	 * 保存问卷答案
	 */
	@RequiresPermissions(value={"sys:tSurveyAnswer:add","sys:tSurveyAnswer:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TSurveyAnswer tSurveyAnswer, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tSurveyAnswer)){
			return form(tSurveyAnswer, model);
		}
		if(!tSurveyAnswer.getIsNewRecord()){//编辑表单保存
			TSurveyAnswer t = tSurveyAnswerService.get(tSurveyAnswer.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tSurveyAnswer, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tSurveyAnswerService.save(t);//保存
		}else{//新增表单保存
			tSurveyAnswerService.save(tSurveyAnswer);//保存
		}
		addMessage(redirectAttributes, "保存问卷答案成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyAnswer/?repage";
	}
	
	/**
	 * 删除问卷答案
	 */
	@RequiresPermissions("sys:tSurveyAnswer:del")
	@RequestMapping(value = "delete")
	public String delete(TSurveyAnswer tSurveyAnswer, RedirectAttributes redirectAttributes) {
		tSurveyAnswerService.delete(tSurveyAnswer);
		addMessage(redirectAttributes, "删除问卷答案成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyAnswer/?repage";
	}
	
	/**
	 * 批量删除问卷答案
	 */
	@RequiresPermissions("sys:tSurveyAnswer:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tSurveyAnswerService.delete(tSurveyAnswerService.get(id));
		}
		addMessage(redirectAttributes, "删除问卷答案成功");
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyAnswer/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:tSurveyAnswer:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TSurveyAnswer tSurveyAnswer, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷答案"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TSurveyAnswer> page = tSurveyAnswerService.findPage(new Page<TSurveyAnswer>(request, response, -1), tSurveyAnswer);
    		new ExportExcel("问卷答案", TSurveyAnswer.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出问卷答案记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyAnswer/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:tSurveyAnswer:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TSurveyAnswer> list = ei.getDataList(TSurveyAnswer.class);
			for (TSurveyAnswer tSurveyAnswer : list){
				try{
					tSurveyAnswerService.save(tSurveyAnswer);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条问卷答案记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条问卷答案记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入问卷答案失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyAnswer/?repage";
    }
	
	/**
	 * 下载导入问卷答案数据模板
	 */
	@RequiresPermissions("sys:tSurveyAnswer:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "问卷答案数据导入模板.xlsx";
    		List<TSurveyAnswer> list = Lists.newArrayList(); 
    		new ExportExcel("问卷答案数据", TSurveyAnswer.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/tSurveyAnswer/?repage";
    }
	
	/**
	 * 
	 * @Description: 用户答卷界面
	 * @author 罗学林
	 * @date 2017年7月20日 上午6:26:01 
	 * @param tSurveyAnswer
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"dj", ""})
	public String dj(TSurveyAnswer tSurveyAnswer, HttpServletRequest request, HttpServletResponse response, Model model) {
		// 查出问卷
		TSurvey survey = surveyService.get(tSurveyAnswer.getWjid());
		// 查出试题
		List<TSurveyQuestion> qusList = quesService.findListBySurvey(tSurveyAnswer.getWjid());
		// 查出选项
		for (TSurveyQuestion qus : qusList) {
			TSurveyOption tSurveyOption = new TSurveyOption();
			tSurveyOption.setStid(qus.getId());
			List<TSurveyOption> options = optionService.findList(tSurveyOption);
			// 查出答案
			for (TSurveyOption option : options) {
				TSurveyAnswer answer = new TSurveyAnswer();
				answer.setWjid(survey.getId());
				answer.setStid(qus.getId());
				answer.setUserId(tSurveyAnswer.getCurrentUser().getId());
				answer.setStda(option.getId());
				answer = tSurveyAnswerService.findAnswer(answer);
				if (answer != null && answer.getId() != null) {
					option.setCheck("1");
				} else {
					option.setCheck("0");
				}
			}
			
			qus.setOptions(options);
		}
		
		survey.setQusList(qusList);
		
		TSurveyAnswer answer = new TSurveyAnswer();
		answer.setWjid(survey.getId());
		answer.setUserId(tSurveyAnswer.getCurrentUser().getId());
		List<TSurveyAnswer> alist = tSurveyAnswerService.findUserAnswer(answer);
		if (alist.size() > 0) {
			survey.setIsFinish("1");
		} else {
			survey.setIsFinish("0");
		}
		
		model.addAttribute("survey", survey);
		return "modules/sys/tSurveyAnswer";
	}
	
	/**
	 * 
	 * @Description: 保存用户答卷结果
	 * @author 罗学林
	 * @date 2017年7月20日 上午6:26:24 
	 * @param answer
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveanswer")
	public String saveanswer(String answer, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<TSurveyAnswer> list = new ArrayList<TSurveyAnswer>();
		
		String[] sts = answer.split("@");
		for (String st : sts) {
			String[] param = st.split(":");
			String stid = param[0];
			String surveyid = param[1];
			String stda = param[2];
			String[] das = stda.split(",");
			for (String da : das) {
				TSurveyAnswer an = new TSurveyAnswer();
				an.setWjid(surveyid);
				an.setUserId(UserUtils.getUser().getId());
				an.setStid(stid);
				an.setStda(da);
				
				list.add(an);
			}
		}
		
		tSurveyAnswerService.saveList(list);
		String loginfo ="用户["+ UserUtils.getUser().getName() + "提交问卷";
		LogUtils.saveLog(request, loginfo);
		return "succ";
	}
	
	/**
	 * 
	 * @Description: 查看问卷结果统计
	 * @author 罗学林
	 * @date 2017年7月20日 上午6:26:46 
	 * @param tSurveyAnswer
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"count", ""})
	public String count(TSurveyAnswer tSurveyAnswer, HttpServletRequest request, HttpServletResponse response, Model model) {
		// 查出问卷
		TSurvey survey = surveyService.get(tSurveyAnswer.getWjid());
		survey.setZrs(tSurveyAnswerService.findJoinCount(survey.getId()));
		
		
		// 查出试题
		List<TSurveyQuestion> qusList = quesService.findListBySurvey(tSurveyAnswer.getWjid());
		// 查出选项
		for (TSurveyQuestion qus : qusList) {
			TSurveyOption tSurveyOption = new TSurveyOption();
			tSurveyOption.setStid(qus.getId());
			List<TSurveyOption> options = optionService.findList(tSurveyOption);
			// 查出答案
			for (TSurveyOption option : options) {
				TSurveyAnswer answer1 = new TSurveyAnswer();
				answer1.setWjid(survey.getId());
				answer1.setStid(qus.getId());
				answer1.setStda(option.getId());
				List<TSurveyAnswer> as = tSurveyAnswerService.findList(answer1);
				option.setRs(as.size() + "");
				
				DecimalFormat df = new DecimalFormat("######0.00");  
				if (survey.getZrs().equals("0")) {
					option.setBl("0");
				} else {
					Double rate = (Double.parseDouble(option.getRs()) / Double.parseDouble(survey.getZrs())) * 100;
					option.setBl(df.format(rate).equals("NaN") ? "0" : df.format(rate));
				}
			}
			
			qus.setOptions(options);
		}
		
		survey.setQusList(qusList);
		model.addAttribute("survey", survey);
		
		// 已答卷/未答卷人数
		String zrs = receiverService.findUnreadNum(survey.getId());
		Integer unreadNum = Integer.parseInt(zrs) - Integer.parseInt(survey.getZrs());
		model.addAttribute("unread", unreadNum);
		model.addAttribute("readed", Integer.parseInt(survey.getZrs()));
		
		String loginfo ="用户["+ UserUtils.getUser().getName() + "查看问卷统计["+ survey.getName() +"]";
		LogUtils.saveLog(request, loginfo);
		return "modules/sys/tSurveyCount";
	}
	
	@RequestMapping(value = {"userlist", ""})
	public String userlist(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (user.getStid().equals("1")) { // 已达卷列表
			Page<User> page = systemService.findReadedUser(new Page<User>(request, response), user);
	        model.addAttribute("page", page);
		} else { // 未答卷列表
			Page<User> page = systemService.findUnreadUser(new Page<User>(request, response), user);
	        model.addAttribute("page", page);
		}
		return "modules/sys/tSurveyAnswerUserList";
	}

}