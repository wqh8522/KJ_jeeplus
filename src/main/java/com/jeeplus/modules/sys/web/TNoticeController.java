package com.jeeplus.modules.sys.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.jeeplus.common.utils.UUIDUtil;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.sys.entity.TAttachs;
import com.jeeplus.modules.sys.entity.TNotice;
import com.jeeplus.modules.sys.entity.TNoticeRece;
import com.jeeplus.modules.sys.service.TAttachsService;
import com.jeeplus.modules.sys.service.TNoticeReceService;
import com.jeeplus.modules.sys.service.TNoticeService;
import com.jeeplus.modules.sys.utils.AttachUtil;
import com.jeeplus.modules.sys.utils.LogUtils;

/**
 * 新闻通知Controller
 * @author 罗学林
 * @version 2017-07-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/notice")
public class TNoticeController extends BaseController {

	@Autowired
	private TNoticeService noticeService;
	@Autowired
	private TAttachsService attachsService;
	@Autowired
	private TNoticeReceService receService;
	
	@ModelAttribute
	public TNotice get(@RequestParam(required=false) String id) {
		TNotice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = noticeService.get(id);
		}
		if (entity == null){
			entity = new TNotice();
		}
		return entity;
	}
	
	/**
	 * 新闻通知列表页面
	 */
	@RequiresPermissions("sys:notice:list")
	@RequestMapping(value = {"list", ""})
	public String list(TNotice notice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TNotice> page = noticeService.findPage(new Page<TNotice>(request, response), notice); 
		model.addAttribute("page", page);
		return "modules/sys/noticeList";
	}
	
	/**
	 * 用户通知列表（待完成）
	 * @author 罗学林
	 * @param notice
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:notice:list")
	@RequestMapping(value = {"mylist", ""})
	public String mylist(TNotice notice, HttpServletRequest request, HttpServletResponse response, Model model) {
		notice.setCreateOrg(notice.getCurrentUser().getOffice().getId());
		Page<TNotice> page = noticeService.findUserNoticePage(new Page<TNotice>(request, response), notice); 
		model.addAttribute("page", page);
		return "modules/sys/myNoticeList";
	}

	/**
	 * 查看，增加，编辑新闻通知表单页面
	 */
	@RequiresPermissions(value={"sys:notice:view","sys:notice:add","sys:notice:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TNotice notice, Model model) {
		model.addAttribute("notice", notice);
		List<TAttachs> attachs = new ArrayList<TAttachs>();
		if (notice.getId() != null) { // 修改/查看
			// 查询附件
			attachs = attachsService.findByModule(notice.getId());
			if (notice.getType().equals("1")) {
				List<TNoticeRece> reces = receService.findByNoticeid(notice.getId());
				String ids = "";
				String names = "";
				for (TNoticeRece rece : reces) {
					ids += rece.getOrgId() + ",";
					names += rece.getOrgName() + ",";
				}
				notice.setReceIds(ids.substring(0, ids.length() - 1));
				notice.setReceNames(names.substring(0, names.length() - 1));
			}
		}
		model.addAttribute("attachs", attachs);
		return "modules/sys/noticeForm";
	}
	
	@RequiresPermissions(value={"sys:notice:view","sys:notice:add","sys:notice:edit"},logical=Logical.OR)
	@RequestMapping(value = "view")
	public String view(TNotice notice, Model model, HttpServletRequest request) {
		model.addAttribute("notice", notice);
		List<TAttachs> attachs = new ArrayList<TAttachs>();
		if (notice.getId() != null) { // 修改/查看
			// 查询附件
			attachs = attachsService.findByModule(notice.getId());
			if (notice.getType().equals("1")) {
				List<TNoticeRece> reces = receService.findByNoticeid(notice.getId());
				String ids = "";
				String names = "";
				for (TNoticeRece rece : reces) {
					ids += rece.getOrgId() + ",";
					names += rece.getOrgName() + ",";
				}
				notice.setReceIds(ids.substring(0, ids.length() - 1));
				notice.setReceNames(names.substring(0, names.length() - 1));
			}
		}
		model.addAttribute("attachs", attachs);
		String loginfo = "查看新闻通知["+ notice.getTitle() +"]";
		LogUtils.saveLog(request, loginfo);
		return "modules/sys/noticeView";
	}

	/**
	 * 保存新闻通知
	 */
	@RequiresPermissions(value={"sys:notice:add","sys:notice:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TNotice notice, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, notice)){
			return form(notice, model);
		}
		// 附件
		List<TAttachs> attachs = new ArrayList<TAttachs>();
		if (notice.getAttachs() != null) {
			for (MultipartFile file : notice.getAttachs() ) {
				if (file.getOriginalFilename() != null && !file.getOriginalFilename().equals("")) {
					attachs.add(AttachUtil.save(file));
				}
			}
		}
		String loginfo = "";
		if(!notice.getIsNewRecord()){//编辑表单保存
			loginfo = "修改新闻通知["+ notice.getTitle() +"]";
			TNotice t = noticeService.get(notice.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(notice, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			noticeService.save(notice, attachs);//保存
		}else{//新增表单保存
			loginfo = "新增新闻通知["+ notice.getTitle() +"]";
			notice.setCreateOrg(notice.getCurrentUser().getOffice().getId());
			noticeService.save(notice, attachs);//保存
		}
		LogUtils.saveLog(request, loginfo);
		addMessage(redirectAttributes, "保存新闻通知成功");
		return "redirect:"+Global.getAdminPath()+"/sys/notice/list?repage";
	}
	
	
	/**
	 * 删除新闻通知
	 */
	@RequiresPermissions("sys:notice:del")
	@RequestMapping(value = "delete")
	public String delete(TNotice notice, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		noticeService.delete(notice);
		String loginfo = "删除新闻通知["+ notice.getTitle() +"]";
		LogUtils.saveLog(request, loginfo);
		addMessage(redirectAttributes, "删除新闻通知成功");
		return "redirect:"+Global.getAdminPath()+"/sys/notice/list?repage";
	}
	
	
	/**
	 * 删除新闻通知
	 */
	@RequiresPermissions("sys:notice:edit")
	@RequestMapping(value = "top")
	public String top(TNotice notice, RedirectAttributes redirectAttributes) {
		String top = notice.getIsTop();
		notice = noticeService.get(notice.getId());
		notice.setIsTop(top);
		noticeService.save(notice);
		String msg = "1".equals(top)?"置顶新闻通知成功":"取消置顶新闻通知成功";
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/sys/notice/list?repage";
	}
	
	/**
	 * 批量删除新闻通知
	 */
	@RequiresPermissions("sys:notice:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			TNotice notice = noticeService.get(id);
			noticeService.delete(notice);
			String loginfo = "删除新闻通知["+ notice.getTitle() +"]";
			LogUtils.saveLog(request, loginfo);
		}
		addMessage(redirectAttributes, "删除新闻通知成功");
		return "redirect:"+Global.getAdminPath()+"/sys/notice/list?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sys:notice:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TNotice notice, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "新闻通知"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TNotice> page = noticeService.findPage(new Page<TNotice>(request, response, -1), notice);
    		new ExportExcel("新闻通知", TNotice.class).setDataList(page.getList()).write(response, fileName).dispose();
    		String loginfo = "导出新闻通知";
			LogUtils.saveLog(request, loginfo);
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出新闻通知记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/notice/?repage";
    }

	/**
	 * 导入Excel数据
	 */
	@RequiresPermissions("sys:notice:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TNotice> list = ei.getDataList(TNotice.class);
			for (TNotice notice : list){
				try{
					noticeService.save(notice);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条新闻通知记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条新闻通知记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入新闻通知失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/notice/?repage";
    }
	
	/**
	 * 下载导入新闻通知数据模板
	 */
	@RequiresPermissions("sys:notice:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "新闻通知数据导入模板.xlsx";
    		List<TNotice> list = Lists.newArrayList(); 
    		new ExportExcel("新闻通知数据", TNotice.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/notice/?repage";
    }
	
	@RequestMapping(value = "cropmodal")
    public String cropmodal(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		
		return "modules/sys/noticeCrop";
    }
	
	@ResponseBody
	@RequestMapping(value = "coversave")
    public String coversave(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getRealPath("/");
		// 读取保存文件
		String savePath = "/asset/newscover/";
		File outFolder = new File(savePath);
		if(!outFolder.exists()){
			outFolder.mkdirs();
		}
		savePath +=  UUIDUtil.randomUUID() + ".jpg";
		
		byte[] bs = new byte[1024];
        int len;

        InputStream inputStream = file.getInputStream();
        OutputStream os = null;
        File tempFile = new File(path);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        os = new FileOutputStream(path + savePath);
        // 开始读取
        while ((len = inputStream.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
		return savePath;
    }
	
}