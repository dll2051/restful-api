//默认分割符号
var default_split="@#$";

function fillsize(percent) {
	var bodyWidth = document.body.clientWidth;
	return (bodyWidth - 90) * percent;
}
function heightSize(percent, item){
	if (!item){
		item = document;
	}
	var bodyHeight = item.compatMode=="CSS1Compat" ? item.documentElement.clientHeight : item.body.clientHeight;
	return bodyHeight  * percent;
} 
/**
 * 扩展datagrid的编辑单元格的方法
 */
$.extend($.fn.datagrid.methods, {
    editCell: function(jq,param){
        return jq.each(function(){
            var opts = $(this).datagrid('options');
            var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor1 = col.editor;
                if (fields[i] != param.field){
                    col.editor = null;
                }
            }
            $(this).datagrid('beginEdit', param.index);
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor = col.editor1;
            }
        });
    }
});

/**  
 * 刷新当前页面的DataGrid区域
 * 
 * @param gridId DataGrid的ID
 * @return  
 */
function reloadDataGrid(gridId){
	$('#' + gridId).datagrid('reload');
}

/**  
 * 关闭传入的ID所属Jquery EasyUi window窗口
 * 
 * @param windowId window窗口ID
 * @return  
 */
function closePopupWindow(windowId){
	top.$('#' + windowId).window('close');
}
/**
 * 时间格式化
 */
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, //month 
		"d+" : this.getDate(), //day 
		"h+" : this.getHours(), //hour 
		"m+" : this.getMinutes(), //minute 
		"s+" : this.getSeconds(), //second 
		"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter 
		"S" : this.getMilliseconds()
	//millisecond 
	}
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}

/**
 * 打开/关闭EasyUI的进度条遮罩
 * 
 * @param type 打开/关闭遮罩 打开:'open';关闭:'close'
 */
function progressMask(type, title, msg, text){
	if(type == "open"){
		if (typeof(title)=='undefined'){
			title = '请稍等...';
		}
		if (typeof(msg)=='undefined'){
			msg = '正在进行处理';
		}
		if (typeof(text)=='undefined'){
			text = '处理中.......';
		}
		$.messager.progress({ title: title, msg: msg, text: text });
	} else if (type == "close"){
		$.messager.progress('close');
	} else {
		return;
	}
}

/**
 * jquery easyui tree 添加title的方法
 * 注意，组织树的时候必须添加title属性
 * @param row 待添加的行
 */
function addTreeTitle(row){
    $.each(row,function(idx,val){
        $("[node-id='"+val.id+"']").attr('title',val.title);//设置title
        if(val.children){
            addTreeTitle(val.children);//如果还有子节点则继续遍历
        }
    });
}

/**
 * jquery easyui获取异步树子节点的方法
 * @param treeId --树的ID
 * @param node --父级节点
 * @param asynUrl --异步加载子节点时的URL
 * @param param --ajax的参数
 * @return
 */
function easyuiTreeChild(node,treeId,asynUrl,param){
	var bool = false;
	$.ajax({
		type : "post",
		url : asynUrl,
		data : param,
		dataType : "json",
		async: false,
		cache: false,
		success : function(result){
			if(result!=null){
				for(var i in result){
					if($('#'+treeId).tree("find", result[i].id)){
						continue;
					}
					$('#'+treeId).tree('append', {
						parent: node.target,
						data:result
					});
				}
				bool = true;;
			}
		}
	})
	return bool;
}

/** 
 * 扩展表格列对齐属性： 
 *      自定义一个列字段属性： 
 *      headalign ：原始align属性针对数据有效, headalign针对列名有效
 *      
 */
