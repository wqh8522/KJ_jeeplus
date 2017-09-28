<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>首页</title>
	<style type="text/css">
	#calendar{ margin:40px auto 10px auto;width:99%;height:50%;}
	.fancy{width:450px; height:200px}
	.fancy h3{height:30px; line-height:30px; border-bottom:1px solid #d3d3d3; font-size:14px}
	.fancy form{padding:10px}
	.fancy p{height:28px; line-height:28px; padding:4px; color:#999}
	.input{height:20px; line-height:20px; padding:2px; border:1px solid #d3d3d3; width:100px}
	.btn{-webkit-border-radius: 3px;-moz-border-radius:3px;padding:5px 12px; cursor:pointer}
	.btn_ok{background: #360;border: 1px solid #390;color:#fff}
	.btn_cancel{background:#f0f0f0;border: 1px solid #d3d3d3; color:#666 }
	.btn_del{background:#f90;border: 1px solid #f80; color:#fff }
	.sub_btn{height:32px; line-height:32px; padding-top:6px; border-top:1px solid #f0f0f0; text-align:right; position:relative}
	.sub_btn .del{position:absolute; left:2px}
</style>

<script src='${ctxStatic}/fullcalendar/js/jquery-1.9.1.js'></script>
<script src='${ctxStatic}/fullcalendar/js/jquery-ui.js'></script>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/fullcalendar/css/main.css">
<link rel="stylesheet" type="text/css" href="${ctxStatic}/fullcalendar/css/fancybox.css">
<script src='${ctxStatic}/fullcalendar/js/jquery.fancybox-1.3.1.pack.js'></script>
<link href="${ctxStatic}/fullcalendar/css/fullcalendar2.css" rel="stylesheet">
<link href="${ctxStatic}/fullcalendar/fullcalendar.print.css" rel="stylesheet">
<script src="${ctxStatic}/fullcalendar/js/fullcalendar.js"></script>
<meta name="decorator" content="default"/>




<!--
	说明：需要整合农历节气和节日，引入fullcalendar.js fullcalendar2.css
	不需要则引入：fullcalendar.min.js fullcalendar.css
-->

<script type="text/javascript">
$(function() {
	//页面加载完初始化日历 
	$('#calendar').fullCalendar({
		//设置日历头部信息
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		firstDay:1,//每行第一天为周一 
        editable: true,//启用拖动 
		events: '${ctx}/iim/myCalendar/findList',
		//点击某一天时促发
		dayClick: function(date, allDay, jsEvent, view) {
			var selDate =$.fullCalendar.formatDate(date,'yyyy-MM-dd');
			$.fancybox({
				'type':'ajax',
				'href':'${ctx}/iim/myCalendar/addform?date='+selDate
			});
    	},
		//单击事件项时触发 
        eventClick: function(calEvent, jsEvent, view) { 
           $.fancybox({ 
                'type':'ajax', 
                'href':'${ctx}/iim/myCalendar/editform?id='+calEvent.id 
           }); 
        },
		
		//拖动事件 
		eventDrop: function(event,dayDelta,minuteDelta,allDay,revertFunc) { 
        	$.post("${ctx}/iim/myCalendar/drag",{id:event.id,daydiff:dayDelta, minudiff:minuteDelta,allday:allDay},function(msg){ 
            	if(msg!=1){ 
                	alert(msg); 
                	revertFunc(); //恢复原状 
            	} 
        	}); 
    	},
		
		//日程事件的缩放
		eventResize: function(event,dayDelta,minuteDelta,revertFunc) { 
    		$.post("${ctx}/iim/myCalendar/resize",{id:event.id,daydiff:dayDelta,minudiff:minuteDelta},function(msg){ 
        		if(msg!=1){ 
            		alert(msg); 
            		revertFunc(); 
        		} 
    		}); 
		},
		
		selectable: true, //允许用户拖动 
		select: function( startDate, endDate, allDay, jsEvent, view ){ 
	    	var start =$.fullCalendar.formatDate(startDate,'yyyy-MM-dd'); 
	    	var end =$.fullCalendar.formatDate(endDate,'yyyy-MM-dd'); 
	    	$.fancybox({ 
	        	'type':'ajax', 
	        	'href':'${ctx}/iim/myCalendar/addform?date='+start+'&end='+end 
	    	}); 
		} 
	});
});

$('#todotab a').click(function (e) {
  e.preventDefault()
  $(this).tab('show')
})
</script>
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content">
    
        <div class="row animated fadeInDown">

			<div class="col-sm-6">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>待办事项 </h5>
                    </div>
                    <div class="ibox-content" style="height:300px;">
						<div>
						  <ul class="nav nav-tabs" role="tablist">
						    <li role="presentation" class="active"><a href="#tab1" aria-controls="tab1" role="tab" data-toggle="tab">待办 <span class="badge" style="color:white;background-color:red;">${todocount }</span></a></li>
						    <li role="presentation"><a href="#tab2" aria-controls="tab2" role="tab" data-toggle="tab">通知</a></li>
						    <li role="presentation"><a href="#tab3" aria-controls="tab3" role="tab" data-toggle="tab">消息 <span class="badge" style="color:white;background-color:red;">${msgcount }</span></a></li>
						  </ul>
						
						  <div class="tab-content">
						    <div role="tabpanel" class="tab-pane active" id="tab1" style="padding-bottom:20px;">
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
									<c:forEach items="${todopage.list}" var="todo">
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
								<table:page page="${todopage}"></table:page>
						    </div>
						    <div role="tabpanel" class="tab-pane" id="tab2" style="padding-bottom:20px;">
						    	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
									<thead>
										<tr>
											<th> <input type="checkbox" class="i-checks"></th>
											<th  class="sort-column title">标题</th>
											<th  class="sort-column is_top">创建人</th>
											<th  class="sort-column is_top">创建时间</th>
										</tr>
									</thead>
									<tbody>
									<c:forEach items="${noticepage.list}" var="notice">
										<tr>
											<td> <input type="checkbox" id="${notice.id}" class="i-checks"></td>
											<td>
												<a  href="${ctx}/sys/notice/view?id=${notice.id}">
													${notice.title}
												</a>
											</td>
											<td>
												${notice.createBy.name}
											</td>
											<td>
												<fmt:formatDate value="${notice.createDate}" type="both" dateStyle="full"/>
											</td>
										</tr>
									</c:forEach>
									</tbody>
								</table>
								
									<!-- 分页代码 -->
								<table:page page="${noticepage}"></table:page>
						    </div>
						    <div role="tabpanel" class="tab-pane" id="tab3" style="padding-bottom:20px;">
						    	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
									<thead>
										<th> <input type="checkbox" class="i-checks"></th>
										<th  class="sort-column msgType">消息类型</th>
										<th  class="sort-column msgContent">消息内容</th>
										<th>操作</th>
									</thead>
									<tbody>
									<c:forEach items="${msgpage.list}" var="tMsg">
										<tr>
											<td> <input type="checkbox" id="${tMsg.id}" class="i-checks"></td>
											<td>
												${fns:getDictLabel(tMsg.msgType, 'sys_msg_type', "无")}
											</td>
											<td>
												<c:if test="${tMsg.isRead == 0}"><div style="width:8px;height:8px;margin-top:5px;border-radius:100%;background-color:red;float:left;"></div></c:if>${tMsg.msgContent}
											</td>
											<td>
												<a href="${ctx}/sys/tMsg/deal?id=${tMsg.id}" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 处理</a>
											</td>
										</tr>
									</c:forEach>
									</tbody>
								</table>
								
									<!-- 分页代码 -->
								<table:page page="${msgpage}"></table:page>
						    </div>
						  </div>
						
						</div>
                    </div>
                </div>
            </div>
			
            
            <div class="col-sm-6">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>图文新闻 </h5>
                    </div>
                    <div class="ibox-content">
						<div>
						  <ul class="nav nav-tabs" role="tablist">
						 
						  <c:forEach items="${fns:getDictList('sys_notice_column')}" var="tab" varStatus="status">
						  <li role="presentation" <c:if test="${status.index==0 }"> class="active"</c:if>  ><a href="#tab${status.index+5}" aria-controls="tab${status.index+5}" role="tab" data-toggle="tab">${tab.label }</a></li>
						  </c:forEach>
						  </ul>
						  
						  
						 
						  <div class="tab-content">
						  	<c:forEach items="${tabpage}" var="tablist" varStatus="status">
						  
						    <div role="tabpanel" class="tab-pane <c:if test="${status.index==0 }"> active</c:if>" id="tab${status.index+5}" style="padding-bottom:20px;">
						    	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
									<thead>
										<tr>
											<th> <input type="checkbox" class="i-checks"></th>
											<th  class="sort-column title">标题</th>
											<th  class="sort-column is_top">创建人</th>
											<th  class="sort-column is_top">创建时间</th>
										</tr>
									</thead>
									<tbody>
									<c:forEach items="${tablist.list}" var="notice">
										<tr>
											<td> <input type="checkbox" id="${notice.id}" class="i-checks"></td>
											<td>
												<a  href="${ctx}/sys/notice/view?id=${notice.id}">
													${notice.title}
												</a>
											</td>
											<td>
												${notice.createBy.name}
											</td>
											<td>
												<fmt:formatDate value="${notice.createDate}" type="both" dateStyle="full"/>
											</td>
										</tr>
									</c:forEach>
									</tbody>
								</table>
									<!-- 分页代码 -->
								<table:page page="${tablist}"></table:page>
						    </div>
						 </c:forEach>
						    
						    
						    
						    
						    
						  </div>
						</div>
                    </div>
                </div>
            </div>
			
            <div class="col-sm-6">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>我的日程 </h5>
                    </div>
                    <div class="ibox-content">
                        <div id="calendar"></div>
                    </div>
                </div>
            </div>
            
        </div>
    </div>

</body>
</html>