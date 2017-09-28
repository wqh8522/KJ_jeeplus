<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>问卷试题管理</title>
	<meta name="decorator" content="default"/>
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
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="tSurveyQuestion" action="${ctx}/sys/tSurveyQuestion/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">题目类型：</label></td>
					<td class="width-35">
						<form:select path="type" class="form-control required" style="width:170px;">
							<form:options items="${fns:getDictList('sys_survey_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">所属题库：</label></td>
					<td class="width-35">
						<form:select id="typeId" path="typeId" class="form-control m-b required" style="width:170px;">
							<c:forEach items="${typeList }" var="type">
								<option value="${type.id }" label="${type.name }"/>
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">题目：</label></td>
					<td class="width-35" colspan="3">
						<form:input path="title" htmlEscape="false"  class="form-control required"/>
					</td>
				</tr>
		 	</tbody>
		</table>
		
		<!-- 选项表格  -->
		<table id="contentTable1" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead>
				<tr>
					<th style="width:50px;">编号</th>
					<th>选项</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="options">
				<c:if test="${tSurveyQuestion.id == null }">
					<tr>
						<td>
							<input name="bhs" htmlEscape="false"  class="form-control bhs" value="A" readonly="true"/>
						</td>
						<td>
							<input name="xxs" htmlEscape="false"  class="form-control required"/>
						</td>
						<td>
							<button type="button" class="btn btn-danger" onclick="del(this);">删除</button>
						</td>
					</tr>
				</c:if>
				<c:if test="${tSurveyQuestion.id != null }">
					<c:forEach items="${optionList }" var="option">
						<tr>
							<td>
								<input name="bhs" htmlEscape="false"  class="form-control required bhs" value="${option.bh }" readonly="true"/>
							</td>
							<td>
								<input name="xxs" htmlEscape="false"  class="form-control required" value="${option.option }"/>
							</td>
							<td>
								<button type="button" class="btn btn-danger" onclick="del(this);">删除</button>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
		<button type="button" class="btn btn-primary" onclick="add();">新增选项</button>
	</form:form>
	<script type="text/javascript">
		
		function add() {
			var items = $(".bhs");
			if(items.length > 25) {
				layer.msg("选项个数最大支持26个,无法添加更多！", {icon:5});
			} else {
				var html = '<tr><td><input name="bhs" htmlEscape="false"  class="form-control required bhs" readonly="true"/></td><td><input name="xxs" htmlEscape="false"  class="form-control required"/></td><td><button type="button" class="btn btn-danger" onclick="del(this);">删除</button></td></tr>';
				$("#options").append(html);
				reorder();
			}
		}
		
		function del(el) {
			$(el).parent().parent().remove();
			reorder();
		}
		
		/*
		 * 重排序
		 */
		function reorder() {
			var chac = new Array("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");
			var items = $(".bhs");
			for(var i = 0; i < items.length; i ++) {
				$(items[i]).val(chac[i])
			}
		}
	
	</script>
</body>
</html>