var extendData=
	{  
	    onLoadSuccess : function() {  
	        var target = $(this);  
	        var opts = $.data(this, "datagrid").options;  
	        var panel = $(this).datagrid("getPanel");  
	        //获取列
	        var fields=$(this).datagrid('getColumnFields',false);
	        //datagrid头部 table 的第一个tr 的td们，即columns的集合
	        var headerTds =panel.find(".datagrid-view2 .datagrid-header .datagrid-header-inner table tr:first-child").children();
	        //重新设置列表头的对齐方式
	        headerTds.each(function (i, obj) {
	            var col = target.datagrid('getColumnOption',fields[i]);
	            if (!col.hidden && !col.checkbox)
	            {
	                var headalign=col.headalign||col.align||'left';
	                $("div:first-child", obj).css("text-align", headalign);
	            }
	        })

			$(this).datagrid('clearSelections'); //一定要加上这一句，要不然datagrid会记住之前的选择状态，注销时会出问题
			function bindRowsEvent(){
				var rows = panel.find('tr[datagrid-row-index]');
				rows.unbind('click').bind('click',function(e){
					// 当点击位置在<a>标签上，取消选中
					if ((e.target.parentElement.tagName == 'a') || (e.target.parentElement.tagName == 'A') || (e.target.tagName == 'a') || (e.target.tagName == 'A')){
						return false;
					}
				});
				rows.find('div.datagrid-cell-check input[type=checkbox]').unbind().bind('click', function(e){
					var index = $(this).parent().parent().parent().attr('datagrid-row-index');
					if ($(this).attr('checked')){
						$(this).datagrid('selectRow', index);
					} else {
						$(this).datagrid('unselectRow', index);
					}
					e.stopPropagation();
				});
			}
			setTimeout(function(){ bindRowsEvent(); }, 10);
	    }
	};
$.extend($.fn.datagrid.defaults,extendData);
$.extend($.fn.treegrid.defaults,extendData);

String.prototype.trim = function() {
	var t = this.replace(/(^\s*)|(\s*$)/g, "");
	return t.replace(/(^\u3000*)|(\u3000*$)/g, "");
};
//验证身份证号
function validateIdCard(obj){
 var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};
  var iSum = 0;
 //var info = "";
 var strIDno = obj;
 var idCardLength = strIDno.length;
 if(!/^\d{17}(\d|x)$/i.test(strIDno)&&!/^\d{15}$/i.test(strIDno))
        return 1; //非法身份证号

 if(aCity[parseInt(strIDno.substr(0,2))]==null)
 return 2;// 非法地区

  // 15位身份证转换为18位
 if (idCardLength==15)
 {
    sBirthday = "19" + strIDno.substr(6,2) + "-" + Number(strIDno.substr(8,2)) + "-" + Number(strIDno.substr(10,2));
  var d = new Date(sBirthday.replace(/-/g,"/"))
  var dd = d.getFullYear().toString() + "-" + (d.getMonth()+1) + "-" + d.getDate();
  if(sBirthday != dd)
                return 3; //非法生日
              strIDno=strIDno.substring(0,6)+"19"+strIDno.substring(6,15);
              strIDno=strIDno+GetVerifyBit(strIDno);
 }

       // 判断是否大于2078年，小于1900年
       var year =strIDno.substring(6,10);
       if (year<1900 || year>2078 )
           return 3;//非法生日

    //18位身份证处理

   //在后面的运算中x相当于数字10,所以转换成a
    strIDno = strIDno.replace(/x$/i,"a");

  sBirthday=strIDno.substr(6,4)+"-"+Number(strIDno.substr(10,2))+"-"+Number(strIDno.substr(12,2));
  var d = new Date(sBirthday.replace(/-/g,"/"))
  if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))
                return 3; //非法生日
    // 身份证编码规范验证
  for(var i = 17;i>=0;i --)
   iSum += (Math.pow(2,i) % 11) * parseInt(strIDno.charAt(17 - i),11);
  if(iSum%11!=1)
                return 1;// 非法身份证号

   // 判断是否屏蔽身份证
    var words = new Array();
    words = new Array("11111119111111111","12121219121212121");

    for(var k=0;k<words.length;k++){
        if (strIDno.indexOf(words[k])!=-1){
            return 1;
        }
    }

 return 0;
}


 /**  
  * 18位数身份证号码中的生日
  * @param idCard 18位书身份证字符串  
  * @return  
  */  
