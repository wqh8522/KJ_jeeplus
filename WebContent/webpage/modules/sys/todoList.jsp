<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>待办事项</title>
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
		<h5>待办事项</h5>
		<div class="ibox-tools">
			
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column title">标题</th>
				<th  class="sort-column currStatus">当前状态</th>
				<th  class="sort-column createTime">创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="todo">
			<tr>
				<td> <input type="checkbox" id="${todo.id}" class="i-checks"></td>
				<td>
					<a  href="${ctx}/sys/todo/deal?id=${todo.id}">
						<c:if test="${todo.isRead == 0}"><div style="width:8px;height:8px;margin-top:5px;border-radius:100%;background-color:red;float:left;"></div></c:if>${todo.title}
					</a>
				</td>
				<td>
					${todo.currStatus}
				</td>
				<td>
					<fmt:formatDate value="${todo.createDate}" type="both" dateStyle="full" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="sys:todo:edit">
	   					<a href="${ctx}/sys/todo/deal?id=${todo.id}" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 处理</a>
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