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
		    
				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
					<tbody>
						<c:forEach items="${typeList }" var="type">
					   		<tr>
								<td class="width-15 active"><label class="pull-right">${type.name }：</label></td>
								<td class="width-35">
									<input type="hidden" value="${type.id }" class="id"/>
									<input type="text" class="form-control single" placeholder="请输入单选题个数，当前题库共${type.singlenum }道" />
								</td>
								<td class="width-35">
									<input type="text" class="form-control multi" placeholder="请输入多选题个数，当前题库共${type.multinum }道" />
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
			</div>
		</div>
	</div>
	<script type="text/javascript">
		
		function getdata() {
			var ids = $(".id");
			var idtext = "";
			var singletext= "";
			var multitext = "";
			for(var i=0; i < ids.length; i ++) {
				var id = ids[i];
				idtext += $(id).val() + ",";
			}
			
			var single = $(".single");
			for(var i=0; i < single.length; i ++) {
				var id = single[i];
				var text = $(id).val();
				if(text == null || text == "") {
					singletext += "0,";
				} else {
					singletext += text + ",";
				}
				
			}
			
			var multi = $(".multi");
			for(var i=0; i < multi.length; i ++) {
				var id = multi[i];
				var text = $(id).val();
				if(text == null || text == "") {
					multitext += "0,";
				} else {
					multitext += text + ",";
				}
			}
			idtext = idtext.substring(0, idtext.length - 1);
			singletext = singletext.substring(0, singletext.length - 1);
			multitext = multitext.substring(0, multitext.length - 1);
			return idtext + "@" + singletext + "@" + multitext;
		}
	
	</script>
</body>
</html>