function brithBy18IdCard(idCard18){   
    var year =  idCard18.substring(6,10);   
    var month = idCard18.substring(10,12);   
    var day = idCard18.substring(12,14);   
    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
        return temp_date;   
}   

  /**  
   * 15位数身份证号码中的生日
   * @param idCard15 15位书身份证字符串  
   * @return  
   */  
  function brithBy15IdCard(idCard15){
      var year =  idCard15.substring(6,8);   
      var month = idCard15.substring(8,10);   
      var day = idCard15.substring(10,12);   
      var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
            return temp_date;   
 }
  
   /**
    * 身份证失去焦点获取生日
    * @param {Object} obj 身份证id
    * @param {Object} target 时间放置id
    * @return {TypeName} 
    */
  function toGetBirth(obj,target){
	  var birthDate = "";
	  var sfzh= $("#"+obj).val();
	  var s = validateIdCard(sfzh);
	  if(s!=0){
		    $("#"+target).val(birthDate);
	  }else if(sfzh.length==18){
			birthDate = brithBy18IdCard(sfzh);
			//$("#"+target).val(birthDate.format("yyyy-MM-dd"));
			$("#"+target).datebox("setValue",birthDate.format("yyyy-MM-dd"));
	  }else if(sfzh.length==15){
		    birthDate = brithBy15IdCard(sfzh);
			//$("#"+target).val(birthDate.format("yyyy-MM-dd"));
		    $("#"+target).datebox("setValue",birthDate.format("yyyy-MM-dd"));
	  }
  }
   /**  
  * 验证18位数身份证号码中的生日是否是有效生日  
  * @param idCard 18位书身份证字符串  
  * @return  
  */  
function isValidityBrithBy18IdCard(idCard18){   
    var year =  idCard18.substring(6,10);   
    var month = idCard18.substring(10,12);   
    var day = idCard18.substring(12,14);   
    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
    // 这里用getFullYear()获取年份，避免千年虫问题   
    if(temp_date.getFullYear()!=parseFloat(year)   
          ||temp_date.getMonth()!=parseFloat(month)-1   
          ||temp_date.getDate()!=parseFloat(day)){   
            return false;   
    }else{   
        return true;   
    }   
}   
  /**  
   * 验证15位数身份证号码中的生日是否是有效生日  
   * @param idCard15 15位书身份证字符串  
   * @return  
   */  
  function isValidityBrithBy15IdCard(idCard15){   
      var year =  idCard15.substring(6,8);   
      var month = idCard15.substring(8,10);   
      var day = idCard15.substring(10,12);   
      var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
      // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
      if(temp_date.getYear()!=parseFloat(year)   
              ||temp_date.getMonth()!=parseFloat(month)-1   
              ||temp_date.getDate()!=parseFloat(day)){   
                return false;   
        }else{   
            return true;   
        }   
  }
  
/**  
 * 通过身份证判断是男是女
 * @param idCard 15/18位身份证号码   
 * @return '2'-女、'1'-男  '0'未知
 */  
function maleOrFemalByIdCard(obj,target){
	var idCard = $("#"+obj).val();
	var s = validateIdCard(idCard);
	  if(s!=0){
		    //$("#"+target).html("<option value=\"\"></option>")
		  $('#'+target).combobox('setValue','');
	  }else if(idCard.length==15){
        if(idCard.substring(14,15)%2==0){
        	//$("#"+target).html("<option value=\"2\">女性</option>")
        	//$("#"+target).val(2);
        	$('#'+target).combobox('setValue','2');
        }else{
        	//$("#"+target).html("<option value=\"1\">男性</option>")
           //$("#"+target).val(1); 
        	$('#'+target).combobox('setValue','1');
        }   
    }else if(idCard.length ==18){  
        if(idCard.substring(14,17)%2==0){   
          //$("#"+target).html("<option value=\"2\">女性</option>")
        	//$("#"+target).val(2);
        	$('#'+target).combobox('setValue','2');
        }else{
        	//$("#"+target).html("<option value=\"1\">男性</option>")
           //$("#"+target).val(1); 
        	$('#'+target).combobox('setValue','1');
        }   
    }else{
    	//$("#"+target).html("<option value=\"0\">未知的性别</option>")
        //$("#"+target).val(0); 
    	$('#'+target).combobox('setValue','0');
    }
 }

