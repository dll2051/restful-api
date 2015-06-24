<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<div id="dictionaryItemPanel"  class="easyui-panel" data-options="noheader:true,fit:true" style="height: 100%;width: 100%;padding: 5px">
	<form id="dictionaryItemForm" method="post">	
		<input type="hidden" name="id" id="itemId" value=""/>
		<input type="hidden" name="active" id="itemActive" value="1"/>
		<input type="hidden" name="dictionary.id" id="dictionaryDotId" value="${param.dictionaryId}"/>
		<input type="hidden" name="dictionaryId" id="dictionaryId" value="${param.dictionaryId}"/>
		<input type="hidden" name="dictionaryCode" id="hiddenDictionaryCode" value="${param.dictionaryCode}"/>
		<input type="hidden" name="itemParentId" id="itemParentId" value="-1"/>
		<!-- 临时保存在树上选择的父ID，以对比是否切换了上级ID -->
		<input type="hidden" name="selectedItemParentId" id="selectedItemParentId" value="-1"/>
		<div id="dictionaryItemPanel" class="easyui-panel" data-options="title:'基本信息'" style="width:100%">
		<table>
				<tr height="25">
					<td width="20%" style="text-align:right">
					<span style="color: red" id="itemStarFlag1">*</span>所属字典：
					</td>
					<td width="30%" style="text-align:left">
						<input class="easyui-textbox" type="text" id=itemDictionaryName name="itemDictionaryName"  
						disabled="disabled"
						style="width: 170px"/>
					</td>
					<td width="20%" style="text-align:right"><span style="color: red" id="itemStarFlag2">*</span>上级字典项：</td>
					<td width="30%" style="text-align:left">
						<select class="easyui-combotree" id="itemParentName" name="itemParentName" style="width: 170px"></select>
					</td>
				</tr>
				<tr height="25">
					<td style="text-align:right"><span style="color: red" id="itemStarFlag3">*</span>编码：</td>
					<td style="text-align:left">
						<input class="easyui-textbox" type="text" id="itemCode" name="code" 
							data-options="required:true,validType:['length[1,15]'],missingMessage:'请输入编码'" maxlength="20"
							style="width: 170px"/>
					</td>
					<td style="text-align:right"><span style="color: red" id="itemStarFlag4">*</span>名称：</td>
					<td style="text-align:left">
						<input class="easyui-textbox" type="text" id="itemName" name="name" 
							data-options="
							required:true,
							validType:['length[1,15]'],
							missingMessage:'请输入名称'" style="width: 170px"/>
					</td>
				</tr>
				<tr height="25">
					<td style="text-align:right">拼音码：</td>
					<td style="text-align:left">
						<input class="easyui-textbox" type="text" id="pinyin" name="pinyin" data-options="
						validType:['code','length[0,50]'],editable:false" style="width: 170px"/>
					</td>
					<td style="text-align:right">五笔码：</td>
					<td style="text-align:left">
						<input class="easyui-textbox" type="text" id="wubi" name="wubi" data-options="
						validType:['code','length[0,50]'],editable:false" style="width: 170px"/>
					</td>
				</tr>
				<tr height="25">
					<td style="text-align:right">顺序：</td>
					<td style="text-align:left">
						<input class="easyui-textbox" type="text" id="itemSort" name="sort" data-options="validType:[
							'integer',
							'length[0,10]'
							]" style="width: 170px"/> 
					</td>
					<td style="text-align:right">自定义：</td>
					<td style="text-align:left">
						<input type="radio" id="isCustom1" name="isCustom" value="1"  checked/>是
						<input type="radio" id="isCustom0" name="isCustom" value="0"/>否
					</td>
				</tr>
				<tr height="25">
					<td style="text-align:right">默认选中：</td>
					<td style="text-align:left">
						<input type="radio" id="defaultSelected1" name="defaultSelected" value="1"/>是
						<input type="radio" id="defaultSelected0" name="defaultSelected" value="0" checked/>否 
					</td>
					<td style="text-align:right"><font id="itemActiveLabel"></font></td>
					<td style="text-align:left"><font id="itemActiveText"></font></td>
				</tr>
				<tr height="25">
					<td style="text-align:right">说明：</td>
					<td style="text-align:left" colspan="3">
						<input class="easyui-textbox" type="text" id="explain" name="explain" 
						data-options="validType:{length:[0,30]}"
							style="width:460px"/>
					</td>
				</tr>
				<tr height="60">
					<td style="text-align:right">备注：</td>
					<td style="text-align:left" colspan="3">
						<textarea class="easyui-textbox" id="itemComments" name="itemComments" style="height:60px;width:460px" 
						data-options="multiline:true,validType:{length:[0,65]}"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<div style="height:2px;"></div>
		<table id="itemPropertyDatagrid" style="width: 100%"></table>
	</form>
</div>