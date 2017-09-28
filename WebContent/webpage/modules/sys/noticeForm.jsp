<!doctype html> 
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>新闻通知管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/kindeditor/themes/default/default.css" />
	<link rel="stylesheet" href="${ctxStatic}/kindeditor/plugins/code/prettify.css" />
	<link rel="stylesheet" href="${ctxStatic}/css/cropper.min.css" />
	<script charset="utf-8" src="${ctxStatic}/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="${ctxStatic}/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="${ctxStatic}/js/cropper.min.js"></script>
	<script>
		var editor1;
		KindEditor.ready(function(K) {
			editor1 = K.create('textarea[name="content1"]', {
				cssPath : '${ctxStatic}/kindeditor/plugins/code/prettify.css',
				uploadJson : '${ctxStatic}/kindeditor/jsp/upload_json.jsp',
				fileManagerJson : '${ctxStatic}/kindeditor/jsp/file_manager_json.jsp',
				allowFileManager : true,
                width : '100%',
                height : '300px',
                filterMode : false,
				afterCreate : function() {
					var self = this;
				}
			});
		});
	</script>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
	</script>
</head>
<body class="hideScroll gray-bg" style="height:100%;">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>新闻通知 </h5>
				<div class="ibox-tools">
				</div>
			</div>
		    
		    <div class="ibox-content">
		    	<form:form id="inputForm" modelAttribute="notice" action="${ctx}/sys/notice/save" method="post" class="form-horizontal" enctype="multipart/form-data">
				<form:hidden path="id"/>
				<form:hidden path="isPublish" id="isPublish"/>
				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				   <tbody>
						<tr>
							<td class="width-15 active">
								<label class="pull-right">类型：</label>
							</td>
							<td class="width-35">
								<form:select path="type" class="form-control required" onchange="typechange(this);">
									<option value="">请选择</option>
			         				<form:options items="${fns:getDictList('sys_notice_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</td>
							<td class="width-15 active">
								<label class="pull-right">栏目：</label>
							</td>
							<td class="width-35">
								<form:select path="colum" class="form-control required" id="colum">
									<option value="">请选择</option>
			         				<form:options items="${fns:getDictList('sys_notice_column')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</td>
						</tr>
						<tr>
							<td class="width-15 active">
								<label class="pull-right">标题：</label>
							</td>
							<td class="width-35" colspan="3">
								<form:input path="title" htmlEscape="false"  class="form-control required"/>
							</td>
						</tr>
						<tr>
							<td class="width-15 active">
								<label class="pull-right">发送到：</label>
							</td>
							<td class="width-35">
								<sys:treeselect id="rece" name="receIds" value="${notice.receIds}" labelName="receNames" labelValue="${notice.receNames}"
									title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" notAllowSelectParent="false" checked="true"/>
							</td>
							<td class="width-15 active">
								<label class="pull-right">是否置顶：</label>
							</td>
							<td class="width-35">
								<form:select path="isTop" class="form-control required" id="isTop">
			         				<option value="0">否</option>
			         				<option value="1">是</option>
								</form:select>
							</td>
						</tr>
						<tr>
							<td class="width-15 active"><label class="pull-right">新闻封面：</label></td>
							<td class="width-35">
								<input type="file" id="imgsel" class="form-control" onchange="cropimg(this);"/>
								<form:hidden path="cover" id="cover"/>
								<c:if test="${notice.cover == null }">
									<img src="" style="width:200px;display:none;" id="cover_preview"/>
								</c:if>
								<c:if test="${notice.cover != null }">
									<img src="${ctxRoot }${notice.cover}" style="width:200px;" id="cover_preview"/>
								</c:if>
							</td>
							<td class="width-15 active"><label class="pull-right">附件：</label></td>
							<td class="width-35">
								<form:input type="file" id="attachs" path="attachs" class="form-control" multiple="multiple"/>
								<br>
								<c:forEach items="${attachs }" var="attach">
									<div class="alert alert-success alert-dismissible" role="alert" style="line-height: 0.42857;margin-bottom:5px;">
									  <button type="button" class="close" data-dismiss="alert" aria-label="Close" onclick="delAttach('${attach.id}')"><span aria-hidden="true">&times;</span></button>
									  <a href="${ctx }/sys/attach/download?id=${attach.id}">${attach.fileName }</a>
									</div>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<td class="width-15 active"><label class="pull-right">内容：</label></td>
							<td class="width-35" colspan="3">
								<form:hidden cols="100" rows="8"  path="content"/>
								<textarea name="content1" cols="100" rows="8">${notice.content}</textarea>
							</td>
						</tr>
				 	</tbody>
				</table>
			</form:form>
			<center>
				<c:if test="${notice.isPublish eq '0' || notice.isPublish == null}">
					<button type="button" class="btn btn-primary" onclick="save('1');">保存并发布</button>
					<button type="button" class="btn btn-info" onclick="save('0');">保存草稿</button>
				</c:if>
				<button type="button" class="btn btn-default" onclick="cancel();">取消</button>
			</center>
		    </div>
		</div>		
	</div> 
	<div style="display:none;" id="crop">
		<img src="" id="img" style="width:100%;">
	</div>
	<script type="text/javascript">
		function cropimg(el) {
			var url =  getObjectURL(el.files[0]);
			if(url) {
				$("#img").attr("src", url);
			}
			layer.open({
			  type: 2,
			  title: '封面裁剪',
			  closeBtn: 0,
			  shadeClose: true,
			  area: ['800px', '500px'],
			  shadeClose:false,
			  content: '${ctx}/sys/notice/cropmodal',
			  btn: ['确定'],
			  yes: function(index, layero){
			    layer.load();
			    var iframeWin = window['layui-layer-iframe' + index];
			    var resImg = iframeWin.getImg();
			    var fd = new FormData();
			    var blob = dataURItoBlob(resImg);
			    fd.append('file', blob);
				$.ajax({
				    async : false,
				    cache : false,
				    type : 'post',
				    data : fd,
				    processData: false, // 不会将 data 参数序列化字符串
				    contentType: false,
				    url : '${ctx}/sys/notice/coversave?'+new Date(),
				    success : function(msg) {
				        var cover = '${ctxRoot}' + msg;
				        $("#cover_preview").attr("src", cover);
				        $("#cover_preview").show();
				        $("#cover").val(msg);
				        $("#imgsel").remove();
				        layer.closeAll('loading');
				        layer.close(index); //如果设定了yes回调，需进行手工关闭
				    }
				});
			  }
			});
		}
		
		function getObjectURL(file) {
			var url =  null;
			if (window.createObjectURL != undefined) { // basic
		        url = window.createObjectURL(file);
		    } else if (window.URL != undefined) { // mozilla(firefox)
		        url = window.URL.createObjectURL(file);
		    } else if (window.webkitURL != undefined) { // webkit or chrome
		        url = window.webkitURL.createObjectURL(file);
		    }
		    return url;
		}
		
		/*
		 * 图片Base64转二进制
		 */
		function dataURItoBlob(dataURI) {
		    var byteString = atob(dataURI.split(',')[1]);
		    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
		    var ab = new ArrayBuffer(byteString.length);
		    var ia = new Uint8Array(ab);
		    for (var i = 0; i < byteString.length; i++) {
		        ia[i] = byteString.charCodeAt(i);
		    }
		    return new Blob([ab], {type: mimeString});
		}
		
		function typechange(el) {
			console.log($(el).val())
			if($(el).val() == '1') { // 通知
				$("#receName").removeAttr("disabled");
				$("#receName").show();
				$("#receButton").removeAttr("disabled");
				$("#receButton").show();
				$("#colum").attr("disabled", true);
				$("#colum").hide();
				$("#imgsel").attr("disabled", true);
				$("#imgsel").hide();
				$("#isTop").attr("disabled", true);
				$("#isTop").hide();
			} else { // 新闻
				$("#receName").attr("disabled", true);
				$("#receName").hide();
				$("#receButton").attr("disabled", true);
				$("#receButton").hide();
				$("#colum").removeAttr("disabled");
				$("#colum").show();
				$("#imgsel").removeAttr("disabled");
				$("#imgsel").show();
				$("#isTop").removeAttr("disabled");
				$("#isTop").show();
			}
		}
		
		function save(pub) {
			$("#isPublish").val(pub);
			$("#content").val(editor1.html());
			$("#inputForm").submit();
		}
		
		function cancel() {
			var url = "${ctx}/sys/notice/list";
			window.open(url, "_self")
		}
		
		$(function() {
			var el = $("#type");
			if($(el).val() == '1') { // 通知
				$("#colum").attr("disabled", true);
				$("#receIdsName").removeAttr("disabled");
				$("#receIdsButton").removeAttr("disabled");
				$("#imgsel").attr("disabled", true);
				$("#isTop").attr("disabled", true);
			} else { // 新闻
				$("#colum").removeAttr("disabled");
				$("#receIdsName").attr("disabled", true);
				$("#receIdsButton").attr("disabled", true);
				$("#imgsel").removeAttr("disabled");
				$("#isTop").removeAttr("disabled");
			}
		});
		
		function delAttach(id) {
			$.ajax({
			    async : false,
			    cache : false,
			    type : 'get',
			    data : "id=" + id,
			    processData: false, // 不会将 data 参数序列化字符串
			    contentType: false,
			    url : '${ctx}/sys/attach/delete?'+new Date(),
			    success : function(msg) {
			        
			    }
			});
		}
		
	</script>  
</body>
</html>