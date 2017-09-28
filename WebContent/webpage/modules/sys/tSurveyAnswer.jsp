<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>调查问卷管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.sttitle{
			font-size:16px;
			color:#000;
			font-weight:bold;
		}
		
		.stxx{
			font-size:16px;
			color:#000;
			padding-left:20px;
		}
	</style>
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
				<h5>${survey.name }</h5>
				<div class="ibox-tools">
				</div>
			</div>
		    
		    <div class="ibox-content">
				<table id="contentTable1" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<tbody>
						<input type="hidden" id="surveyid" value="${survey.id }"/>
						<c:forEach items="${survey.qusList }" var="qus" varStatus="status">
								<tr>
									<td class="sttitle">
										<c:if test="${qus.type eq '2' }">
											${status.index + 1 }、<span class="label label-primary">单选题</span> ${qus.title }
										</c:if>
										<c:if test="${qus.type eq '1' }">
											${status.index + 1 }、<span class="label label-danger">多选题</span> ${qus.title }
										</c:if>
										
										<input type="hidden" value="${qus.id }" class="qus" qustype="${qus.type }"/>
									</td>
								</tr>
								<c:forEach items="${qus.options }" var="option">
									<tr>
										<td class="stxx">
											&nbsp;&nbsp;&nbsp;&nbsp;
											<c:if test="${qus.type eq '2' }">
												<c:if test="${option.check eq '1' }">
													<input type="radio" class="i-checks" name="${qus.id }" value="${option.id }" checked/>
												</c:if>
												<c:if test="${option.check eq '0' }">
													<input type="radio" class="i-checks" name="${qus.id }" value="${option.id }"/>
												</c:if>
											</c:if>
											<c:if test="${qus.type eq '1' }">
												<c:if test="${option.check eq '1' }">
													<input type="checkbox" class="i-checks" name="${qus.id }" value="${option.id }" checked/>
												</c:if>
												<c:if test="${option.check eq '0' }">
													<input type="checkbox" class="i-checks" name="${qus.id }" value="${option.id }"/>
												</c:if>
											</c:if>
											${option.bh }、${option.option }
										</td>
									</tr>
								</c:forEach>
						</c:forEach>
					</tbody>
				</table>
				
				<center>
					<c:if test="${survey.isFinish eq '0' }">
						<button type="button" class="btn btn-primary" onclick="save();">提交答卷</button>
					</c:if>
					<button type="button" class="btn btn-default" onclick="cancel();">返回</button>
				</center>
		    </div>
		</div>		
	</div> 
	<script type="text/javascript">
		function cancel() {
			var url = "${ctx}/sys/tSurvey/mylist";
			window.open(url, "_self");
		}
		
		function save() {
			var qusList = $(".qus");
			var result = "";
			for(var i = 0; i < qusList.length; i ++) {
				var qusid = $(qusList[i]).val();
				var qustype = $(qusList[i]).attr("qustype");
				var val = "";
				if(qustype == "1") { // 多选
					var checkBoxs = $("input[type='checkbox'][name='" + qusid + "']:checked");
					for(var j = 0; j < checkBoxs.length; j ++) {
						var check = checkBoxs[j];
						val += $(check).val() + ',';
					}
					val = val.substring(0, val.length - 1);
				} 
				if(qustype == "2") { // 单选
					var radio = $("input[type='radio'][name='" + qusid + "']:checked").val();
					val = radio;
				} 
				if(val == "" || val == "undefined") {
					layer.msg('请完成所有题目再提交问卷!', {icon: 5});
					return;
				}
				result += qusid + ":" + $("#surveyid").val() + ":" + val + "@";
			}
			
			layer.msg('问卷提交后无法修改，是否确定提交？', {
			  time: 0 //不自动关闭
			  ,btn: ['确定提交', '取消']
			  ,yes: function(index){
				  var url = "${ctx}/sys/tSurveyAnswer/saveanswer?";
					var param = "answer=" + result;
					$.ajax({
			            async : false,
			            cache : false,
			            type : 'get',
			            data : param,
			            url : url + new Date(),
			            success : function(msg) {
			            	top.layer.msg('感谢您参与此次问卷!', {icon: 6});
			            	cancel();
			            }
			        });
			  }
			});
		}
	</script> 
</body>
</html>