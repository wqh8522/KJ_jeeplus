<!doctype html> 
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>新闻通知管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/css/cropper.min.css" />
	<script charset="utf-8" src="${ctxStatic}/js/cropper.min.js"></script>
</head>
<body class="hideScroll gray-bg" style="height:100%;">
	<div class="wrapper wrapper-content">
		<div class="ibox">
		    <div class="ibox-content">
		    	<center >
			  		<div style="width:75%;">
			  			<img src="" id="img" style="width:100%;">
			  			<input id="imgval" type="hidden"/>
			  		</div>
			  	</center>
		    </div>
		</div>		
	</div> 
	<script type="text/javascript">
		$(function() {
			var src = parent.$('#img').attr("src");
		  	$("#img").attr("src", src);
			var img = document.getElementById("img");
			cropper = new Cropper(img, {
				aspectRatio: 1.7 / 1,
				rotatable: true,
				minCanvasWidth: 200,
				minCanvasHeight: 200,
				minContainerWidth: 200,
				minContainerHeight: 200,
				crop: function(data) {

				}
			});
		});
		
		function getImg() {
			var resImg = cropper.getCroppedCanvas().toDataURL();
			return resImg;
		}
	
	</script>  
</body>
</html>