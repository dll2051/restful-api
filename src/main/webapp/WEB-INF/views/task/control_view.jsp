<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/project.jsp"%>
<html>
<head>
	<title></title>
</head>
<body>
	<div id="tt" class="easyui-tabs" tools="#tab-tools">
			<div title="操作栏">
				<input id="btn_interface" type="button" value="开始获取数据"/> 
				<input id="btn_go" type="button" value="开始分析"/>
				<div id="js_id">
				</div> 
			</div>
			<div title="详细" style="overflow:hidden">
				<iframe id="ck_tab" scrolling="auto" frameborder="0"  src="" style="width:880px;height:370px;"></iframe>
			</div>
	</div>
    <script type="text/javascript">
    	var interfaceID = "";
    	$("#btn_interface").click(function(){
    		if("开始获取数据"==$("#btn_interface").val()){
    			$("#btn_interface").val("停止获取数据");
    			interfaceID = setInterval(interfaceMessageAjax,10000);//时间间隔5秒
    		}else{
    			$("#btn_interface").val("开始获取数据");
    			clearInterval(interfaceID);
    		}
    	});
    	var goID = "";
    	$("#btn_go").click(function(){
    		if("开始分析"==$("#btn_go").val()){
    			$("#btn_go").val("停止分析");
    			goID = setInterval(saleMessageAjax,5000);//时间间隔5秒
    		}else{
    			$("#btn_go").val("开始分析");
    			clearInterval(goID);
    		}
    	});
    	var count = 0;
    	function interfaceMessageAjax(){
    		$.ajax({
       				type:"get",
       				url:"interface.action",
       				async:false,
       				dataType:"json",
       				success:function(data){
       					count += parseInt(data.count);
       					$("#js_id").html("已经获取了<strong>"+count+"</strong>条数据");	
       				}
    		});
    	}
    	function saleMessageAjax(){
    		$.ajax({
       				type:"get",
       				url:"go.action",
       				async:false,
       				dataType:"json",
       				success:function(data){
       					var id = data.id;
       					if("error"==id){
       						$("#btn_go").val("开始分析");
       						clearInterval(goID);
       					}else{
       						try{
       							eval(data.js);
       							var url = newHref.replace("'","");
               			    	$("#ck_tab").attr("src", "toSaleSave.action?id=" + id+"&url="+url);
       					  	}
       						catch(err){
       					  	//在这里处理错误
       							$("#ck_tab").attr("src", "toSaleSave.action?id=" + id+"&error=error");
       					  	}
       					}
       				}
    		});
    	}
    </script>
</body>
</html>

