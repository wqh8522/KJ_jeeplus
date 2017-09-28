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
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form id="searchForm" modelAttribute="tSurveyQuestion" action="${ctx}/sys/tSurveyQuestion/select" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>题目类型：</span>
				<select path="type" class="form-control">
					<option value="">请选择</option>
        			<c:forEach items="${fns:getDictList('sys_survey_type')}" var="type">
						<option value="${type.value }">${type.label }</option>
					</c:forEach>
				</select>
			<span>所属题库：</span>
				<select id="typeId" path="typeId" class="form-control m-b">
					<option value="" label="请选择"/>
					<c:forEach items="${typeList }" var="type">
						<option value="${type.id }" label="${type.name }"/>
					</c:forEach>
				</select>
			<span>题目：</span>
				<input path="title" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
		 </div>	
	</form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
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
					<c:if test="${tSurveyQuestion.sfjr eq '0'}">
						<a href="#" onclick="add(this);" class="btn btn-info btn-xs" qusid="${tSurveyQuestion.id}"><i class="fa fa-search-plus"></i> 加入问卷</a>
					</c:if>
					<c:if test="${tSurveyQuestion.sfjr eq '1'}">
						已加入问卷
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

	function add(el) {
		var url = "${ctx}/sys/tSurvey/addqus?";
		var param = "qusid=" + $(el).attr("qusid");
		$.ajax({
            async : false,
            cache : false,
            type : 'get',
            data : param,
            url : url + new Date(),
            success : function(msg) {
            	var parent = $(el).parent();
            	$(el).remove();
            	$(parent).html("已加入问卷");
            }
        });
	}

</script>
</body>
</html>