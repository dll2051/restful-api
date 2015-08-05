//工具栏
var selectedDictionaryItemId=null;
var editRowIndex=0;
var readOnly=false;
//是否是展开节点操作，用于判断是否分页
//var isCollapseNode=false;
//字典项treegrid工具栏
var itemToolbar = [{
 	id:"addItemButton",
	text:'新增',
	iconCls:'icon-add',
	disabled:true,
	handler:openSaveDictionaryItemWindow
},
{
	id:"editItemButton",
	text:'编辑',
	iconCls:'icon-edit',
	handler:function(){
		openUpdateDictionaryItemWindow(true,null);
	},
	disabled:true
},{
	id:"stopItemButton",
	text:'停用',
	iconCls:'icon-cancel',
	handler:function(){
		changeItemActiveStatus(0);
	},
	disabled:true
},{
	id:"activeItemButton",
	text:'启用',
	iconCls:'icon-ok',
	handler:function(){
		changeItemActiveStatus(1);
	},
	disabled:true
}];
//字典项扩展属性工具栏
var itemPropertyToolbar = [
    {
    	id:'addPropertyButton',
		text:'新增',
		iconCls:'icon-add',
		disabled:true,
		handler:addItemPropertity
	},{
		id:'removePropertyButton',
		text:'删除',
		iconCls:'icon-remove',
		disabled:true,
		handler:removeItemProperty
	}];
/**
 * 初始化商品Datagrid
 */
function initSaleDetailDatagrid(){
	 $("#saleDetailTableId").datagrid({
		title:'', //标题
		method:'post',
		singleSelect:false, //多选
		fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
		fit:true,
		//height:345, //高度
       // width:729,	
		striped: true, //奇偶行颜色不同
		//collapsible:true,//可折叠 
		url:'../saleDetail/querySaleDetailList.action',//数据来源
		cache:false,
		idField:"id",
		//treeField:"name",
		//sortName:"code",
		//sortOrder:"asc",
		remoteSort:true,
		rownumbers:true, //显示行号
		pagination:true, //显示分页
		columns:[[ 
				 {field:'title',title:'标题',width:100,sortable:true},
                 {field:'detail',title:'明细',width:100,sortable:true},
                 {field:'createTime',title:'入库时间',width:50,sortable:true,
					  formatter: function(value,row,index){
					      return new Date(value).format("yyyy-MM-dd hh:mm:ss");
					  }
                 },
                 {field:'url',title:'商品获取链接地址',width:50,sortable:true},
                 {field:'source',title:'信息来源',width:50,align:"center",sortable:true},
                 {field:'mallname',title:'商城',width:50,align:"center",sortable:true}
             ]],
         toolbar:itemToolbar
	});
	//$("#addItemButton").checkPermission();
	//$("#editItemButton").checkPermission();
	//$("#stopItemButton").checkPermission();
	//$("#activeItemButton").checkPermission();
}
/**
 * 查询字典项
 */
function searchDictionaryItem(){
	selectedDictionaryItemId=null;
	isCollapseNode=false;
	var selected=$("#dictionaryTreegrid").treegrid("getSelected");
	var selectedInTree=$("#dictionaryTree").tree("getSelected");
	if(selected==null&&selectedInTree==null){
		$.messager.alert('提示',"请先选择一个字典类型再查询","info");
		return;
	}
	$("#dictionaryItemTreegrid").treegrid({
		pageNumber:1,
		queryParams:{id:null}
	});
	//$("#dictionaryItemTreegrid").treegrid("load");
	$('#addItemButton').linkbutton('enable');
	$('#editItemButton').linkbutton('disable');
	$('#stopItemButton').linkbutton('disable');
	$('#activeItemButton').linkbutton('disable');
}

/**
 * 重置查询字典项表单
 */
function resetSearchDictionaryItem(){
	$("#search_itemKeyword").textbox("setValue","");
	$("#search_itemActive").combobox("setValue","1");
	searchDictionaryItem();
}

