<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>调查问卷管理</title>
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
<body class="hideScroll gray-bg" style="height:100%;">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>问卷部署</h5>
				<div class="ibox-tools">
				</div>
			</div>
		    
		    <div class="ibox-content">
		    	<form:form id="inputForm" modelAttribute="tSurvey" action="${ctx}/sys/tSurvey/save" method="post" class="form-horizontal">
				<form:hidden path="id"/>
				<sys:message content="${message}"/>	
				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				   <tbody>
						<tr>
							<td class="width-15 active"><label class="pull-right">名称：</label></td>
							<td class="width-35" colspan="3">
								<form:input path="name" htmlEscape="false"    class="form-control required"/>
							</td>
						</tr>
						<tr>
							<td class="width-15 active"><label class="pull-right">开始时间：</label></td>
							<td class="width-35">
								<input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
									value="<fmt:formatDate value="${tSurvey.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="laydate({elem: '#startTime'});"/>
							</td>
							<td class="width-15 active"><label class="pull-right">结束时间：</label></td>
							<td class="width-35">
								<input id="endTime" name="endTime" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
									value="<fmt:formatDate value="${tSurvey.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="laydate({elem: '#endTime'});"/>
							</td>
						</tr>
						<tr>
							<td class="width-15 active">
								<label class="pull-right">调查机构：</label>
							</td>
							<td class="width-35">
								<sys:treeselect id="rece" name="orgIds" value="${tSurvey.orgIds}" labelName="orgNames" labelValue="${tSurvey.orgNames}"
											title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" notAllowSelectParent="false" checked="true" simpleCheck="true"/>
							</td>
							<td class="width-15 active"><label class="pull-right">是否发布：</label></td>
							<td class="width-35">
								<form:select path="isPublish"  class="form-control required">
									<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</td>
						</tr>
				 	</tbody>
				</table>
			</form:form>
			
			<!-- 问卷试题列表 -->
			<button type="button" class="btn btn-primary" onclick="add();">新增试题</button>
			<button type="button" class="btn btn-success" onclick="auto();">自动组卷</button>
			<table id="contentTable1" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
				<thead>
					<tr>
						<th style="width:100px;">试题类别</th>
						<th>试题</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="options">
					<c:forEach items="${qusList }" var="qus">
						<tr>
							<td>
								${qus.typeName }
							</td>
							<td>
								<a  href="#" onclick="view(this);" qusid="${qus.id }">
									${qus.title }
								</a>
							</td>
							<td>
								<button type="button" class="btn btn-danger" onclick="del(this);" qusid="${qus.id }">删除</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<center>
				<button type="button" class="btn btn-primary" onclick="save();">保存</button>
				<button type="button" class="btn btn-default" onclick="cancel();">取消</button>
			</center>
		    </div>
		</div>		
	</div> 
	<script type="text/javascript">
		function save(pub) {
			$("#inputForm").submit();
		}
		
		function cancel() {
			var url = "${ctx}/sys/tSurvey/list";
			window.open(url, "_self")
		}
		
		function add() {
			layer.open({
			  type: 2,
			  title: '试题列表',
			  closeBtn: 0,
			  shadeClose: true,
			  area: ['900px', '500px'],
			  shadeClose:false,
			  content: '${ctx}/sys/tSurveyQuestion/select',
			  btn: ['确定'],
			  yes: function(index, layero){
				  // 获取新数据，刷新页面
				  layer.load();
				  
				  var url = "${ctx}/sys/tSurvey/choosed?";
				  var param = "";
				  $.ajax({
			            async : false,
			            cache : false,
			            type : 'get',
			            data : param,
			            url : url + new Date(),
			            success : function(msg) {
			            	var data = JSON.parse(msg);
			            	var html = '';
			            	for(var i = 0; i < data.length; i ++) {
			            		var item = data[i];
								html += '<tr><td>';
			            		html += item.typeName;
			            		html += '</td><td><a  href="#" onclick="view(this);" qusid="';
			            		html += item.id;
			            		html += '">';
			            		html += item.title;
			            		html += '</a></td><td><button type="button" class="btn btn-danger" onclick="del(this);" qusid="';
			            		html += item.id;
			            		html += '">删除</button></td></tr>';
			            	}
			            	
			            	$("#options").html(html);
			            }
			      });
				  
				  layer.closeAll('loading');
				  
				  layer.close(index); //如果设定了yes回调，需进行手工关闭
			  }
			});
		}
		
		function auto() {
			layer.open({
			  type: 2,
			  title: '自动组卷',
			  closeBtn: 0,
			  shadeClose: true,
			  area: ['900px', '500px'],
			  shadeClose:false,
			  content: '${ctx}/sys/tSurveyQuestion/autoform',
			  btn: ['确定'],
			  yes: function(index, layero){
				// 获取新数据，刷新页面
				layer.load();

				var iframeWin = window['layui-layer-iframe' + index];
				var data = iframeWin.getdata();

				var url = "${ctx}/sys/tSurvey/autosave?";
				var param = "qusid=" + data;
				$.ajax({
					async : false,
					cache : false,
					type : 'get',
					data : param,
					url : url + new Date(),
					success : function(msg) {
						var url = "${ctx}/sys/tSurvey/choosed?";
						var param = "";
						$.ajax({
						    async : false,
						    cache : false,
						    type : 'get',
						    data : param,
						    url : url + new Date(),
						    success : function(msg) {
						    	var data = JSON.parse(msg);
						    	var html = '';
						    	for(var i = 0; i < data.length; i ++) {
						    		var item = data[i];
									html += '<tr><td>';
						    		html += item.typeName;
						    		html += '</td><td><a  href="#" onclick="view(this);" qusid="';
						    		html += item.id;
						    		html += '">';
						    		html += item.title;
						    		html += '</a></td><td><button type="button" class="btn btn-danger" onclick="del(this);" qusid="';
						    		html += item.id;
						    		html += '">删除</button></td></tr>';
						    	}
						    	
						    	$("#options").html(html);
						    }
						});
					}
				});
				  
				layer.closeAll('loading');

				layer.close(index); //如果设定了yes回调，需进行手工关闭
			  }
			});
		}
		
		function del(el) {
			var url = "${ctx}/sys/tSurvey/removequs?";
			var param = "qusid=" + $(el).attr("qusid");
			$.ajax({
	            async : false,
	            cache : false,
	            type : 'get',
	            data : param,
	            url : url + new Date(),
	            success : function(msg) {
	            	$(el).parent().parent().remove();
	            }
	        });
		}
		
		function view(el) {
			var id = $(el).attr("qusid")
			openDialogView('查看问卷试题', '${ctx}/sys/tSurveyQuestion/form?id=' + id,'800px', '500px')
		}
	</script> 
</body>
</html>