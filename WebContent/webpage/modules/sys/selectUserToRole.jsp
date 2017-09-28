<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<meta name="decorator" content="blank"/>
	<%@include file="/webpage/include/treeview.jsp" %>
	<script type="text/javascript">
	
		var officeTree;
		var selectedTree;//zTree已选择对象
		
		// 初始化
		$(document).ready(function(){
			officeTree = $.fn.zTree.init($("#officeTree"), setting, officeNodes);
			selectedTree = $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
		});

		var setting = {view: {selectedMulti:false,nameIsHTML:true,showTitle:false,dblClickExpand:false},
				data: {simpleData: {enable: true}},
				callback: {onClick: treeOnClick}};
		
		var officeNodes=[
	            <c:forEach items="${officeList}" var="office">
	            {id:"${office.id}",
	             pId:"${not empty office.parent?office.parent.id:0}", 
	             name:"${office.name}"},
	            </c:forEach>];
	
		var pre_selectedNodes =[
   		        <c:forEach items="${userList}" var="user">
   		        {id:"${user.id}",
   		         pId:"0",
   		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
   		        </c:forEach>];
		
		var selectedNodes =[
		        <c:forEach items="${userList}" var="user">
		        {id:"${user.id}",
		         pId:"0",
		         name:"<font color='red' style='font-weight:bold;'>${user.name}(${user.no})</font>"},
		        </c:forEach>];
		
		var pre_ids = "${selectIds}".split(",");
		var ids = "${selectIds}".split(",");
		
		//点击选择项回调
		function treeOnClick(event, treeId, treeNode, clickFlag){
			$.fn.zTree.getZTreeObj(treeId).expandNode(treeNode);
			if("officeTree"==treeId){
				var utype = $("#usertype option:selected").val();
				$.get("${ctx}/sys/role/users?officeId=" + treeNode.id + "&userType=" + utype, function(userNodes){
					$.fn.zTree.init($("#userTree"), setting, userNodes);
				});
			}
			if("userTree"==treeId){
				//alert(treeNode.id + " | " + ids);
				//alert(typeof ids[0] + " | " +  typeof treeNode.id);
				if($.inArray(String(treeNode.id), ids)<0){
					selectedTree.addNodes(null, treeNode);
					ids.push(String(treeNode.id));
				}
			};
			if("selectedTree"==treeId){
				if($.inArray(String(treeNode.id), pre_ids)<0){
					selectedTree.removeNode(treeNode);
					ids.splice($.inArray(String(treeNode.id), ids), 1);
				}else{
					top.$.jBox.tip("角色原有成员不能清除！", 'info');
				}
			}
		};
		function clearAssign(){
			var submit = function (v, h, f) {
			    if (v == 'ok'){
					var tips="";
					if(pre_ids.sort().toString() == ids.sort().toString()){
						tips = "未给角色【${role.name}】分配新成员！";
					}else{
						tips = "已选人员清除成功！";
					}
					ids=pre_ids.slice(0);
					selectedNodes=pre_selectedNodes;
					$.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
			    	top.$.jBox.tip(tips, 'info');
			    } else if (v == 'cancel'){
			    	// 取消
			    	top.$.jBox.tip("取消清除操作！", 'info');
			    }
			    return true;
			};
			tips="确定清除角色【${role.name}】下的已选人员？";
			top.$.jBox.confirm(tips, "清除确认", submit);
		};
		
		function search() {
			var url = "${ctx}/sys/role/searchusers?";
			var param = "";
			if($("#uname").val() != null && $("#uname").val() != "") {
				param += "name=" + $("#uname").val();
			}
			if($("#ucode").val() != null && $("#ucode").val() != "") {
				param += "&no=" + $("#ucode").val();
			}
			if($("#ulogin").val() != null && $("#ulogin").val() != "") {
				param += "&loginName=" + $("#ulogin").val();
			}
			var utype = $("#utype option:selected").val();
			if(utype != null && utype != "") {
				param += "&userType=" + utype;
			}
			/* $.get(url, function(userNodes){
				$.fn.zTree.init($("#userTree"), setting, userNodes);
			}); */
			$.ajax({
                async : false,
                cache : false,
                type : 'post',
                data : param,
                url : url + new Date(),
                success : function(userNodes) {
                	$.fn.zTree.init($("#userTree"), setting, userNodes);
                }
            });
		}
	</script>
</head>
<body>

	<div class="col-sm-12" style="margin-top:10px;margin-bottom:20px;">
		<div class="col-sm-4">
			<label style="padding-top:6px;height:100%;">用户姓名：</label>
			<input id="uname" type="text" class="form-control input-sm" style="width:70%;float:right;"/>
		</div>
		<div class="col-sm-4">
			<label style="padding-top:6px;height:100%;">用户工号：</label>
			<input id="ucode" type="text" class="form-control input-sm" style="width:70%;float:right;"/>
		</div>
		<div class="col-sm-4">
			<label style="padding-top:6px;height:100%;">用户类型：</label>
			<select id="utype" name="usertypes" class="form-control" style="width:70%;float:right;">
				<option value=""></option>
				<c:forEach items="${fns:getDictList('sys_user_type')}" var="usertype">
					<option value="${usertype.value }">${usertype.label }</option>
				</c:forEach>
			</select>
		</div>
		<br><br>
		<div class="col-sm-4">
			<label style="padding-top:6px;height:100%;">用户账号：</label>
			<input id="ulogin" type="text" class="form-control input-sm" style="width:70%;float:right;"/>
		</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
		</div>
	</div>

	<div id="assignRole" class="row wrapper wrapper-content">
		<div class="col-sm-4" style="border-right: 1px solid #A8A8A8;">
			<span style="font-size:12px;">所在部门：</span>
		    <select id="usertype" name="usertype" class="form-control" style="width:50%;float:right;margin-top:-10px;">
				<option value="999999">请选择</option>
				<c:forEach items="${fns:getDictList('sys_user_type')}" var="usertype">
					<option value="${usertype.value }">${usertype.label }</option>
				</c:forEach>
			</select>  
			<div id="officeTree" class="ztree"></div>
		</div>
		<div class="col-sm-4">
			<p>待选人员：</p>
			<div id="userTree" class="ztree"></div>
		</div>
		<div class="col-sm-4" style="padding-left:16px;border-left: 1px solid #A8A8A8;">
			<p>已选人员：</p>
			<div id="selectedTree" class="ztree"></div>
		</div>
	</div>
</body>
</html>