/**
 * 加载数据
 */
function itemLoader(param,success,error){
	var selectedDictionary=$("#dictionaryTreegrid").treegrid("getSelected");
	if(selectedDictionary==null){
		success([]);
		return;
	}
	var t = $(this);
	var options = t.treegrid("options");
//	if (!options.url) {
//		return false;
//	}
	;
	var invokeParams=param;
	invokeParams.keyword=$.trim($("#search_itemKeyword").textbox("getValue"));
	invokeParams.active=$("#search_itemActive").combobox("getValue");
	invokeParams.dictionaryId=selectedDictionary.id;
	invokeParams.orderBy=options.sortName!=undefined?options.sortName:"sort",
	invokeParams.orderDir=options.sortOrder!=undefined?options.sortOrder:"desc";
	invokeParams.pageNo=options.pageNumber;
	invokeParams.pageSize=options.pageSize;
	//invokeParams.pagination=false;
	$.post("../dictionaryItem/queryItemTreeData.action",invokeParams,function(d){
	    	if(d.result="success"){
				success(d.data);
			}else{
				error.apply(this, d.message);
			}
		  })
	    .error(function(){
	    	error.apply(this, arguments);
		  });
}
/**
 * 排序
 */
function sortItem(sort, order){
	var t = $(this);
	t.treegrid({
		sortName:sort,
		sortOrder:order,
		queryParams:{id:null}
	});
	return false;
}
/**
 * 选择一条字典项
 * @param rowData
 */
function selectItem(rowData){
	if(selectedDictionaryItemId==null||selectedDictionaryItemId!=rowData.id){
		selectedDictionaryItemId=rowData.id;
		if(rowData.isCustom==0){
			$('#editItemButton').linkbutton('disable');
			$('#stopItemButton').linkbutton('disable');
			$('#activeItemButton').linkbutton('disable');
		}else{
			$('#editItemButton').linkbutton('enable');
			if(rowData.active==1){
				$('#stopItemButton').linkbutton('enable');
				$('#activeItemButton').linkbutton('disable');
			}else{
				$('#stopItemButton').linkbutton('disable');
				$('#activeItemButton').linkbutton('enable');
			}
		}
	}else{
		selectedDictionaryItemId=null;
		$('#editItemButton').linkbutton('disable');
		$('#stopItemButton').linkbutton('disable');
		$('#activeItemButton').linkbutton('disable');
		$("#dictionaryItemTreegrid").treegrid("unselectAll");
	}
}
/**
 * 初始化字典项窗口
 */
function initDictionaryItemWindow(){
	var itemNameInput=$("#itemName").textbox("textbox");
	itemNameInput.unbind("keyup").bind("keyup",function(){
		autoAppendPinyin($(this).val(),"pinyin","wubi");
	});
	$('#itemParentName').combotree({
		url:"../dictionaryItem/queryItemTreeData.action",
		valueField:"name",
		textField:"name",
		queryParams:{
			dictionaryId:$("#dictionaryTreegrid").treegrid("getSelected").id,
			selectTop:true
		},
		loader:defaultLoader,
		onSelect:function(node){
			$("#itemParentId").val(node.id);
		}
	});
	var selectedDictionary=$("#dictionaryTreegrid").treegrid("getSelected");
	$("#dictionaryId").val(selectedDictionary.id);
	$("#dictionaryDotId").val(selectedDictionary.id);
	$("#hiddenDictionaryCode").val(selectedDictionary.code);
	$("#dictionaryItemForm").form({});
	$("#itemPropertyDatagrid").datagrid({
		title:"扩展属性",
		height:150,
		idField:"code",
		fitColumns: true,
		columns:[[
				  {field:'code',title:'属性编码',width:175,
					  editor: {
	                        type: 'validatebox',
	                        options: {
	                            required: true,
	                            validType:{length:[1,20]},
	                            missingMessage:'请输入属性编码'
	                        }
	                    }
					},
               {field:'name',title:'属性名称',width:175,
						  editor: {
		                        type: 'validatebox',
		                        options: {
		                            required: true,
		                            validType:{length:[1,50]},
		                            missingMessage:'请输入属性名称'
		                        }
		                    }
						},
               {field:'value',title:'属性值',width:175,
							  editor: {
			                        type: 'validatebox',
			                        options: {
			                            required: true,
			                            validType:{length:[1,50]},
			                            missingMessage:'请输入属性值'
			                        }
			                    }
							}
           ]],
       toolbar:itemPropertyToolbar,
       onClickRow: onClickItemProperty
	});
}

 /**
  * 保存数据
  */
 function validateItemForm(){
 	var isValid=$("#dictionaryItemForm").form("validate");
 	if(isValid){
 		isValid = acceptItemProperty();
 	}
 	if(isValid){
 		$.messager.progress();
 	}
 	return isValid;
 }
