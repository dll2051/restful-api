<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/project.jsp"%>
<html>
<head>
	<title></title>
</head>
<body>
	<div id="tt" class="easyui-tabs" tools="#tab-tools">
			<div title="详细" style="overflow:hidden">
				<iframe id="ck_tab" scrolling="auto" frameborder="0"  src="" style="width:880px;height:370px;"></iframe>
			</div>
	</div>
    <script type="text/javascript">
      var docid = ${params}.id;
      $(function () {
    	$("#ck_tab").attr("src", "toGwglDetail.action?docID=" + docid);
      });
    </script>
</body>
</html>

