package com.jeeplus.modules.sys.entity;


import org.springframework.web.multipart.MultipartFile;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 新闻通知Entity
 * @author luoxuelin
 * @version 2017-07-15
 */
public class TNotice extends DataEntity<TNotice> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String type;		// 类型
	private String colum;		// 栏目
	private String isTop;		// 置顶
	private String cover;		// 封面
	private String content;		// 内容
	private String isPublish;  // 是否发布
	private String createOrg;
	
	private String receIds;	// 接收部门ID
	private String receNames;	// 接收部门名称
	private MultipartFile[] attachs;
	
	public TNotice() {
		super();
	}

	public TNotice(String id){
		super(id);
	}

	@ExcelField(title="标题", align=2, sort=1)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="类型", align=2, sort=2)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="栏目", align=2, sort=3)
	public String getColum() {
		return colum;
	}

	public void setColum(String colum) {
		this.colum = colum;
	}
	
	@ExcelField(title="置顶", align=2, sort=4)
	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String is_top) {
		this.isTop = is_top;
	}
	
	@ExcelField(title="封面图片", align=2, sort=5)
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	@ExcelField(title="内容", align=2, sort=6)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}
	
	public String getCreateOrg() {
		return createOrg;
	}

	public void setCreateOrg(String createOrg) {
		this.createOrg = createOrg;
	}

	public String getReceIds() {
		return receIds;
	}

	public void setReceIds(String receIds) {
		this.receIds = receIds;
	}

	public String getReceNames() {
		return receNames;
	}

	public void setReceNames(String receNames) {
		this.receNames = receNames;
	}

	public MultipartFile[] getAttachs() {
		return attachs;
	}

	public void setAttachs(MultipartFile[] attachs) {
		this.attachs = attachs;
	}
	
	
}