/**
 * 保存数据
 */
function saveOrUpdateItem(){
	if(!validateItemForm()){
		return;
	}
	$('#itemPropertyDatagrid').datagrid('acceptChanges');
	var d={
			id:$("#itemId").val(),
			parentId:$("#itemParentId").val(),
			dictionary:{
				id:$("#dictionaryId").val()
			},
			dictionaryCode:$("#hiddenDictionaryCode").val(),
			code:$("#itemCode").textbox("getValue"),
			name:$("#itemName").textbox("getValue"),
			pinyin:$("#pinyin").textbox("getValue"),
			wubi:$("#wubi").textbox("getValue"),
			sort:$("#itemSort").textbox("getValue"),
			isCustom:$("input[name='isCustom']:checked").val(),
			selected:($("input[name='defaultSelected']:checked").val()=="1"?true:false),
			explain:$("#explain").textbox("getValue"),
			comments:$("#itemComments").textbox("getValue"),
			dictionaryItemProperties:$("#itemPropertyDatagrid").datagrid("getRows")
	};
	var params={
			json:JSON.stringify(d)
	}
	$.post("../dictionaryItem/saveOrUpdate.action",params,function(data){
		   	  $.messager.progress('close');
			  if (data.result=="success"){    
					$.messager.alert('提示',data.message,"success",function(){
						//关闭窗口
						closeItemWindow();
						var dictionaryItem=data.data;
						//刷新数据到treegrid
						if(dictionaryItem.parentId!=$("#selectedParentId").val()){
							searchDictionaryItem();
						}else if($("#itemId").val()==""){
							if(d.parentId="-1"){
								$("#dictionaryItemTreegrid").treegrid("append",{
									data:[{
										id:dictionaryItem.id,
										name:dictionaryItem.name,
										text:dictionaryItem.name,
										code:dictionaryItem.code,
										wubi:dictionaryItem.parentId,
										pinyin:dictionaryItem.parentId,
										parentId:dictionaryItem.parentId,
										parentName:dictionaryItem.parentName,
										state:'open',
										active:dictionaryItem.active
									}]
								});
							}else{
								$("#dictionaryItemTreegrid").treegrid("append",{
									parent:dictionaryItem.parentId,
									data:[{
										id:dictionaryItem.id,
										name:dictionaryItem.name,
										text:dictionaryItem.name,
										code:dictionaryItem.code,
										wubi:dictionaryItem.parentId,
										pinyin:dictionaryItem.parentId,
										parentId:dictionaryItem.parentId,
										parentName:dictionaryItem.parentName,
										state:'open',
										active:dictionaryItem.active
									}]
								});
							}
						}else{
							$("#dictionaryItemTreegrid").treegrid("update",{
								id:dictionaryItem.id,
								row:{
									name:dictionaryItem.name,
									text:dictionaryItem.name,
									code:dictionaryItem.code,
									wubi:dictionaryItem.wubi,
									pinyin:dictionaryItem.pinyin,
									parentId:dictionaryItem.parentId,
									parentName:dictionaryItem.parentName
								}
							});
						}
						//刷新工具栏
						if(dictionaryItem.isCustom==1){
							$('#editDictionaryButton').linkbutton('enable');
						}else{
							$('#editDictionaryButton').linkbutton('disable');
						}
					});
				}else{
					$.messager.alert('提示',data.message,"info");
				}
			  
		  })
	    .error(function(XMLHttpRequest, textStatus, errorThrown){
			  $.messager.progress('close');
			  $.messager.alert('提示',"无法保存字典项信息","info");
		  });
}
/**
 * 重置修改字典项
 */
