<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/kindeditor/themes/default/default.css" />
	<link rel="stylesheet" href="${ctxStatic}/kindeditor/plugins/code/prettify.css" />
	<script charset="utf-8" src="${ctxStatic}/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="${ctxStatic}/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="${ctxStatic}/kindeditor/plugins/code/prettify.js"></script>
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
			prettyPrint();
			
			var receiverEmail = "<br/><br/><br/>------------------ 原始邮件 ------------------<br/>";
            receiverEmail += "发件人:${(fns:getUserById(mailBox.sender.id)).name}<br/>";
            receiverEmail += '发送时间:<fmt:formatDate value="${mailBox.sendtime}" pattern="yyyy-MM-dd HH:mm:ss"/><br/>';
            receiverEmail += "收件人:${mailBox.sender.name}<br/>";
            receiverEmail += "主题:${mailBox.mail.title}<br/>";
            receiverEmail += $("#contentView").text();
            editor1.html(receiverEmail);
		});
	</script>

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content">
        <div class="row">
            <div class="col-sm-3">
                <div class="ibox float-e-margins">
                    <div class="ibox-content mailbox-content">
                        <div class="file-manager">
                            <a class="btn btn-block btn-primary compose-mail" href="${ctx}/iim/mailCompose/sendLetter">写信</a>
                            <div class="space-25"></div>
                            <h5>文件夹</h5>
                            <ul class="folder-list m-b-md" style="padding: 0">
                                <li>
                                    <a href="${ctx}/iim/mailBox/list?orderBy=sendtime desc"> <i class="fa fa-inbox "></i> 收件箱 <span class="label label-warning pull-right">${noReadCount}/${mailBoxCount}</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="${ctx}/iim/mailCompose/list?status=1&orderBy=sendtime desc"> <i class="fa fa-envelope-o"></i> 已发送<span class="label label-info pull-right">${mailComposeCount}</span></a>
                                </li>
                                <!-- 
                                <li>
                                    <a href="${ctx}/iim/mailBox/list"> <i class="fa fa-envelope"></i> 群邮件</a>
                                </li>
                                 -->
                                <li>
                                    <a href="${ctx}/iim/mailCompose/list?status=0&orderBy=sendtime desc"> <i class="fa fa-file-text-o"></i> 草稿箱 <span class="label label-danger pull-right">${mailDraftCount}</span>
                                    </a>
                                </li>
                            <div class="clearfix"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-9 animated fadeInRight">
                <div class="mail-box-header">
                    <div class="pull-right tooltip-demo">
                       <button type="button" class="btn btn-white  btn-sm" onclick="saveLetter()"> <i class="fa fa-pencil"></i> 存为草稿</button>
                        <a href="${ctx}/iim/mailBox/list" class="btn btn-danger btn-sm" data-toggle="tooltip" data-placement="top" title="放弃"><i class="fa fa-times"></i> 放弃</a>
                    </div>
                    <h2>
                    写信
                </h2>
                </div>
               
                <div class="mail-box">


                    <div class="mail-body">
					<form:form id="inputForm" modelAttribute="mailBox" action="${ctx}/iim/mailCompose/save" method="post" class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>发送到：</label>

                                <div class="col-sm-8">
			               		 <sys:treeselect id="receiver" name="receiverIds" value="${mailBox.sender.id}" labelName="receiverNames" labelValue="${mailBox.sender.name}"
								title="用户" url="/sys/office/treeData?type=3" cssClass="form-control required" notAllowSelectParent="true" checked="true"/>
                                  <!--  <input type="text" class="form-control" value="${receiver.name}">
                                    <input type="hidden" id="receiver" name="receiver" value="${receiver.id }">-->
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">主题：</label>

                                <div class="col-sm-8">
                                    <input type="text" id="title" name="mail.title"  class="form-control" value="回复:${mailBox.mail.title }">
                                </div>
                            </div>
                          <input type="hidden" id="status" name="status" value="1"><!-- 0 草稿  1 已发送 -->
                          <input type="hidden" id="overview" name="mail.overview"><!-- 内容简介 -->
                    	  <input type="hidden" id="content" name="mail.content"><!-- 内容 -->
 					</form:form>	
                    </div>
                    
                    <textarea name="content1" cols="100" rows="8" style="width:700px;height:200px;visibility:hidden;"></textarea>

                    <div class="mail-body text-right tooltip-demo">
                    	
                    	 <button type="button" class="btn btn-primary  btn-sm" onclick="sendLetter()"> <i class="fa fa-reply"></i> 发送</button>
                        <a href="${ctx}/iim/mailBox/list" class="btn btn-danger btn-sm" data-toggle="tooltip" data-placement="top" title="Discard email"><i class="fa fa-times"></i> 放弃</a>
                   		 <button type="button" class="btn btn-white  btn-sm" onclick="saveLetter()"> <i class="fa fa-pencil"></i> 存为草稿</button>
                    </div>
                    <div class="clearfix"></div>



                </div>
            
            </div>
			
        </div>
        <div class="row">
            <div class="col-sm-12" style="align:center">
            	<input id="btnCancel" class="btn btn-primary btn-bg" style="margin-left:100px" type="button" value="返 回" onclick="history.go(-1)">
            </div>
        </div>
    </div>

   <div style="display:none" id="contentView">
   	  ${mailBox.mail.content}
   </div>

    <script>
        $(document).ready(function () {
            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });

        });

        function sendLetter(){
            if($("#receiverRecordId").val()==''){
            	top.layer.alert('收件人不能为空！', {icon: 0});
            	return;
            }
            if($("#title").val()==''){
              	top.layer.alert('标题不能为空！', {icon: 0});
              	return;
              }
            $("#status").val("1");
            $("#content").val(editor1.html());
			$("#overview").val(editor1.text().substring(0,20));
			var index = layer.load(1, {
			    shade: [0.3,'#fff'] //0.1透明度的白色背景
			});
			$("#inputForm").submit();
	    }
        function saveLetter(){
        	if($("#title").val()==''){
              	top.layer.alert('标题不能为空！', {icon: 0});
              	return;
              }
            $("#status").val("0");
            $("#content").val(editor1.html());
			$("#overview").val(editor1.text().substring(0,20));
			var index = layer.load(1, {
			    shade: [0.3,'#fff'] //0.1透明度的白色背景
			});
			$("#inputForm").submit();
	    }
    </script>

</body>

</html>