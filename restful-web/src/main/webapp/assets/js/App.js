var App = function() {
	var frameId=0;
	var url="getResources.action";
	var userInfo = {};
	var resources = [];
	var actions = {};
	var curr_menu = "";
	//当前菜单资源ID
	var currMenuResId = ""
	/**
	 * 初始当前用户信息
	 */
	var initUser = function(){
		var user = null;
    	if(userInfo!=null&&userInfo.organName!=null){
    		user = "【"+userInfo.organName+"】";
    	}
    	if(userInfo!=null&&userInfo.organName!=null&&userInfo.loginName!=null){
    		user+="-";
    	}
    	if(userInfo!=null&&userInfo.loginName!=null&&userInfo.loginName!="null"){
    		user +="【"+userInfo.personName+"】"
    	}
    	if(user!=null){
    		user+="，";
    	}
    	user+="欢迎您！";
    	$("#userinfo").replaceWith(user);
	};
	/**
	 * 关闭边侧栏
	 */
	var sidebarCollapse = function(){
		var panel = $('.easyui-layout').layout("panel", "west");
		var centerPanel = $('.easyui-layout').layout("panel", "center");
		if(panel.panel("options").closed){
			return;
		}
		$(centerPanel).panel("resize", {width: (centerPanel._outerWidth() + panel._outerWidth()), left: 0});
		panel.animate({left:0}, 0, function() {
			panel.panel("close");
			$(centerPanel).unbind(".layout");
		});
		$("#sidebar-expand").show();
	};
	/**
	 * 展开边侧栏
	 */
	function sidebarExpand(){
		var panel = $('.easyui-layout').layout("panel", "west");
		var centerPanel = $('.easyui-layout').layout("panel", "center");
		if(!panel.panel("options").closed){
			return;
		}
		$(centerPanel).panel("resize", {width: (centerPanel._outerWidth() - panel._outerWidth()), left: (panel._outerWidth() - 1)});
		panel.animate({left:0}, 0, function() {
			panel.panel("open");
			$(centerPanel).unbind(".layout");
		});
		$("#sidebar-expand").hide();
	};
	/**
	 * 当点击顶部菜单时触发
	 * 
	 * 
	 * 修改记录：
	 * 1、加支持直接在显示区打开一级菜单 修改人：hutao 修改日期：2014-11-17
	 * 2、1时将权限写入actions中 修改人：hutao 修改日期：2014-11-18
	 */
	var onClickMenu = function(menuIndex, menuCode){
		if (curr_menu == menuCode){
			return;
		}
		curr_menu = menuCode;
		var currentResource=resources[menuIndex];
		if(currentResource && currentResource.resContent!=null && currentResource.resContent.length>0){
			//设置当前菜单ID
			currMenuResId=currentResource.resId;
			//添加Tab
			addTab(currentResource.resName,currentResource.resContent);
			
			if(!currentResource.childRes){
				//关闭左边栏
				sidebarCollapse();
			}
			//设置权限到actions
			if(currentResource.childRes){
				if(!actions[currentResource.resId]){
					actions[currentResource.resId] = [];
				}
				actions[currentResource.resId]=currentResource.childRes;
			}
		}else{
			//设置当前菜单ID
			currMenuResId=currentResource.resId;
			//初始化并打开左边栏
			initSidebar(menuIndex, menuCode);
			sidebarExpand();
		}
	};
	/**
	 * 初始化顶部菜单栏
	 */
	var initNavbar = function(){
		var topMenuUL = $("#topMenuUL");
		//topMenuUL.append("<li><a menuIndex=\"-1\" menuCode=\"00\" href=\"javascript:void(0);\">首页</a></li>");
		for(var i = 0 ; i < resources.length ; i++){
		  topMenuUL.append("<li><a menuIndex=\""+i+"\" menuCode=\"" + resources[i].resCode + "\" href=\"javascript:void(0);\">" + resources[i].resName + "</a></li>");
		}
		$("#topMenuUL li").click(function(){
			$("li[class='hover']").removeClass("hover");
			$(this).addClass("hover");
			var menuIndex = $(this).children("a").attr("menuIndex");
			var menuCode = $(this).children("a").attr("menuCode");
			onClickMenu(menuIndex, menuCode);
		});
		$("#topMenuUL li:first").addClass("hover");
		$("#topMenuUL li:first").trigger("click");
	};
	
	var generateSidebar = function(resource){
		if (resource.childRes && resource.childRes.length >0 && resource.resType!="4"){
			var node = $("<ul class=\"sub-menu\"></ul>");
			$.each(resource.childRes, function(i, n){
				if(n.resType!="4"){
					var item = $(
							"<li>" + 
					  			"<a id=\""+n.resId+"\" name=\""+n.resName+"\" url=\""+n.resContent+"\" href=\"javascript:void(0);\">" + 
					  				"<i class=\"icon-angle-right\"></i>" +
					      			""+n.resName+"" + 
					  			"</a>" + 
					  		"</li>");
					item.append(generateSidebar(n));
					node.append(item);
				}else{
					if(!actions[resource.resId]){
						actions[resource.resId] = [];
					}
					actions[resource.resId].push(n);
				}
			});
			if(node.children("li").length == 0){
				node.removeClass("sub-menu");
				node = $("");
			}
			return node;
		}
	};
	/**
	 * 初始化边侧栏
	 * 
	 * 修改记录：
	 * 1、click事件中增加设置当前菜单ID 修改人：hutao 修改日期：2014-11-18
	 */
	var initSidebar = function(menuIndex, menuCode) {
		actions = {};
		var sidebar = $("#left");
	    var resource = resources[menuIndex];
	    
		if (!resource){
		  return;
		}
		
		var rootNode = generateSidebar(resource);
		if(!rootNode || rootNode.length==0){
			return;
		}
		rootNode.removeClass("sub-menu").attr("id", "nav");
		
		$("#nav").remove();
		sidebar.prepend(rootNode);
		
	    var z = "icon-angle-down", y = "icon-angle-left";
	    $("li", "#left ul").each(function() {
			if ($(this).hasClass("current")
					|| $(this).hasClass("open-default")) {
				$(">a", this).append("<i class='arrow " + z + "'></i>")
				$(">a>i", this).html("&#xf107;");
			} else {
				if($("ul:has(li)", this).length>0){
					$(">a", this).append("<i class='arrow " + y + "'></i>");
				}else{
					
				}
				$(">a>i", this).html("&#xf104;");
				$(">a>i:first", this).html("&#xf105;");
			}
		});
	    $("#left ul > li > a").on("click",function(C) {
			//设置当前菜单ID
			currMenuResId=$(this).attr("id");
			if ($(this).next().hasClass("sub-menu") == false) {
				sidebar.find(".active").removeClass("active");
				$(this).parent().addClass("active");
				var tabName = $(this).attr("name");
      			var url = $(this).attr("url");
      			addTab(tabName, url);
				return
			}
			var B = $(this).parent().parent();
			B.children("li.open").children("a").children("i.arrow")
					.removeClass(z).addClass(y);
			B.children("li.open").children("a").children("i.arrow").html("&#xf104;");
			B.children("li.open").children(".sub-menu")
					.slideUp(200);
			B.children("li.open-default").children(".sub-menu")
					.slideUp(200);
			B.children("li.open").removeClass("open").removeClass(
					"open-default")
			var A = $(this).next();
			if (A.is(":visible")) {
				$("i.arrow", $(this)).removeClass(z).addClass(y);
				$("i.arrow", $(this)).html("&#xf104;");
				$(this).parent().removeClass("open");
				A.slideUp(200, function() {
					$(this).parent().removeClass("open-fixed")
							.removeClass("open-default");
				})
			} else {
				$("i.arrow", $(this)).removeClass(y).addClass(z);
				$("i.arrow", $(this)).html("&#xf107;");
				$(this).parent().addClass("open");
				A.slideDown(200, function() {
				})
			}
		});
	};
	
	var addTab = function(tabname,url){
    	addFrameTab(tabname,url,"",true);
	};
	//isnew 参数是判断是不是从左侧目录树点击菜单新加的tab的标志
	var addFrameTab = function(subtitle,url,icon,isnew){
		var tabcloseable = true;
		
		if(isnew){
			var tabs = $("#tabs").tabs("tabs");
			if(tabs.length>0){
				for(var i=0; i<tabs.length; i++){
					var tab = tabs[i];
					var options = tab.panel("options");
					$('#tabs').tabs('close', options.title);
					if($("iframe").length>0){
						alert(options.title);
					}
				}
			}
			tabcloseable = false;
		}
		//如果tab不存在则创建tab,存在则激活tab
		if(!$("#tabs").tabs("exists",subtitle)){
			$("#tabs").tabs("add",{
				title: subtitle,
				content: "",
				closable: tabcloseable,
				fit: true,
				bodyCls: "panel-body-noscroll"
			});
			var tab = $('#tabs').tabs('getSelected');
			$(tab).append(createFrame(url));
		}else{
			$("#tabs").tabs("select", subtitle);
		}
	};
	
	var createFrame = function(url){
		var s = '<iframe scrolling="no"  frameborder="0"  src="'+url+'" style="width:100%;height:100%;overflow:hidden;"></iframe>';
		//var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;overflow:auto"></iframe>';
		frameId++;
		
		return s;
	}
	
	var initScroll = function() {
		$("#left").slimScroll({
			color: '#00f',
			size : "7px",
			opacity : "0.2",
			position : "right",
			height : "auto",
			disableFadeOut : true
		})
	};
	
	var setLayout = function(){
		var panel = $('.easyui-layout').layout("panel", "west");
		var options = panel.panel("options");
		options.onResize = function(width, height){
			$('.slimScrollDiv').height(height-$('#sidebar-collapse').height());
			$('#left').height(height-$('#sidebar-collapse').height());
		}
		
		$('.slimScrollDiv').height(panel._outerHeight()-$('#sidebar-collapse').height());
		$('#left').height(panel._outerHeight()-$('#sidebar-collapse').height());
	}
	
	return {
		init : function(params) {
			sidebarCollapse();
			//addFrameTab("首页",params.homePage,"",true);
			$("#sidebar-expand").hide();
			//获取当前登陆人的基本信息和可以访问的资源集合
			$.post(url, {resId:params.resId}, function(result){
				if(result){
					resources = result.menuResource;
					userInfo = result.user;
				}
				//顶部菜单
				initNavbar();
				//初始化登录人员信息
				initUser();
			}, "json");
			
			$("#sidebar-collapse").click(function(){
				sidebarCollapse();
			});
			
			$("#sidebar-expand").click(function(){
				sidebarExpand();
			});
			initScroll();
			setLayout();
		},
		getActions : function(resId) {
			if(actions){
				return actions[resId]
			}
		},
		getCurrMenuResId : function() {
			return currMenuResId;
		}
	}
}();