function resetItem(){
	$.messager.confirm('确认',"确认重置？",function(result){
		if(result){
			var select=$("#itemId").val();
			if(select==null||select==""){
				resetForm();
			}else{
				reLoadItemData(select);
				//editItem();
			}
		}
	});
}

/**
 * 取消修改字典项（关闭窗口）
 */
function closeItemWindow(){
	$("#dictionaryItemWindow").window("close");
}

/**
 * 重置表单
 */
function resetForm(){
	$("#itemParentName").combotree("enable");
	$("#itemCode").textbox("enable");
	$("#itemName").textbox("enable");
	$("#pinyin").textbox("enable");
	$("#wubi").textbox("enable");
	$("#itemSort").textbox("enable");
	$("input[name='isCustom']").removeAttr("disabled");
	$("input[name='defaultSelected']").removeAttr("disabled");
	$("#explain").textbox("enable");
	$("#itemComments").textbox("enable");
	$("#itemPropertyDatagrid").datagrid("loadData",[]);
	$('#addPropertyButton').linkbutton('enable');
	$('#removePropertyButton').linkbutton('enable');
	$('#acceptPropertyButton').linkbutton('enable');
	$("#itemCode").textbox("setValue","");
	$("#itemName").textbox("setValue","");
	$("#pinyin").textbox("setValue","");
	$("#wubi").textbox("setValue","");
	$("#itemSort").textbox("setValue","");
	document.getElementById("isCustom1").checked=true;
	document.getElementById("defaultSelected0").checked=true;
	$("#explain").textbox("setValue","");
	$("#itemComments").textbox("setValue","");
	//$('#item_save').linkbutton('enable');
	//$('#item_reset').linkbutton('enable');
	readOnly=false;
}

/**
 * disabled表单
 */
function disableItemForm(){
	$("#itemParentName").combotree("disable");
	$("#itemCode").textbox("disable");
	$("#itemName").textbox("disable");
	$("#pinyin").textbox("disable");
	$("#wubi").textbox("disable");
	$("#itemSort").textbox("disable");
	$("input[name='isCustom']").attr("disabled","disabled");
	$("input[name='defaultSelected']").attr("disabled","disabled");
	$("#explain").textbox("disable");
	$("#itemComments").textbox("disable");
	$('#addPropertyButton').linkbutton('disable');
	$('#removePropertyButton').linkbutton('disable');
	$('#acceptPropertyButton').linkbutton('disable');
	//$('#item_save').linkbutton('disable');
	//$('#item_reset').linkbutton('disable');
	readOnly=true;

}

/**
 * 添加字典项
 */
