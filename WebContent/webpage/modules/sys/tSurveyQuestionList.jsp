<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>问卷试题管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>题库试题 </h5>
		<div class="ibox-tools">
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="TSurveyQuestion" action="${ctx}/sys/tSurveyQuestion/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>题目类型：</span>
				<form:select path="type" class="form-control" style="width:170px;">
					<option value="">请选择</option>
        			<c:forEach items="${fns:getDictList('sys_survey_type')}" var="type">
						<option value="${type.value }">${type.label }</option>
					</c:forEach>
				</form:select>
			<span>题目：</span>
				<form:input path="title" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
		 		<input type="hidden" name="typeId" value="${currQusTypeId }"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:tSurveyType:add">
				<table:addRow url="${ctx}/sys/tSurveyQuestion/form" title="问卷试题"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:tSurveyType:edit">
			    <table:editRow url="${ctx}/sys/tSurveyQuestion/form" title="问卷试题" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:tSurveyType:del">
				<table:delRow url="${ctx}/sys/tSurveyQuestion/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:tSurveyType:import">
				<table:importExcel url="${ctx}/sys/tSurveyQuestion/import"></table:importExcel> <!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:tSurveyType:export">
	       		<!--<table:exportExcel url="${ctx}/sys/tSurveyQuestion/export"></table:exportExcel> 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="cancel()" title="返回"><i class="glyphicon glyphicon-arrow-left"></i> 返回</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column type">题目类型</th>
				<th  class="sort-column typeId">所属题库</th>
				<th  class="sort-column title">题目</th>
				<th  class="sort-column remarks">创建者</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tSurveyQuestion">
			<tr>
				<td> <input type="checkbox" id="${tSurveyQuestion.id}" class="i-checks"></td>
				<td>
					${fns:getDictLabel(tSurveyQuestion.type, 'sys_survey_type', "无")}
				</td>
				<td>
					${tSurveyQuestion.typeName}
				</td>
				<td>
					<a  href="#" onclick="openDialogView('查看问卷试题', '${ctx}/sys/tSurveyQuestion/form?id=${tSurveyQuestion.id}','800px', '500px')">
						${tSurveyQuestion.title}
					</a>
				</td>
				<td>
					${tSurveyQuestion.createBy.name}
				</td>
				<td>
					<shiro:hasPermission name="sys:tSurveyType:view">
						<a href="#" onclick="openDialogView('查看问卷试题', '${ctx}/sys/tSurveyQuestion/form?id=${tSurveyQuestion.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:tSurveyType:edit">
    					<a href="#" onclick="openDialog('修改问卷试题', '${ctx}/sys/tSurveyQuestion/form?id=${tSurveyQuestion.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="sys:tSurveyType:del">
						<a href="${ctx}/sys/tSurveyQuestion/delete?id=${tSurveyQuestion.id}" onclick="return confirmx('确认要删除该问卷试题吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
<script type="text/javascript">
	function cancel()  {
		var url = "${ctx}/sys/tSurveyType/list";
		window.open(url, "_self");
	}
</script>
</body>
</html>