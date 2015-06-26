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
				<div id="js_id">
				</div> 
			</div>
	</div>
    <script type="text/javascript">
    	
      var count = ${params}.count;
		$("#js_id").html("已经获取了<strong>"+count+"</strong>条数据");	
    </script>
</body>
</html>

