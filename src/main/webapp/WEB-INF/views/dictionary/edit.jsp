<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<div id="dictionaryPanel" class="easyui-panel" data-options="noheader:true" style="height: 100%;width: 100%">
	<form id="dictionaryWindowForm" method="post">
		<input type="hidden" name="id" id="id"/>
		<input type="hidden" name="active" id="dictionaryActive" value="1"/>
		<input type="hidden" name="parentId" id="parentId"/>
		<!-- 临时保存在树上选择的父ID，以对比是否切换了上级ID -->
		<input type="hidden" name="selectedParentId" id="selectedParentId"/>
		<!-- 所属的类型的code，值可能是国标、地标、卫标、自定义等 等，取决于顶级节点的code-->
		<input type="hidden" name="typeCode" id="typeCode"/>
		<table>
			<tr height="25">
				<td width="15%" style="text-align:right"><span style="color: red" id="dictionaryStarFlag1">*</span>字典编码：</td>
				<td width="35%" style="text-align:left">
					<input class="easyui-textbox" type="text" id="dictionaryCode" name="code" maxlength="20" style="width: 120px"/>
				</td>
				<td width="15%" style="text-align:right"><span style="color: red" id="dictionaryStarFlag2">*</span>字典名称：</td>
				<td width="35%" style="text-align:left">
					<input class="easyui-textbox" type="text" id="dictionaryName" name="name" 
						data-options="
						required:true,
						validType:['length[1,15]'],
						missingMessage:'请输入字典名称'
						" style="width: 120px"/>
				</td>
			</tr>
			<tr height="25">
				<td width="15%" style="text-align:right">拼音码：</td>
				<td width="35%" style="text-align:left">
					<input class="easyui-textbox" type="text" id="dictionaryPinyin" name="pinyin" 
						data-options="validType:['code','length[0,50]'],editable:false" style="width: 120px"/>
				</td>
				<td width="15%" style="text-align:right">五笔码：</td>
				<td width="35%" style="text-align:left">
					<input class="easyui-textbox" type="text" id="dictionaryWubi" name="wubi" data-options="
						validType:['code','length[0,50]'],editable:false" style="width: 120px"/>
				</td>
			</tr>
			<tr height="25">
				<td width="15%" style="text-align:right"><span style="color: red" id="dictionaryStarFlag3">*</span>上级字典：</td>
				<td width="35%" style="text-align:left">
					<select class="easyui-combotree" id="parentName" name="parentName" style="width:120px" 
					data-options="required:true,missingMessage:'请选择上级字典'">
					</select>
				</td>
				<td width="15%" style="text-align:right">标准号：</td>
				<td width="35%" style="text-align:left">
					<input class="easyui-textbox" type="text" id="standardNo" name="standardNo" data-options="validType:{
						length:[0,20]
						}" style="width: 120px"/>
				</td>
			</tr>
			<tr height="25">
				<td width="15%" style="text-align:right"><span style="color: red" id="dictionaryStarFlag4">*</span>使用范围：</td>
				<td width="35%" style="text-align:left">
					<select class="easyui-combobox" id="useRange" name="useRange" style="width: 120px" data-options="required:true,panelHeight:'auto',editable:false,missingMessage:'请选择使用范围'">
						<option value="1">所有</option>
						<option value="2">卫生</option>
						<option value="3">医疗</option>
					</select>
				</td>
				<td width="15%" style="text-align:right">字典来源：</td>
				<td width="35%" style="text-align:left">
					<input class="easyui-textbox" type="text" id="source" name="source" data-options="validType:{
						length:[0,15]}" style="width: 120px"/>
				</td>
			</tr>
			<tr height="25">
				<td width="15%" style="text-align:right">允许编辑：</td>
				<td width="35%" style="text-align:left">
					<input type="radio" name="isModify" value="1" checked/>是
					<input type="radio" name="isModify" value="0"/>否
				</td>
				<td width="15%" style="text-align:right">允许缓存：</td>
				<td width="35%" style="text-align:left">
					<input type="radio" name="isCache" value="1" checked/>是
					<input type="radio" name="isCache" value="0"/>否
				</td>
			</tr>
			<tr height="25">
				<td width="15%" style="text-align:right">顺序：</td>
				<td width="35%" style="text-align:left">
					<input class="easyui-textbox" type="text" id="sort" name="sort" data-options="validType:[
						'integer',
						'length[0,10]'
						]" style="width: 120px"/> 
				</td>
				<td width="15%" style="text-align:right"><font id="activeLabel"></font></td>
				<td width="35%" style="text-align:left"><font id="activeText"></font></td>
			</tr>
			<tr height="60">
				<td width="15%" style="text-align:right">备注：</td>
				<td width="35%" style="text-align:left" colspan="3">
					<textarea class="easyui-textbox" id="comments" name="comments" style="height:60px;width:450px" 
					data-options="multiline:true,
					validType:{length:[0,65]},
					missingMessage:'请输入备注'"></textarea>
				</td>
			</tr>
		</table>
	</form>
</div>