<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="base" value="${pageContext.request.contextPath}" />
<!--[if IE 8]>    <html class="no-js ie8 ie" lang="en"> <![endif]-->
<!--[if IE 9]>    <html class="no-js ie9 ie" lang="en"> <![endif]-->
<!--[if gt IE 9]><!-->  <html class="no-js" lang="en">  <!--<![endif]-->
  <head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="${base}/assets/js/jquery-easyui-1.4/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="${base}/assets/js/jquery-easyui-1.4/themes/metro-blue/easyui.css" />
	<link type="text/css" rel="stylesheet" href="${base}/assets/css/fontawesome/css/font-awesome.min.css" />
	<link type="text/css" rel="stylesheet" href="${base}/assets/css/fontawesome/css/font-awesome-ie7.min.css" />
	<link type="text/css" rel="stylesheet" href="${base}/assets/css/layout/styles.css" />
	<!--[if lt IE 9]>
	<link type="text/css" rel="stylesheet" href="${base}/assets/css/layout/styles-ie.css" />
	<![endif]-->
	<script type="text/javascript" src="${base}/assets/js/jquery-easyui-1.4/jquery-1.8.0.min.js" ></script>
	<script type="text/javascript" src="${base}/assets/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${base}/assets/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${base}/assets/js/jQuery-slimScroll/jquery.slimscroll.js" ></script>
	<script type="text/javascript" src="${base}/assets/js/App.js" ></script>
	<script type="text/javascript" src="${base}/static/js/config.js" ></script>
    <script type="text/javascript">
    	var resId="${param.resId}";
    	console.log("resId"+resId);
    	if(resId!=""){
    		initAppParams.resId=resId;
    	}
		$(document).ready(function(){
			App.init(initAppParams);
			$.App = App;
	 	});
    </script>
  </head>
  
  <body class="easyui-layout" data-options="fit:true,border:false">
    <div class="layout-north" data-options="region:'north',split:false,border:false">
    	<%@ include file="header.jsp"%>
    </div>
    <div class="layout-west" data-options="region:'west',split:false">
    	<%@ include file="left.jsp"%>
    </div>
    <div class="layout-center" data-options="region:'center',split:false,border:false">
    	<!-- 
    	<div id="ddd" style="width:100%; height:100%;"></div>
    	 -->
    	<div id="tabs" class="easyui-tabs" fit="true" data-options="tools:'#tab-tools'" style="overflow: hidden;">
   		
		</div>
    	<!-- 
    	<div class="easyui-layout" fit="true">
    		<div data-options="region:'center',split:false,border:false">
    		</div>
			<div data-options="region:'south',split:false," style="height:30px;border-left: none; border-bottom: none; border-right: none;">
	    		<%@ include file="footer.jsp"%>
	    	</div>
    	</div>
    	-->
    </div>
    <div id="sidebar-expand" class="sidebar-expand">
		<i class="ace-icon fa fa-angle-double-right"></i>
	</div>
  </body>
</html>