function openSaveDictionaryItemWindow(){
	var selectedDictionary=$("#dictionaryTreegrid").treegrid("getSelected");
	var selectItem=$("#dictionaryItemTreegrid").treegrid("getSelected");
	initDictionaryItemWindow();
	resetForm();
	if(selectedDictionary!=null){
		$("#dictionaryDotId").val(selectedDictionary.id);
		$("#dictionaryId").val(selectedDictionary.id);
		$("#itemDictionaryName").textbox("setValue",selectedDictionary.name);
	}
	
	$("#itemActiveLabel").text("");
	$("#itemActiveText").text("");
	$("#dictionaryItemWindow").dialog({
		title:"新增字典项",
		buttons:[{
			id: "item_save",
			text:'保存',
			iconCls:"icon-ok",
			handler: saveOrUpdateItem
		},{
			id: "item_reset",
			text:'重置',
			iconCls:"icon-reload",
			handler: resetItem
		},{
			id: "item_cancel",
			text:'取消',
			iconCls:"icon-cancel",
			handler: closeItemWindow
		}]
		});
	$("#dictionaryItemWindow").dialog("open");
	$('#itemCode').textbox({
		  required:true,
		  validType:{
			  length:[1,15],
			  remoteCheckExists:['../dictionaryItem/checkItemCode.action?dictionaryId='+$('#dictionaryId').val()+'&dictionaryItemId='+$('#itemId').val(),'code']
		  }
	  });
	setTimeout(function(){
		if(selectItem==null){
			$("#itemId").val("");
			$("#itemParentId").val("-1");
			$('#selectedItemParentId').val("-1");
			$("#itemParentName").combotree("setText","顶级");
			$("#dictionaryDotId").val(selectedDictionary.id);
			$("#dictionaryId").val(selectedDictionary.id);
		}else{
			$("#itemId").val("");
			$("#itemParentId").val(selectItem.id);
			$('#selectedItemParentId').val(selectItem.id);
			$("#itemParentName").combotree("setText",selectItem.name);
		}
	},1000);
	
	$('#itemCode').textbox("textbox").focus();
	
}

/**
 * 编辑/查看字典项
 * 
 * @param editable  是否可编辑 true/false
 * @param index  	字典项在treegrid中的索引
 */
function openUpdateDictionaryItemWindow(editable,id){
	if(editable==null){
		editable=false;
	}
	if(id==null){
		var selectItem=$("#dictionaryItemTreegrid").treegrid("getSelected");
		if(selectItem!=null){
			id=selectItem.id
		}else{
			$.messager.alert('提示',"请选择一条字典项","info");
			return;
		}
	}
	initDictionaryItemWindow();
	resetForm();
	var selectedDictionary=$("#dictionaryTreegrid").treegrid("getSelected");
	if(selectedDictionary!=null){
		$("#dictionaryDotId").val(selectedDictionary.id);
		$("#dictionaryId").val(selectedDictionary.id);
		$("#itemDictionaryName").textbox("setValue",selectedDictionary.name);
	}
	var params={
			id:id
	}
	//获取基本属性
	$.post("../dictionaryItem/getDictionaryItemVO.action",params,function(data){
		  if (data.result=="success"){            
			  $("#dictionaryItemForm").form("load",data.data);
			  setTimeout(function(){
				  $("#itemParentName").combotree("setText",data.data.parentName==""||data.data.parentName == null?"顶级":data.data.parentName);
			  },1000);
			  $('#selectedItemParentId').val(data.data.parentId);
			  $("#itemActiveLabel").text("状态：");
			  $("#itemActiveText").text(data.data.active==1?"启用":"停用");
			  $("#itemComments").textbox("setValue",data.data.comments);
			  if(data.data.selected){
				  document.getElementById("defaultSelected1").checked=true;
			  }else{
				  document.getElementById("defaultSelected0").checked=true;
			  }
			  $("#dictionaryDotId").val(data.data.dictionaryId);
			  if(data.data.isCustom!=1||!editable){
				  disableItemForm();
				  $("#dictionaryItemWindow").dialog({
					  buttons:[]
				  });
			  }else{
				  $("#dictionaryItemWindow").dialog({
					  buttons:[{
							id: "item_save",
							text:'保存',
							iconCls:"icon-ok",
							handler: saveOrUpdateItem
						},{
							id: "item_reset",
							text:'重置',
							iconCls:"icon-reload",
							handler: resetItem
						},{
							id: "item_cancel",
							text:'取消',
							iconCls:"icon-cancel",
							handler: closeItemWindow
						}]
				  });
			  }
			  $('#itemCode').textbox({
				  required:true,
				  validType:{
					  length:[1,15],
					  remoteCheckExists:['../dictionaryItem/checkItemCode.action?dictionaryId='+$('#dictionaryId').val()+'&dictionaryItemId='+$('#itemId').val(),'code']
				  }
			  });
			}else{
				$.messager.alert('提示',data.message,"info");
			}
		  	$("#dictionaryItemWindow").dialog({title:editable?"编辑字典项":"查看字典项"});
			$("#dictionaryItemWindow").dialog("open");
			if(editable){
				$('#itemCode').textbox("textbox").focus();
			}
	  })
	.error(function(XMLHttpRequest, textStatus, errorThrown){
			  $.messager.progress('close');
			  $.messager.alert('提示',"无法加载字典项基本信息","info");
		  });
	//扩展属性
	$.post("../dictionaryItem/getDictionaryItemProperty.action",params,function(data){
		  if (data.result=="success"){
			  $("#itemPropertyDatagrid").datagrid("loadData",data.data);
			}else{
				$.messager.alert('提示',data.message,"info");
			}
	  })
	  .error(function(XMLHttpRequest, textStatus, errorThrown){
			  $.messager.progress('close');
			  $.messager.alert('提示',"无法加载字典项扩展信息","info");
		  });
}
/**
 * 启用/停用字典
 * @param active 修改为何种启用状态 1：启用，2：停用
 */
