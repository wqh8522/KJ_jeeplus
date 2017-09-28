<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>调查问卷管理</title>
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
		<h5>问卷部署列表 </h5>
		<div class="ibox-tools">
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="TSurvey" action="${ctx}/sys/tSurvey/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="500"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:tSurvey:add">
				<button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="localadd()" title="添加"><i class="fa fa-plus"></i> 添加</button><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:tSurvey:edit">
			    <button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="localedit()" title="修改"><i class="fa fa-file-text-o"></i> 修改</button><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:tSurvey:del">
				<!-- <table:delRow url="${ctx}/sys/tSurvey/deleteAll" id="contentTable"></table:delRow> 删除按钮 -->
			</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
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
				<th  class="name">名称</th>
				<th  class="isPublish">是否发布</th>
				<th  class="startTime">开始时间</th>
				<th  class="endTime">结束时间</th>
				<th  class="">创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tSurvey">
			<tr>
				<td> <input type="checkbox" id="${tSurvey.id}" class="i-checks"></td>
				<td>
					<a  href="${ctx}/sys/tSurvey/form?id=${tSurvey.id}">
						${tSurvey.name}
					</a>
				</td>
				<td>
					<c:if test="${tSurvey.isPublish eq '1' }">
						<span class="label label-primary">已发布</span>
					</c:if>
					<c:if test="${tSurvey.isPublish eq '0' }">
						<span class="label label-danger">未发布</span>
					</c:if>
				</td>
				<td>
					<fmt:formatDate value="${tSurvey.startTime}" type="both" dateStyle="full" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${tSurvey.endTime}" type="both" dateStyle="full" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${tSurvey.createDate}" type="both" dateStyle="full" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="sys:tSurvey:view">
						<a href="${ctx}/sys/tSurvey/form?id=${tSurvey.id}"  class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:tSurvey:edit">
						<c:if test="${tSurvey.isPublish eq '0' }">
							<a href="${ctx}/sys/tSurvey/form?id=${tSurvey.id}"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
						</c:if>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="sys:tSurvey:del">
						<a href="${ctx}/sys/tSurvey/delete?id=${tSurvey.id}" onclick="return confirmx('确认要删除该调查问卷吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					<c:if test="${tSurvey.isPublish eq '1' }">
						<a href="${ctx}/sys/tSurveyAnswer/count?wjid=${tSurvey.id}"  class="btn btn-success btn-xs" ><i class="fa fa-search-plus"></i> 查看结果统计</a>
					</c:if>
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
	$(document).ready(function() {
	    $('#contentTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
	    	  $('#contentTable tbody tr td input.i-checks').iCheck('check');
	    	});
	
	    $('#contentTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
	    	  $('#contentTable tbody tr td input.i-checks').iCheck('uncheck');
	    	});
	    
	});

	function localadd() {
		var url = "${ctx}/sys/tSurvey/form";
		window.open(url, "_self");
	}
	
	function localedit(){
	  var size = $("#contentTable tbody tr td input.i-checks:checked").size();
	  if(size == 0 ){
			top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
			return;
		  }

	  if(size > 1 ){
			top.layer.alert('只能选择一条数据!', {icon: 0, title:'警告'});
			return;
		  }
	  var id =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("id");
	  var url = "${ctx}/sys/tSurvey/form?id=" + id;
	  window.open(url, "_self");
	}
</script>
</body>
</html>