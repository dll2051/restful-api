 //工具栏
var selectedDictionaryId=null;
var dictionaryToolbar = [{
	 	id:"addDictionaryButton",
		text:'新增',
		iconCls:'icon-add',
		handler:openSaveDictionaryWindow,
		disabled:false
	},{
		id:"editDictionaryButton",
		text:'编辑',
		iconCls:'icon-edit',
		handler:openUpdateDictionaryWindow,
		disabled:true
	},{
		id:"stopDictionaryButton",
		text:'停用',
		iconCls:'icon-cancel',
		handler:function(){
			changeDictionaryActiveStatus(0);
		},
		disabled:true
	},{
		id:"activeDictionaryButton",
		text:'启用',
		iconCls:'icon-ok',
		handler:function(){
			changeDictionaryActiveStatus(1);
		},
		disabled:true
	}];
 /**
  * 初始化字典类型Treegrid
  */
 function initDictionaryTreegrid(){
	 $("#dictionaryTreegrid").treegrid(
	{
		method:'post',
		//singleSelect:true, //多选
		fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
		fit:true,
		striped: true, //奇偶行颜色不同
		//collapsible:true,//可折叠 
		url:'../dictionary/queryDictionary',
		cache:false,
		idField:"id",
		treeField:"name",
		sortName:"sort",
		sortOrder:"asc",
		remoteSort:true,
		rownumbers:false, //显示行号
		pagination:false,
		data:[],
		columns:[[        
		          {field:'name',title:'名称',width:160,sortable:true,
					  formatter: function(value,row,index){
						  if(value==null){
							  value="";
						  }
						  var viewDictionary=$.judgeExistPermission("viewDictionary");
						  if(viewDictionary){
							  return "<a href=\"javascript:void(0)\"  style=\"color:green\" "
		              		  	+"onclick=\"openViewDictionaryWindow('"+row['id']+"');\">"+value+"</a>";
						  }else{
							  return value;
						  }
						  
                	  }
				  },
				  {field:'standardNo',title:'标准号',width:80,sortable:true},
                  {field:'code',title:'编码',width:80,sortable:true}
              ]],
          toolbar:dictionaryToolbar,
          onBeforeSortColumn:sortDictionary,
          loader:dictionaryLoader,
          onSelect:selectDictionary
	});
	//$("#addDictionaryButton").checkPermission();
	//$("#editDictionaryButton").checkPermission();
	//$("#stopDictionaryButton").checkPermission();
	//$("#activeDictionaryButton").checkPermission();
	$("#addDictionaryButton").linkbutton("enable");
 }
 /**
  * 初始化窗口
  */
 function initDialogs(){
	 //窗口中的组件
	 $('#parentName').combotree({
			url:"queryDictionary.action",
			valueField:"name",
			textField:"name",
			queryParams:{
				selectTop:true
			},
			loader:defaultLoader,
			onSelect:function(node){
				$("#parentId").val(node.id);
				if(node.typeCode==null||node.typeCode==""){
					$("#typeCode").val(node.code==null?"":node.code);
				}else{
					$("#typeCode").val(node.typeCode);
				}
			}
		});
	 $('#dictionaryWindowForm').form({});
	 //窗口初始化
	 $("#dictionaryWindow").dialog({
			title:"新增字典",
			width:630,
			height:310,
			modal:true,
			minimizable:false,
			maximizable:false,
			collapsible:false,
			resizable:false,
			closed: true,
			buttons:[{
				id: "save_btn",
				text:'保存',
				iconCls:"icon-ok",
				handler: saveOrUpdateDictionary
			},{
				id: "cancel_btn",
				text:'取消',
				iconCls:"icon-cancel",
				handler: closeDictionaryWindow
			}]
		});
	$("#dictionaryWindow").dialog("close");
	$('#dictionaryItemForm').form({});
	$("#dictionaryItemWindow").dialog({
		title:"编辑字典项",
		width:700,
		height:500,
		modal:true,
		minimizable:false,
		maximizable:false,
		collapsible:false,
		cache: false,
		resizable:false,
		closed: true
	});
	$("#dictionaryItemWindow").dialog("close");
 }
 
 /**
 * 加载数据
 */
