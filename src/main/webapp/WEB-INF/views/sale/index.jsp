<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<html>
<head>
 <title>字典管理</title>
 <%@ include file="../common/project.jsp" %>
  <script type="text/javascript" src="${base}/static/js/dictionary/dictionary.js"></script>
  <script type="text/javascript" src="${base}/static/js/dictionary/saleDetail.js"></script>
  <script type="text/javascript" src="${base}/static/js/common/pinyin.js"></script>
 <%--  <script type="text/javascript" src="${base}/static/js/common/tool.js"></script> --%>
  <script type="text/javascript" src="${base}/assets/js/JSON-js-master/json2.js"></script>
 <script type="text/javascript">
 	//初始化页面数据
	$(document).ready(function(){
	 	//初始化商品Datagrid
	 	initSaleDetailDatagrid();
	 	//初始化窗口
	 	//initDialogs();
 		//初始化查询页面
 		//1.关键字
 		$("#search_keyword").textbox("textbox").focus();
 		$("#search_itemActive").combobox({
 			required:true,
 			panelHeight:'auto',
 			editable:false,
 			data:[{value:"",text:"所有"},{value:"1",text:"启用",selected:true},{value:"0",text:"停用"}],
 			onSelect:function(n){
 				$("#searchItemLinkButton").focus();
 			}
 		});
 		$("#searchTable").keydown(function(e){
 			if(e.keyCode==13){
 				$("#searchLinkButton").focus();
 				searchDictionary();
 			}
 		});
 		$("#searchItemTable").keydown(function(e){
 			if(e.keyCode==13){
 				$("#searchItemLinkButton").focus();
 				searchDictionaryItem();
 			}
 		});
 		
	});
 </script>
</head>
<body class="easyui-layout">
		<div data-options="region:'center',title:'',split:false,border:false"
			 style="width: 60%;height: 100%;padding: 5px">
			<div class="easyui-panel"
				data-options="title:'字典项',border:true,fit:true"
				style="width: 100%;height: 100%;overflow:none">
				<!-- 主操作区 -->
				<!-- 搜索 -->
				<table id="searchItemTable">
					<tr style="height: 30px">
			   			<td style="width:55px;min-width:55px;text-align:right">关键字：</td>
			   			<td style="width:100px;min-width:100px;">
			   				<input class="easyui-textbox" type="text" id="search_itemKeyword" name="keyword" style="width: 100px"></input>
			   			</td>
			   			<td style="width:60px;min-width:60px;text-align:right">状态：</td>
			   			<td style="width:100px;min-width:100px;">
			   				<select class="easyui-combobox" id="search_itemActive"  name="active" style="width: 100px">
							</select>
						</td>
			   			<td style="min-width:170px;" align="left">
			   				&nbsp;&nbsp;&nbsp;
			   				<a id="searchItemLinkButton" class="easyui-linkbutton search" style="margin-right:10px;width:50px;" href="javascript:searchDictionaryItem();">查询</a>
			   				<a class="easyui-linkbutton search" style="margin-right:10px;width:50px;"  href="javascript:resetSearchDictionaryItem();">重置</a>
			   				&nbsp;
			   				<!-- a class="easyui-linkbutton search"  href="javascript:resetSearchDictionary();">重置</a -->
			   			</td>
			   		</tr>
				</table>
				<!-- 搜索结束 -->
				<!-- 列表 -->
				<div style="width: 100%;height: 94%;overflow:none">
					<table id="saleDetailTableId"></table>
				</div>
				<!-- 列表结束 -->
			</div>
			<!-- 主操作区结束 -->
	</div>
</body>
</html>