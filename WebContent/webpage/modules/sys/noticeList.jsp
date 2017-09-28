<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>新闻通知管理</title>
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
		<h5>新闻通知 </h5>
		<div class="ibox-tools">
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="TNotice" action="${ctx}/sys/notice/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
				<span>标题：</span>
				<form:input path="title" htmlEscape="false" maxlength="50" class=" form-control input-sm"/>
				
				<span>类型：</span>
				<form:select path="type" class="form-control" style="width:170px;">
					<option value="">请选择</option>
        			<form:options items="${fns:getDictList('sys_notice_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			
				<span>新闻栏目：</span>
				<form:select path="colum" class="form-control" id="colum" style="width:170px;">
					<option value="">请选择</option>
        			<form:options items="${fns:getDictList('sys_notice_column')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		</div>
	</form:form>
	</div>
	</div>
	
	<br/>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:notice:add">
				<button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="localadd()" title="添加"><i class="fa fa-plus"></i> 添加</button><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:notice:edit">
			    <button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="localedit()" title="修改"><i class="fa fa-file-text-o"></i> 修改</button><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:notice:del">
				<!-- <table:delRow url="${ctx}/sys/notice/deleteAll" id="contentTable"></table:delRow> 删除按钮 -->
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
				<th  class=" type">类型</th>
				<th  class=" title">标题</th>
				<th  class=" colum">栏目</th>
				<th  class=" is_top">置顶</th>
				<th  class=" is_top">创建人</th>
				<th  class=" is_top">是否发布</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="notice">
			<tr>
				<td> <input type="checkbox" id="${notice.id}" class="i-checks"></td>
				<td>
					${fns:getDictLabel(notice.type, 'sys_notice_type', "无")}
				</td>
				<td>
					<a  href="${ctx}/sys/notice/form?id=${notice.id}">
						${notice.title}
					</a>
				</td>
				<td>
					${fns:getDictLabel(notice.colum, 'sys_notice_column', "无")}
				</td>
				<td>
					<c:if test="${notice.isTop eq '1' }">
						<span class="label label-warning">置顶</span>
					</c:if>
					<c:if test="${notice.isTop eq '0' }">
						<span class="label label-primary">否</span>
					</c:if>
					<c:if test="${notice.isTop eq null }">
						<span class="label label-default">无</span>
					</c:if>
				</td>
				<td>
					${notice.createBy.name}
				</td>
				<td>
					<c:if test="${notice.isPublish eq '1' }">
						<span class="label label-primary">已发布</span>
					</c:if>
					<c:if test="${notice.isPublish eq '0' }">
						<span class="label label-danger">未发布</span>
					</c:if>
				</td>
				<td>
					<shiro:hasPermission name="sys:notice:view">
						<a href="${ctx}/sys/notice/form?id=${notice.id}" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:notice:edit">
    					<a href="${ctx}/sys/notice/form?id=${notice.id}" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="sys:notice:del">
    					<c:if test="${notice.isPublish eq '0' || notice.isPublish == null }">
    						<a href="${ctx}/sys/notice/delete?id=${notice.id}" onclick="return confirmx('确认要删除该新闻通知吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
    					</c:if>
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:notice:edit">
						<c:if test="${notice.isTop eq '0' }">
							<a href="${ctx}/sys/notice/top?id=${notice.id}&isTop=1" onclick="return confirmx('确认要置顶该新闻通知吗？', this.href)"   class="btn btn-warning btn-xs"><i class="fa fa-trash"></i> 置顶消息</a>
						</c:if>
						<c:if test="${notice.isTop eq '1' }">
							<a href="${ctx}/sys/notice/top?id=${notice.id}&isTop=0" onclick="return confirmx('确认取消置顶该新闻通知吗？', this.href)"   class="btn btn-warning btn-xs"><i class="fa fa-trash"></i> 取消置顶</a>
						</c:if>
						
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
	$(document).ready(function() {
	    $('#contentTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
	    	  $('#contentTable tbody tr td input.i-checks').iCheck('check');
	    	});
	
	    $('#contentTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
	    	  $('#contentTable tbody tr td input.i-checks').iCheck('uncheck');
	    	});
	    
	});

	function localadd() {
		var url = "${ctx}/sys/notice/form";
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
	  var url = "${ctx}/sys/notice/form?id=" + id;
	  window.open(url, "_self");
	}
</script>
</body>
</html>