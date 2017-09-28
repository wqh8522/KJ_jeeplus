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
				<center>
								<h1>${notice.title }</h1>
								<small><fmt:formatDate value="${notice.createDate}" type="both" dateStyle="full"/></small>
							</center>
							
								<div>
									${fns:unescapeHtml(notice.content)}
								</div>
								
								<br/><br/>
								<br/><br/>
								
								<c:forEach items="${attachs }" var="attach" varStatus="status">
									<div class="alert alert-success alert-dismissible" role="alert" style="line-height: 0.42857;margin-bottom:5px;">
									  <strong>附件列表${status.index+1 }：</strong><a href="${ctx }/sys/attach/download?id=${attach.id}">${attach.fileName }</a>
									</div>
								</c:forEach>
							
				<center>
				<button type="button" class="btn btn-default" onclick="cancel();">返回</button>
			</center>
			</form:form>
			
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
		}
		
		function save(pub) {
			$("#isPublish").val(pub);
			$("#content").val(editor1.html());
			$("#inputForm").submit();
		}
		
		function cancel() {
			var url = "${ctx}/sys/notice/mylist";
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