/**
 * validatebox的扩充验证
 */
$.extend($.fn.validatebox.defaults.rules, {
    minLength : { // 判断最小长度
        validator : function(value, param) {
            return value.length >= param[0];
        },
        message : '最少输入 {0} 个字符。'
    },
    length:{// 验证长度，包括全角字符长度
    	validator:function(value,param){
        	var len=$.trim(value).length;
        	//var len=$.trim(value.replace(/[^\x00-\xff]/g,"aaa")).length;
            return len>=param[0]&&len<=param[1];
        },
            message:"内容长度介于{0}个字符到{1}个字符之间."
        },
    phone : {// 验证电话号码
        validator : function(value) {
            return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message : '格式不正确,请使用下面格式:020-88888888'
    },
    mobile : {// 验证手机号码
        validator : function(value) {
            return /^(13|15|18)\d{9}$/i.test(value);
        },
        message : '手机号码格式不正确(正确格式如：13450774432)'
    },
    phoneOrMobile:{//验证手机或电话
        validator : function(value) {
            return /^(13|15|18)\d{9}$/i.test(value) || /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message:'请填入手机或电话号码,如13688888888或020-8888888'
    },
    idcard : {// 验证身份证
        validator : function(value) {
        	var idNo = validateIdCard(value);
        	if(idNo==1){
        		return false;
        	}else{
        		return true;
        	}
           // return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
        },
        message : '身份证号码不正确'
    },
    floatOrInt : {// 验证是否为小数或整数
        validator : function(value) {
            return /^(\d{1,3}(,\d\d\d)*(\.\d{1,3}(,\d\d\d)*)?|\d+(\.\d+))?$/i.test(value);
        },
        message : '请输入数字，并保证格式正确'
    },
    currency : {// 验证货币
        validator : function(value) {
            return /^\d{0,}(\.\d+)?$/i.test(value);
        },
        message : '货币格式不正确'
    },
    qq : {// 验证QQ,从10000开始
        validator : function(value) {
            return /^[1-9]\d{4,9}$/i.test(value);
        },
        message : 'QQ号码格式不正确(正确如：453384319)'
    },
    integer : {// 验证整数
        validator : function(value) {
            return /^[+]?[1-9]+\d*$/i.test(value);
        },
        message : '请输入整数'
    },
    chinese : {// 验证中文
        validator : function(value) {
            return /^[\u4e00-\u9fa5]+$/i.test(value);
        },
        message : '请输入中文'
    },
    english : {// 验证英语
        validator : function(value) {
            return /^[A-Za-z]+$/i.test(value);
        },
        message : '请输入英文'
    },
    unnormal : {// 验证是否包含空格和非法字符
        validator : function(value) {
            return /.+/i.test(value);
        },
        message : '输入值不能为空和包含其他非法字符'
    },
    username : {// 验证用户名
        validator : function(value) {
            return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
        },
        message : '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
    },
    faxno : {// 验证传真
        validator : function(value) {
//          return /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/i.test(value);
            return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message : '传真号码不正确'
    },
    zip : {// 验证邮政编码
        validator : function(value) {
            return /^[1-9]\d{5}$/i.test(value);
        },
        message : '邮政编码格式不正确'
    },
    ip : {// 验证IP地址
        validator : function(value) {
            //return /d+.d+.d+.d+/i.test(value);
            return /\d+\.\d+\.\d+\.\d+/i.test(value);
        },
        message : 'IP地址格式不正确'
    },
    web:{//网址验证
    	validator:function(value){
    		return /(http[s]?|ftp):\/\/[^\/\.]+?\..+\w$/i.test(value);
    	},
    	message : '网址格式格式不正确'
    },
    checkzyw:{//验证中英文输入
    	validator:function(value){
    		return new RegExp("^[a-zA-Z\u4e00-\u9fa5]+$").test(value);
    	},
    	message : '只能输入汉字或者拼音'
    },
    name : {// 验证姓名，可以是中文或英文
        validator : function(value) {
            return /^[\u0391-\uFFE5]+$/i.test(value)|/^\w+[\w\s]+\w+$/i.test(value);
        },
        message : '请输入姓名'
    },
    carNo:{
        validator : function(value){
            return /^[\u4E00-\u9FA5][\da-zA-Z]{6}$/.test(value);
        },
        message : '车牌号码无效（例：粤J12350）'
    },
    carenergin:{
        validator : function(value){
            return /^[a-zA-Z0-9]{16}$/.test(value);
        },
        message : '发动机型号无效(例：FG6H012345654584)'
    },
    email:{
        validator : function(value){
	        return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
	    },
	    message : '请输入有效的电子邮件账号(例：abc@126.com)'
    },
    msn:{
        validator : function(value){
	        return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
	    },
	    message : '请输入有效的msn账号(例：abc@hotnail(msn/live).com)'
    },
    department:{
        validator : function(value){
            return /^[0-9]*$/.test(value);
        },
        message : '请输入部门排序号(例：1)'  
    },
    same:{
        validator : function(value, param){
            if($("#"+param[0]).val() != "" && value != ""){
                return $("#"+param[0]).val() == value;
            }else{
                return true;
            }
        },
        message : '两次输入的密码不一致！'
    },
    code:{//验证只能输入数字与字母
        validator : function(value){
            return /^[A-Za-z0-9_-]+$/.test(value);
        },
        message : '只能输入数字与字母,不能输入汉字！'
    },
    remoteCheckExists:{//验证只能输入数字与字母
        validator : function(value,param){
        	var data={};
        	data[param[1]]=value;
        	var r=$.ajax({url:param[0],dataType:"json",data:data,async:false,cache:false,type:"post"}).responseText;
        	return r=="true";
        },
        message : '编码已存在,请重新输入'
    },
    remoteCheckJobNumber:{//验证只能输入数字与字母
        validator : function(value,param){
        	var data={};
        	data[param[1]]=value;
        	var r=$.ajax({url:param[0],dataType:"json",data:data,async:false,cache:false,type:"post"}).responseText;
        	return r=="true";
        },
        message : '工号已存在,请重新输入'
    }
});

$.fn.extend({
	/**
	 * 修改DataGrid对象的默认大小，以适应页面宽度。
	 * 
	 * @param heightMargin
	 *            高度对页内边距的距离。
	 * @param widthMargin
	 *            宽度对页内边距的距离。
	 * @param minHeight
	 *            最小高度。
	 * @param minWidth
	 *            最小宽度。
	 * 
	 */
	resizeDataGrid : function(heightMargin, widthMargin, minHeight, minWidth) {
		var height = $(document.body).height() - heightMargin;
		var width = $(document.body).width() - widthMargin;

		height = height < minHeight ? minHeight : height;
		width = width < minWidth ? minWidth : width;

		$(this).datagrid('resize', {
			height : height,
			width : width
		});
	}
});

/**
 * JS数组排重
 */
Array.prototype.unique = function() {
	var n = {};
	var r = [];
	this.forEach(function(v){
		if (!n[typeof(v) + v]){
			n[typeof(v) + v] = true;
			r.push(v);
		}
	});
	return r;
}

if (!Array.prototype.forEach) {
	Array.prototype.forEach = function(callback, thisArg) {
		var T, k;
		if (this == null) {
			throw new TypeError(" this is null or not defined");
		}
		var O = Object(this);
		var len = O.length >>> 0; // Hack to convert O.length to a UInt32
		if ({}.toString.call(callback) != "[object Function]") {
			throw new TypeError(callback + " is not a function");
		}
		if (thisArg) {
			T = thisArg;
		}
		k = 0;
		while (k < len) {
			var kValue;
			if (k in O) {
				kValue = O[k];
				callback.call(T, kValue, k, O);
			}
			k++;
		}
	};
}

/**
 * JS数组排重(树节点)
 */
Array.prototype.uniqueTreeNode = function() {
	var n = {};
	var r = [];
	this.forEach(function(v){
		var temp = v.id
		if (!n[typeof(temp) + temp]){
			n[typeof(temp) + temp] = true;
			r.push(v);
		}
	});
	return r;
}