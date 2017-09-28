<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端，就使用自适应大小弹窗
				width='auto';
				height='auto';
			}else{//如果是PC端，根据用户设置的width和height显示。
				width='700px';
				height='500px';
			}

			$("#userPassWordBtn").click(function(){
				top.layer.open({
				    type: 2, 
				    area: [width, height],
				    title:"修改密码",
				    content: "${ctx}/sys/user/modifyPwd" ,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	 var body = top.layer.getChildFrame('body', index);
				         var inputForm = $(body).find('#inputForm');
				         var btn = body.find('#btnSubmit');
				         var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				         inputForm.validate({
								rules: {
								},
								messages: {
									confirmNewPassword: {equalTo: "输入与上面相同的密码"}
								},
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
					     if(inputForm.valid()){
				        	  loading("正在提交，请稍等...");
				        	  inputForm.submit();
				        	  top.layer.close(index);//关闭对话框。
				          }else{
					          return;
				          }
						
						
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			});
			
			$("#userInfoBtn").click(function(){
				top.layer.open({
				    type: 2,  
				    area: [width, height],
				    title:"个人信息编辑",
				    content: "${ctx}/sys/user/infoEdit" ,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	 var body = top.layer.getChildFrame('body', index);
				         var inputForm =  $(body).find('#inputForm');
				         var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				         inputForm.validate();
				         var phone = $(body).find("#mobile").val()
				         console.log(phone)
				         var reg = /^1[3|4|5|7|8][0-9]{9}$/;
				         if(!(reg.test(phone))){ 
				        	 top.layer.msg('手机号码有误，请重填', {icon: 5});
				             return; 
				         }
				         if(inputForm.valid()){
				        	  loading("正在提交，请稍等...");
				        	  inputForm.submit();
				          }else{
					          return;
				          }
				        
						 top.layer.close(index);//关闭对话框。
						
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			});

			$("#userImageBtn").click(function(){
				top.layer.open({
				    type: 2,  
				    area: [width, height],
				    title:"上传头像",
				    content: "${ctx}/sys/user/imageEdit" ,
				  //  btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	 var body = top.layer.getChildFrame('body', index);
				         var inputForm = body.find('#inputForm');
				         var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				         inputForm.validate();
				         if(inputForm.valid()){
				        	  loading("正在提交，请稍等...");
				        	  inputForm.submit();
				          }else{
					          return;
				          }
				        
						 top.layer.close(index);//关闭对话框。
						
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			});
			
		});
	</script>
</head>
<body>

	<body class="gray-bg">
		<div class="wrapper wrapper-content">
			<div class="row animated fadeInRight">
				<sys:message hideType="1" content="${message}"/>
				<div class="col-sm-5">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5>个人资料</h5>
							<div class="ibox-tools">
								<a class="dropdown-toggle" data-toggle="dropdown" href="#">
									编辑<i class="fa fa-wrench"></i>
								</a>
								<ul class="dropdown-menu dropdown-user">
									<!-- <li><a id="userImageBtn" data-toggle="modal" data-target="#register">更换头像</a>
									</li> -->
									<li><a id="userInfoBtn" data-toggle="modal" data-target="#register">编辑资料</a>
									</li>
								</ul>
							</div>
						</div>
						<div class="ibox-content">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<td class="width-15"><label class="pull-right">工号/学号：</label></td>
										<td class="width-35">
											${user.no}
										</td>
									</tr>
									<tr>
										<td class="width-15"><label class="pull-right">姓名：</label></td>
										<td class="width-35">
											${user.name}
										</td>
									</tr>
									<tr>
										<td class="width-15"><label class="pull-right">邮箱：</label></td>
										<td class="width-35">
											${user.email}
										</td>
									</tr>
									<tr>
										<td class="width-15"><label class="pull-right">手机：</label></td>
										<td class="width-35">
											${user.mobile}
										</td>
									</tr>
									<tr>
										<td class="width-15"><label class="pull-right">电话：</label></td>
										<td class="width-35">
											${user.phone}
										</td>
									</tr>
									<tr>
										<td class="width-15"><label class="pull-right">学校：</label></td>
										<td class="width-35">
											${user.company.name}
										</td>
									</tr>
									<tr>
										<td class="width-15"><label class="pull-right">机构：</label></td>
										<td class="width-35">
											${user.office.name}
										</td>
									</tr>
									<tr>
										<td class="width-15"><label class="pull-right">备注：</label></td>
										<td class="width-35">
											${user.remarks}
										</td>
									</tr>
								</tbody>
							</table>
							<strong>上次登录</strong>
							IP: ${user.oldLoginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.oldLoginDate}" type="both" dateStyle="full"/>
											
						</div>
					</div>
				</div>
				<div class="col-sm-5">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5>扩展信息</h5>
							<div class="ibox-tools">
								<a class="dropdown-toggle" data-toggle="dropdown" href="#">
									编辑<i class="fa fa-wrench"></i>
								</a>
								<ul class="dropdown-menu dropdown-user">
									<li><a id="userPassWordBtn" data-toggle="modal" data-target="#register">更换密码</a>
									</li>
								</ul>
							</div>
						</div>
						<div class="ibox-content">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<td class="width-15"><label class="pull-right">用户名：</label></td>
										<td class="width-35">
											${user.loginName}
										</td>
									</tr>
									<tr>
										<td class="width-15"><label class="pull-right">用户角色：</label></td>
										<td class="width-35">
											${user.roleNames}
										</td>
									</tr>
									<tr>
										<td class="width-15"><label class="pull-right">用户类型：</label></td>
										<td class="width-35">
											${fns:getDictLabel(user.userType, 'sys_user_type', '无')}
										</td>
									</tr>
									<tr>
										<td class="width-15"><label class="pull-right">兼职机构：</label></td>
										<td class="width-35">
											${user.otherOrgNames}
										</td>
									</tr>
								</tbody>
							</table>
					
					</div>
					</div>

				</div>

			</div>
		</div>
</body>
</html>