function changeItemActiveStatus(active){
	var select=$("#dictionaryItemTreegrid").treegrid("getSelected");
	if(select==null){
		$.messager.alert('提示',"请选择字典项","info");
		return;
	}
	var params={
			ids:select.id,
			active:active
	}
	var message="确认启用所选的字典项？";
	if(active==0){
		message="确认停用所选字典项？"
	}
	$.messager.confirm('确认',message,function(result){
		if(result){
			$.messager.progress();
			$.post("../dictionaryItem/changeActiveStatus.action",params,function(data){
				  if (data.result=="success"){            
						$.messager.alert('提示',data.message,"success",function(){
							$("#dictionaryItemTreegrid").treegrid("update",{
								  id:select.id,
								  row:{
									  active:active
								  }
							  });
							if(active==1){
								$('#stopItemButton').linkbutton('enable');
								$('#activeItemButton').linkbutton('disable');
							}else{
								$('#stopItemButton').linkbutton('disable');
								$('#activeItemButton').linkbutton('enable');
							}
						}); 
					}else{
						$.messager.alert('提示',data.message,"info");
					}
				  $.messager.progress('close');
			  })
		    .error(function(XMLHttpRequest, textStatus, errorThrown){
				  $.messager.progress('close');
				  $.messager.alert('提示',"操作错误","info");
			  });
		}
	});
}
/**
 * 添加扩展属性
 */
function addItemPropertity(){
	var select=$("#itemTree").tree("getSelected");
	var dictionaryItemId=null;
	var dictionaryItemName="";
	if(select!=null){
		dictionaryItemId=select.id
		dictionaryItemName=select.name
	}
	if(acceptItemProperty()){
		$("#itemPropertyDatagrid").datagrid('appendRow',{
			"id": null,
			"code": "",
			"name": "",
			"value":""
		});
		editRowIndex=$("#itemPropertyDatagrid").datagrid("getRows").length-1;
		$("#itemPropertyDatagrid").datagrid("beginEdit",$("#itemPropertyDatagrid").datagrid("getRows").length-1);
	}
	

}
/**
 * 添加扩展属性
 */
function editItemPropertity(rowIndex){
	editRowIndex=rowIndex;
	$("#itemPropertyDatagrid").datagrid("beginEdit",rowIndex);
	if(acceptItemProperty()){
		$("#itemPropertyDatagrid").datagrid("beginEdit",rowIndex);
	}
}
/**
 * 删除扩展属性行
 */
