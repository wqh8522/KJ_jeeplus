<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>调查问卷管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.sttitle{
			font-size:16px;
			color:#000;
			font-weight:bold;
		}
		
		.stxx{
			font-size:16px;
			color:#000;
			padding-left:20px;
		}
	</style>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
		
	</script>
	<script src="${ctxStatic }/js/echarts.min.js"></script>
</head>

<body class="hideScroll gray-bg" style="height:100%;">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>${survey.name }</h5>
				<div class="ibox-tools">
				</div>
			</div>
		    
		    <div class="ibox-content">
		    	<div>
				  <ul class="nav nav-tabs" role="tablist">
				    <li role="presentation" class="active"><a href="#tab1" aria-controls="tab1" role="tab" data-toggle="tab">答卷概况</a></li>
				    <li role="presentation"><a href="#tab2" aria-controls="tab2" role="tab" data-toggle="tab">答卷详情</a></li>
				  </ul>
				
				  <div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="tab1" style="padding-bottom:20px;">
				    	<div id="main" style="width: 600px;height:400px;"></div>
				    </div>
				    <div role="tabpanel" class="tab-pane" id="tab2" style="padding-bottom:20px;">
				    	<table id="contentTable1" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
							<tbody>
								<input type="hidden" id="surveyid" value="${survey.id }"/>
								<c:forEach items="${survey.qusList }" var="qus" varStatus="status">
										<tr>
											<td class="sttitle" colspan="2">
												<c:if test="${qus.type eq '2' }">
													${status.index + 1 }、<span class="label label-primary">单选题</span> ${qus.title }
												</c:if>
												<c:if test="${qus.type eq '1' }">
													${status.index + 1 }、<span class="label label-danger">多选题</span> ${qus.title }
												</c:if>
												
												<input type="hidden" value="${qus.id }" class="qus" qustype="${qus.type }"/>
											</td>
										</tr>
										<c:forEach items="${qus.options }" var="option">
											<tr>
												<td class="stxx">
													&nbsp;&nbsp;&nbsp;&nbsp;
													${option.bh }、${option.option }
												</td>
												<td>
													<div class="progress">
													  <div class="progress-bar" role="progressbar" aria-valuenow="${option.bl }" aria-valuemin="0" aria-valuemax="100" style="width: ${option.bl }%;">
													    	${option.rs }（${option.bl }%）
													  </div>
													</div>
												</td>
											</tr>
										</c:forEach>
								</c:forEach>
							</tbody>
						</table>
				    </div>
				  </div>
				</div>
		    </div>
		</div>		
	</div> 
	<script src="${ctxStatic}/echarts-2.2.7/build/dist/echarts.js"></script>
	<script type="text/javascript">
		function cancel() {
			var url = "${ctx}/sys/tSurvey/mylist";
			window.open(url, "_self");
		}
		var myChart = echarts.init(document.getElementById('main'));

        // 指定图表的配置项和数据
        option = {
		    title : {
		        text: '${survey.name}-答卷情况',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        orient: 'vertical',
		        left: 'left',
		        data: ['已答卷','未答卷']
		    },
		    series : [
		        {
		            name: '答卷情况',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            data:[
		                {value:${readed}, name:'已答卷'},
		                {value:${unread}, name:'未答卷'}
		            ],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            }
		        }
		    ]
		};


        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        
        myChart.on('click', function (params) {
        	var url = "${ctx}/sys/tSurveyAnswer/userlist?";
            if(params.name == "已答卷") {
            	url += "wjid=" + $("#surveyid").val() + "&stid=1";
            }
			if(params.name == "未答卷") {
            	url += "wjid=" + $("#surveyid").val() + "&stid=2";
            }
			window.open(url, "_self")
        });
	</script> 
</body>
</html>