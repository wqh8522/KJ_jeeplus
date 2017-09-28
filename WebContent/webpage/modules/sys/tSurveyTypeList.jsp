<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>问卷调查题库管理</title>
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
		<h5>问卷调查题库列表 </h5>
		<div class="ibox-tools">
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="tSurveyType" action="${ctx}/sys/tSurveyType/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
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
				<table:addRow url="${ctx}/sys/tSurveyType/form" title="问卷调查题库"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:tSurveyType:edit">
			    <table:editRow url="${ctx}/sys/tSurveyType/form" title="问卷调查题库" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:tSurveyType:del">
				<table:delRow url="${ctx}/sys/tSurveyType/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:tSurveyType:import">
				<!--<table:importExcel url="${ctx}/sys/tSurveyType/import"></table:importExcel> 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:tSurveyType:export">
	       		<!--<table:exportExcel url="${ctx}/sys/tSurveyType/export"></table:exportExcel> 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<!-- <button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button> -->
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class=" name">名称</th>
				<th  class=" createBy.id">创建者</th>
				<th  class=" createDate">创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tSurveyType">
			<tr>
				<td> <input type="checkbox" id="${tSurveyType.id}" class="i-checks"></td>
				<td>
					<a  href="${ctx}/sys/tSurveyQuestion/list?typeId=${tSurveyType.id}" >
						${tSurveyType.name}
					</a>
				</td>
				<td>
					${tSurveyType.createBy.name}
				</td>
				<td>
					<fmt:formatDate value="${tSurveyType.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="sys:tSurveyType:view">
						<a href="#" onclick="openDialogView('查看问卷调查题库', '${ctx}/sys/tSurveyType/form?id=${tSurveyType.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:tSurveyType:edit">
    					<a href="#" onclick="openDialog('修改问卷调查题库', '${ctx}/sys/tSurveyType/form?id=${tSurveyType.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="sys:tSurveyType:del">
						<a href="${ctx}/sys/tSurveyType/delete?id=${tSurveyType.id}" onclick="return confirmx('确认要删除该问卷调查题库吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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
</body>
</html>