function removeItemProperty(){
	var select=$("#itemPropertyDatagrid").datagrid("getSelected");
	if(select==null){
		$.messager.alert('提示',"请选择需要删除的扩展属性","info");
		return;
	}
	$("#itemPropertyDatagrid").datagrid("deleteRow",$("#itemPropertyDatagrid").datagrid("getRowIndex",select));
}
/**
 * 单击扩展属性行
 */
function onClickItemProperty(rowIndex){
	if(readOnly){
		return;
	}
	editItemPropertity(rowIndex);
}

/**
 * 接受扩展属性
 */
function acceptItemProperty(){
	var ed = $("#itemPropertyDatagrid").datagrid('getEditor', {index:editRowIndex,field:'code'});
	if(ed==null||ed.actions==null){
		return true;
	}
	var codeValue=$.trim($(ed.target).val());
	var rows=$("#itemPropertyDatagrid").datagrid("getRows");
	if(rows!=null){
		for(var i=0;i<rows.length;i++){
			if(codeValue==rows[i].code&&i!=editRowIndex&&rows[i].code!=""){
				$.messager.alert('提示',"属性编码重复，请重新输入","info");
				$(ed.target).focus();
				return false;
			}
		}
	}
	$('#itemPropertyDatagrid').datagrid('acceptChanges');
	return true;
}
/**
 * 输入拼音打target控件 
 */
function autoAppendItemPinyin(newValue,oldValue){
	if($.trim(newValue)==""){
		$("#pinyin").textbox("setValue","");
		$("#wubi").textbox("setValue","");
	}else{
		$("#pinyin").textbox("setValue",pinyin.getCamelChars($.trim(newValue)));
		$("#wubi").textbox("setValue",wubi.getCamelChars($.trim(newValue)));
	}
}
/**
 * 编辑/查看字典项
 * 
 * @param editable  是否可编辑 true/false
 * @param index  	字典项在treegrid中的索引
 */
function reLoadItemData(id){
	//resetForm();
	var selectedDictionary=$("#dictionaryTreegrid").treegrid("getSelected");
	if(selectedDictionary!=null){
		$("#dictionaryDotId").val(selectedDictionary.id);
		$("#dictionaryId").val(selectedDictionary.id);
		$("#itemDictionaryName").textbox("setValue",selectedDictionary.name);
	}
	var params={
			id:id
	}
	//获取基本属性
	$.post("../dictionaryItem/getDictionaryItemVO.action",params,function(data){
		  if (data.result=="success"){            
			  $("#dictionaryItemForm").form("load",data.data);
			  $("#itemActiveLabel").text("状态：");
			  $("#itemActiveText").text(data.data.active==1?"启用":"停用");
			  $("#itemComments").textbox("setValue",data.data.comments);
			  $('#selectedItemParentId').val(data.data.parentId);
			  if(data.data.selected){
				  document.getElementById("defaultSelected1").checked=true;
			  }else{
				  document.getElementById("defaultSelected0").checked=true;
			  }
			  $("#dictionaryDotId").val(data.data.dictionaryId);
			  if($("#itemParentName").textbox("getValue")==""){
				  $("#itemParentName").textbox("setValue","顶级");
			  }
			  /*$('#itemCode').textbox({
				  required:true,
				  validType:{
					  length:[1,15],
					  remoteCheckExists:['../dictionaryItem/checkItemCode?dictionaryId='+$('#dictionaryId').val()+'&dictionaryItemId='+$('#itemId').val(),'code']
				  }
			  });*/
			}else{
				$.messager.alert('提示',data.message,"info");
			}
	  });
	//扩展属性
	$.post("../dictionaryItem/getDictionaryItemProperty.action",params,function(data){
		  if (data.result=="success"){
			  $("#itemPropertyDatagrid").datagrid("loadData",data.data);
			}else{
				$.messager.alert('提示',data.message,"info");
			}
	  });
}