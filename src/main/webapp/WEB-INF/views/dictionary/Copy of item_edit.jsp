<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; utf-8">
 <title>字典项管理</title>
  <%@include file="../common/project.jsp" %>
</head>
<body>
	<form id="dictionaryItemForm" method="post" style="width:100%;height: 100%;">		
	<input type="hidden" name="id" id="itemId" value=""/>
	<input type="hidden" name="active" id="active"/>
	<input type="hidden" name="dictionary.id" id="dictionaryDotId" value="${param.dictionaryId}"/>
	<input type="hidden" name="dictionaryId" id="dictionaryId" value="${param.dictionaryId}"/>
	<input type="hidden" name="dictionaryCode" id="hiddenDictionaryCode" value="${param.dictionaryCode}"/>
	<input type="hidden" name="parentId" id="parentId" value="-1"/>
	<table style="border:0px;">
		<tr id="itemToolbarTr" height="25">
			<td height="25" colspan="2" style="padding-left: 5px">
				<a href="javascript:void(0)" id="addItemButton" class="easyui-linkbutton" onclick="addItem('${param.dictionaryId}')">新增字典项</a>
				<a href="javascript:void(0)" id="addChildrenItemButton" class="easyui-linkbutton" data-options="disabled:true" onclick="addChildrenItem('${param.dictionaryId}')">新增子字典项</a>
				<a href="javascript:void(0)" id="editItemButton" class="easyui-linkbutton" data-options="disabled:true" onclick="editItem()">编辑</a>
				<a href="javascript:void(0)" id="changeActiveStatus1" class="easyui-linkbutton" data-options="disabled:true" onclick="changeActiveStatus(0)">停用</a>
				<a href="javascript:void(0)" id="changeActiveStatus0" class="easyui-linkbutton" data-options="disabled:true" onclick="changeActiveStatus(1)">启用</a>
			</td>
		</tr>
		<tr>
			<td style="padding-left: 5px;padding-top: 5px;vertical-align: top;width: 200px;height: 410px">
				<div id="dictionaryItemPanel" class="easyui-panel" data-options="title:'字典项'"
					 style="width:100%">
					<ul id="itemTree" style="width:180px;height:380px"/>
				</div>
			</td>
			<td style="padding-left: 5px;padding-top: 5px;vertical-align: top;height: 410px">
					<table id="dictionaryItemPanel" class="easyui-panel" data-options="title:'基本信息'" style="width:100%;height: 100%">
						<tr height="25">
							<td width="15%" style="text-align:right"><span style="color: red" id="itemStarFlag1">*</span>所属字典：</td>
							<td width="35%" style="text-align:left">
								<input class="easyui-textbox" type="text" id="dictionaryName" name="dictionaryName" value="${param.dictionaryName}" disabled="disabled"/>
							</td>
							<td width="15%" style="text-align:right"><span style="color: red" id="itemStarFlag2">*</span>上级名称：</td>
							<td width="35%" style="text-align:left">
								<input class="easyui-textbox" type="text" id="parentName" name="parentName" value="顶级" disabled="disabled"/>
							</td>
						</tr>
						<tr height="25">
							<td width="15%" style="text-align:right"><span style="color: red" id="itemStarFlag3">*</span>编码：</td>
							<td width="35%" style="text-align:left">
								<input class="easyui-textbox" type="text" id="itemCode" name="code" 
									data-options="required:true,validType:['length[1,15]'],missingMessage:'请输入编码'" maxlength="20"/>
							</td>
							<td width="15%" style="text-align:right"><span style="color: red" id="itemStarFlag4">*</span>名称：</td>
							<td width="35%" style="text-align:left">
								<input class="easyui-textbox" type="text" id="itemName" name="name" 
									data-options="
									required:true,
									validType:['length[1,15]'],
									missingMessage:'请输入名称'"/>
							</td>
						</tr>
						<tr height="25">
							<td width="15%" style="text-align:right">拼音码：</td>
							<td width="35%" style="text-align:left">
								<input class="easyui-textbox" type="text" id="pinyin" name="pinyin" data-options="
								validType:['code','length[0,50]'],editable:false"/>
							</td>
							<td width="15%" style="text-align:right">五笔码：</td>
							<td width="35%" style="text-align:left">
								<input class="easyui-textbox" type="text" id="wubi" name="wubi" data-options="
								validType:['code','length[0,50]'],editable:false"/>
							</td>
						</tr>
						<tr height="25">
							<td width="15%" style="text-align:right">顺序：</td>
							<td width="35%" style="text-align:left">
								<input class="easyui-textbox" type="text" id="itemSort" name="sort" data-options="validType:[
									'integer',
									'length[0,10]'
									]"/> 
							</td>
							<td width="15%" style="text-align:right">自定义：</td>
							<td width="35%" style="text-align:left">
								<input type="radio" id="isCustom1" name="isCustom" value="1"  checked/>是
								<input type="radio" id="isCustom0" name="isCustom" value="0"/>否
							</td>
						</tr>
						<tr height="25">
							<td width="15%" style="text-align:right">默认选中：</td>
							<td width="35%" style="text-align:left">
								<input type="radio" id="defaultSelected1" name="defaultSelected" value="1" disabled="disabled"/>是
								<input type="radio" id="defaultSelected0" name="defaultSelected" value="0" checked/>否 
							</td>
							<td width="15%" style="text-align:right"></td>
							<td width="35%" style="text-align:left">
							</td>
						</tr>
						<tr height="25">
							<td width="15%" style="text-align:right">说明：</td>
							<td width="35%" style="text-align:left" colspan="3">
								<input class="easyui-textbox" type="text" id="explain" name="explain" 
								data-options="validType:{length:[0,30]}"
									style="width:450px"/>
							</td>
						</tr>
						<tr height="60">
							<td width="15%" style="text-align:right">备注：</td>
							<td width="35%" style="text-align:left" colspan="3">
								<textarea class="easyui-textbox" id="itemComments" name="comments" style="height:60px;width:450px" 
								data-options="multiline:true,validType:{length:[0,65]}"></textarea>
							</td>
						</tr>
					</table>
				<div style="height:2px;"></div>
				<table id="itemPropertyDatagrid"></table>
			</td>
		</tr>
	</table>
	</form>
</body>
</html>