function dictionaryLoader(param,success,error){
	var t = $(this);
	var options = t.treegrid("options");
	var invokeParams=param;
	invokeParams.keyword=$.trim($("#search_keyword").textbox("getValue"));
	invokeParams.active=(document.getElementById("showUnactive").checked==true)?"":"1";
	invokeParams.orderBy=options.sortName!=undefined?options.sortName:"sort,name",
	invokeParams.orderDir=options.sortOrder!=undefined?options.sortOrder:"asc,asc";
	invokeParams.pageNo=options.pageNumber;
	invokeParams.pageSize=options.pageSize;
	$.post("../saleDetail/querySaleDetail.action",invokeParams,function(d){
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
 * 打开新增字典窗口
 */
function openSaveDictionaryWindow(){
	//$('#dictionaryWindowForm').form("enableValidation");
	//$("#dictionaryOperateTr").show();
	$("#activeLabel").text("");
	$("#activeText").text("");
	$("#id").val("");
	$("#dictionaryCode").textbox("enable");
	$("#dictionaryName").textbox("enable");
	$("#dictionaryPinyin").textbox("enable");
	$("#dictionaryWubi").textbox("enable");
	$("#dictionaryPinyin").textbox("enable");
	$("#dictionaryWubi").textbox("enable");
	$("#parentName").combotree("enable");
	$("#standardNo").textbox("enable");
	$("#useRange").combobox("enable");
	$("#source").textbox("enable");
	$("input[name='isModify']").removeAttr("disabled");
	$("input[name='isCache']").removeAttr("disabled");
	$("#sort").textbox("enable");
	$("#comments").textbox("enable");
	$('#save_btn').show();
	$('#cancel_btn').show();
	$('#save_btn').linkbutton("enable");
	$('#dictionaryCode').textbox({
		  required:true,
		  validType:{length:[1,15],remoteCheckExists:['checkDictionaryCode.action?dictionaryId='+$('#id').val(),'code']},
		  missingMessage:'请输入字典编码'//,
		  //invalidMessage:'字典编码已存在或者字符串长度太长'
	  });
	$("#dictionaryWindowForm").form("reset");
	var dictNameInput=$("#dictionaryName").textbox("textbox");
	dictNameInput.unbind("keyup").bind("keyup",function(){
		autoAppendPinyin($(this).val(),"dictionaryPinyin","dictionaryWubi");
	});
	//$("span[id^='dictionaryStarFlag']").show();
	var selected=$("#dictionaryTreegrid").treegrid("getSelected");
	if(selected==null){
		$('#parentName').combotree("setText","顶级");
		$('#parentId').val("-1");
		$('#selectedParentId').val("-1");
	}else{
		$('#parentName').combotree("setText",selected.name);
		$('#parentId').val(selected.id);
		$('#selectedParentId').val(selected.id);
		$('#typeCode').val(selected.typeCode==null||selected.typeCode==""?selected.code:selected.typeCode);
	}
	$("#dictionaryWindow").dialog({title:"新增字典"});
	$("#dictionaryWindow").dialog("open");
	$('#dictionaryCode').textbox("textbox").focus();
}
/**
 * 打开修改字典窗口
 */
function openUpdateDictionaryWindow(){
	var selected=$("#dictionaryTreegrid").treegrid("getSelected");
	if(selected==null){
		$.messager.alert('提示',"请选择一条字典再操作","info");
		return;
	}
	var params={
			id:selected.id
		};
	$.post("getDictionary.action",params,function(d){
	    	if(d.result="success"){
	    		  $('#dictionaryWindowForm').form('load', d.data);
	    		  $("#activeLabel").text("状态：");
	    		  $("#activeText").text(d.data.active==1?"启用":"停用");
	    		  $('#selectedParentId').val(d.data.parentId);
	    		  $('#parentName').combotree("setText",d.data.parentName==""||d.data.parentName == null?"顶级":d.data.parentName);
				  var dictNameInput=$("#dictionaryName").textbox("textbox");
					dictNameInput.unbind("keyup").bind("keyup",function(){
						autoAppendPinyin($(this).val(),"dictionaryPinyin","dictionaryWubi");
					});
				  if(d.data.isModify=='0'){
					  //$('#dictionaryWindowForm').form("disableValidation");
					  $("#dictionaryCode").textbox("disable");
					  $("#dictionaryName").textbox("disable");
					  $("#dictionaryPinyin").textbox("disable");
					  $("#dictionaryWubi").textbox("disable");
					  $("#parentName").combotree("disable");
					  $("#standardNo").textbox("disable");
					  $("#useRange").combobox("disable");
					  $("#source").textbox("disable");
					  $("input[name='isModify']").attr("disabled","disabled");
					  $("input[name='isCache']").attr("disabled","disabled");
					  $("#sort").textbox("disable");
					  $("#comments").textbox("disable");
					  $('#save_btn').show();
					  $('#cancel_btn').show();
					  $('#save_btn').linkbutton('disable');
					  //$("span[id^='dictionaryStarFlag']").hide();
				  }else{
					  //$('#dictionaryWindowForm').form("enableValidation");
					  $("#dictionaryCode").textbox("enable");
					  $("#dictionaryName").textbox("enable");
					  $("#dictionaryPinyin").textbox("enable");
					  $("#dictionaryWubi").textbox("enable");
					  $("#parentName").combotree("enable");
					  $("#standardNo").textbox("enable");
					  $("#useRange").combobox("enable");
					  $("#source").textbox("enable");
					  $("input[name='isModify']").removeAttr("disabled");
					  $("input[name='isCache']").removeAttr("disabled");
					  $("#sort").textbox("enable");
					  $("#comments").textbox("enable");
					  $('#save_btn').show();
					  $('#cancel_btn').show();
					  $('#save_btn').linkbutton('enable');
					  $('#dictionaryCode').textbox("textbox").focus();
					  $('#dictionaryCode').textbox({
						  required:true,
						  validType:{length:[1,15],remoteCheckExists:['checkDictionaryCode.action?dictionaryId='+$('#id').val(),'code']},
						  missingMessage:'请输入字典编码'
					  });
				  }
				  $("#dictionaryWindow").dialog({title:"编辑字典"});
				  $("#dictionaryWindow").dialog("open");
			  }else{
				  $.messager.alert('提示',d.message,"info");
			  }
		  })
	    .error(function(){
	    	$.messager.alert('提示',"无法获取字典信息","info");
		  });
}

/**
 * 打开修改字典窗口
 */
function openViewDictionaryWindow(dictionaryId){
	var params={
			id:dictionaryId
		};
	$("#dictionaryWindow").dialog({title:"查看字典"});
	$.post("getDictionary.action",params,function(d){
	    	if(d.result="success"){
	    		  //$("#dictionaryOperateTr").hide();
				  $('#dictionaryWindowForm').form('load', d.data);
				  $('#parentName').combotree("setValue",d.data.parentName==""||d.data.parentName == null?"顶级":d.data.parentName);
				  $("#activeLabel").text("状态：");
	    		  $("#activeText").text(d.data.active==1?"启用":"停用");
				  $("#comments").textbox("setText",d.data.comments);
				  $("#dictionaryCode").textbox("disable");
				  $("#dictionaryName").textbox("disable");
				  $("#dictionaryPinyin").textbox("disable");
				  $("#dictionaryWubi").textbox("disable");
				  $("#parentName").combotree("disable");
				  $("#standardNo").textbox("disable");
				  $("#useRange").combobox("disable");
				  $("#source").textbox("disable");
				  $("input[name='isModify']").attr("disabled","disabled");
				  $("input[name='isCache']").attr("disabled","disabled");
				  $("#sort").textbox("disable");
				  $("#comments").textbox("disable");
				  $('#save_btn').hide();
				  $('#cancel_btn').hide();
				  $('#save_btn').linkbutton('disable');
				  $("#dictionaryWindow").dialog("open");
			  }else{
				  $.messager.alert('提示',d.message,"info");
			  }
		  })
	    .error(function(){
	    	$.messager.alert('提示',"无法获取字典信息","info");
		  });
}

/**
 * 关闭字典窗口
 */
function closeDictionaryWindow(){
	$("#dictionaryWindow").dialog("close");
}
/**
 * 保存数据
 */
function validateDictionaryForm(){
	var isValid=$("#dictionaryWindowForm").form("validate");
	if(isValid){
		$.messager.progress();
	}
	return isValid;
}
/**
 * 保存数据
 */
function saveOrUpdateDictionary(){
	if(!validateDictionaryForm()){
		return;
	}
	var d=$("#dictionaryWindowForm").serialize();
	d=d+"&active="+$("#dictionaryActive").val();
	$.post("saveOrUpdate.action",d,function(data){
			  if (data.result=="success"){            
					$.messager.alert('提示',data.message,"success",function(){
						$("#dictionaryWindow").dialog("close");
						//将节点增加进去
						var dictionary=data.data;
						if(dictionary.parentId!=$("#selectedParentId").val()){
							searchDictionary();
						}else if($("#id").val()==""){
							//新增
							if(d.parentId=="-1"){
								$("#dictionaryTreegrid").treegrid("append",{
									data:[{
										id:dictionary.id,
										name:dictionary.name,
										text:dictionary.name,
										standardNo:dictionary.standardNo,
										code:dictionary.code,
										parentId:dictionary.parentId,
										parentName:dictionary.parentName,
										state:'open',
										active:dictionary.active,
										isModify:dictionary.isModify
									}]
								});
							}else{
								$("#dictionaryTreegrid").treegrid("append",{
									parent:dictionary.parentId,
									data:[{
										id:dictionary.id,
										name:dictionary.name,
										text:dictionary.name,
										standardNo:dictionary.standardNo,
										code:dictionary.code,
										parentId:dictionary.parentId,
										parentName:dictionary.parentName,
										state:'open',
										active:dictionary.active,
										isModify:dictionary.isModify
									}]
								});
							}
						}else{
							$("#dictionaryTreegrid").treegrid("update",{
								id:dictionary.id,
								row:{
									//id:dictionary.id,
									name:dictionary.name,
									//text:dictionary.name,
									standardNo:dictionary.standardNo,
									code:dictionary.code,
									parentId:dictionary.parentId,
									parentName:dictionary.parentName,
									active:dictionary.active,
									isModify:dictionary.isModify
								}
							});
						}
						//刷新工具栏
						if(dictionary.isModify==1){
							$('#editDictionaryButton').linkbutton('enable');
						}else{
							$('#editDictionaryButton').linkbutton('disable');
						}
					});     
				}else{
					$.messager.alert('提示',data.message,"info");
				}
			  $.messager.progress('close');
		  })
	    .error(function(){
			  $.messager.progress('close');
			  $.messager.alert('提示',"无法保存字典信息","info");
		  });
}
/**
 * 查询字典
 */
function searchDictionary(){
	selectedDictionaryId=null;
	$("#dictionaryTreegrid").treegrid({
		pageNumber:1,
		queryParams:{id:null}
	});
	if($('#editDictionaryButton')){
		$('#editDictionaryButton').linkbutton('disable');
	}
	if($('#stopDictionaryButton')){
		$('#stopDictionaryButton').linkbutton('disable');
	}
	if($('#activeDictionaryButton')){
		$('#activeDictionaryButton').linkbutton('disable');
	}
	$("#dictionaryItemTreegrid").treegrid("loadData",[]);
	if($('#addItemButton')){
		$('#addItemButton').linkbutton('disable');
	}
	if($('#editItemButton')){
		$('#editItemButton').linkbutton('disable');
	}
	if($('#stopItemButton')){
		$('#stopItemButton').linkbutton('disable');
	}
	if($('#activeItemButton')){
		$('#activeItemButton').linkbutton('disable');
	}
}
/**
 * 重置查询表单
 */
function resetSearchDictionary(){
	$("#search_keyword").textbox("setValue","");
	document.getElementById("showUnactive").checked=true;
	searchDictionary();
}

/**
 * 删除字典
 */
function deleteDictionaris(){
	var selections=$("#dictionaryTreegrid").treegrid("getSelections");
	var params=new Object();
	if(selections.length==0){
		$.messager.alert('提示',"请选择一条字典再操作","info");
		return;
	}else{
		var ids=new Array();
		var names=new Array();
		//拼接字符串
		for(var i=0;i<selections.length;i++){
			//default_split=@#$
			ids=ids+default_split+selections[i].id;
			names=names+default_split+selections[i].name;
		}
		params={
				ids:ids,
				names:names
			}
	}
	
	$.messager.confirm('确认','确认删除所选的字典?',function(result){
		if(result){
			$.messager.progress();
			$.post("delete.action",params,function(data){
				  if (data.result=="success"){            
					$.messager.alert('提示',data.message,"success");     
				}else{
					$.messager.alert('提示',data.message,"info");
				}
				  $("#dictionaryWindow").window("close");
				  $("#dictionaryDatagrid").treegrid("reload");
				  $.messager.progress('close');
			  }
			)
		    .error(function(){
				  $.messager.progress('close');
				  $.messager.alert('提示',"无法删除字典信息","info");
			  });
		}
	});
}
/**
 * 启用/停用字典
 * @param active 修改为何种启用状态 1：启用，2：停用
 */
function changeDictionaryActiveStatus(active){
	var selected=$("#dictionaryTreegrid").treegrid("getSelected");
	var params;
	var message="确认启用所选的字典？";
	if(active==0){
		message="确认停用所选字典？"
	}
	if(selected==null){
		$.messager.alert('提示',"请选择一条字典再操作","info");
		return;
	}else{
		var ids=selected.id;
		params={
			ids:ids,
			active:active
		}
	}   
	$.messager.confirm('确认',message,function(result){
		if(result){
			$.messager.progress();
			$.post("changeActiveStatus.action",params,function(data){
				  if (data.result=="success"){            
						$.messager.alert('提示',data.message,"success");     
					}else{
						$.messager.alert('提示',data.message,"info");
					}
				  $("#dictionaryWindow").window("close");
				  $("#dictionaryTreegrid").treegrid("update",{
					  id:selected.id,
					  row:{
						  active:active
					  }
				  });
				  if(active==1){
						$('#stopDictionaryButton').linkbutton('enable');
						$('#activeDictionaryButton').linkbutton('disable');
					}else{
						$('#stopDictionaryButton').linkbutton('disable');
						$('#activeDictionaryButton').linkbutton('enable');
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
 * 排序
 */
function sortDictionary(sort, order){
	var t = $(this);
	t.treegrid({
		sortName:sort,
		sortOrder:order,
		queryParams:{id:null}
	});
	return false;
	//var pager=t.datagrid("getPager");
	//pager.pagination({
	//	pageSize:20
	//});
}

/**
 * 打开编辑/查看字典项窗口
 * 
 * @param operateType 操作类型 1：编辑 2 查看
 */
function openEditItemWindow(operateType,index){
	if(index!=null&&index!=""){
		$("#dictionaryDatagrid").datagrid("selectRow",index);
	}
	var selections=$("#dictionaryDatagrid").datagrid("getSelections");
	if(selections.length!=1){
		$.messager.alert('提示',"请选择一条字典再操作","info");
		return;
	}
	$("#dictionaryItemPanel").panel({
		cache: false,
		border: false,
		href:'../dictionaryItem/editItemPage.action',
		queryParams:{
			dictionaryId:selections[0].id,
			dictionaryName:selections[0].name,
			dictionaryCode:selections[0].code
		},
		method:"post",
		onLoad:function(){
			if(operateType==2){
				$("#itemToolbarTr").hide();
				$("#itemOpreateDiv").hide();
				$(".dialog-button").hide();
			}else{
				$("#itemToolbarTr").show();
				$("#itemOpreateDiv").show();
				$(".dialog-button").show();
			}
			var dictNameInput=$("#itemName").textbox("textbox");
			dictNameInput.bind("keyup",function(){
				autoAppendPinyin($(this).val(),null);
			});
			$("#dictionaryId").val(selections[0].id);
			$("#dictionaryDotId").val(selections[0].id);
			$("#hiddenDictionaryCode").val(selections[0].code);
			$("#itemTree").tree({
				url:"../dictionaryItem/queryItemTreeData.action",
				queryParams:{
					dictionaryId:selections[0].id
				},
				loader:itemTreeLoader,
				onSelect:selectAItemIntree
			});
			$("#dictionaryItemForm").form({});
			$("#itemPropertyDatagrid").datagrid({
				title:"扩展属性",
				height:150,
				idField:"code",
				itColumns: true,
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
			disableItemForm();
		}
	});
	if(operateType==1){
		$("#dictionaryItemWindow").dialog({
			width:815,
 			height:530,
			title:"编辑字典项"
		});
	}else{
		$("#dictionaryItemWindow").dialog({
			width:815,
 			height:500,
			title:"查看字典项"
		});
	}
	$("#dictionaryItemWindow").dialog("open");
}

/**
 * 输入拼音打target控件 
 */
function autoAppendPinyin(newValue,pinyinTextId,wubiTextId){
	if($.trim(newValue)==""){
		$("#"+pinyinTextId).textbox("setValue","");
		$("#"+wubiTextId).textbox("setValue","");
	}else{
		$("#"+pinyinTextId).textbox("setValue",pinyin.getCamelChars($.trim(newValue)));
		$("#"+wubiTextId).textbox("setValue",wubi.getCamelChars($.trim(newValue)));
	}
}
/**
 * 选择一行数据
 * @param rowIndex
 * @param rowData
 */
function selectDictionary(rowData){
	if(selectedDictionaryId==null||selectedDictionaryId!=rowData.id){
		if(rowData.isModify==1){
			$('#editDictionaryButton').linkbutton('enable');
		}else{
			$('#editDictionaryButton').linkbutton('disable');
		}
		if(rowData.active==1){
			$('#stopDictionaryButton').linkbutton('enable');
			$('#activeDictionaryButton').linkbutton('disable');
		}else{
			$('#stopDictionaryButton').linkbutton('disable');
			$('#activeDictionaryButton').linkbutton('enable');
		}
		//如果不是顶级节点，则加载字典下的字典项
		if(selectedDictionaryId!=rowData.id&&rowData.parentId!='-1'){
			searchDictionaryItem();
		}else{
			$("#dictionaryItemTreegrid").treegrid("loadData",[]);
			$('#addItemButton').linkbutton('disable');
			$('#editItemButton').linkbutton('disable');
			$('#stopItemButton').linkbutton('disable');
			$('#activeItemButton').linkbutton('disable');
		}
		selectedDictionaryId=rowData.id;
	}else{
		selectedDictionaryId=null;
		$('#editDictionaryButton').linkbutton('disable');
		$('#stopDictionaryButton').linkbutton('disable');
		$('#activeDictionaryButton').linkbutton('disable');
		$("#dictionaryItemTreegrid").treegrid("loadData",[]);
		$('#addItemButton').linkbutton('disable');
		$('#editItemButton').linkbutton('disable');
		$('#stopItemButton').linkbutton('disable');
		$('#activeItemButton').linkbutton('disable');
		$("#dictionaryTreegrid").treegrid("unselectAll");
	}
}

/**
 * 默认树加载器
 * @param param
 * @param success
 * @param error
 * @returns {Boolean}
 */
function defaultLoader(param,success,error){
	var t = $(this);
	var options = t.tree("options");
	if (options!=null&&!options.url) {
		return false;
	}
	$.post(options.url,param,function(d){
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