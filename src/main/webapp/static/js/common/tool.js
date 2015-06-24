(function($){
	/**
	 * 检查权限并确定是否显示
	 * 
	 * @param 权限系统中的权限编码
	 */
   $.fn.checkPermission = function(v){
//	  	var res = $("#left .active", parent.document);
//	  	var resId;
//	  	if(res && res.length>0){
//	  		resId = $(res[0]).children("a").attr("id");
//	  	}
	  	var currentMenuResId=parent.App.getCurrMenuResId();
		var actions = parent.App.getActions(currentMenuResId)
		var exist = $.array.contains(actions, v!=null?v:$(this).attr("id"), function(v1, v2){
			return v1.resCode == v2;
		});
	   if(!exist){
		  $(this).hide();
	   }
   }
   /**
    * 判断是否具有某个权限
    * 
    * @param 权限系统中的权限编码
    * @author hutao
    * 
    * @return true/false
    */
   $.judgeExistPermission = function(permissionCode){
	  	var currentMenuResId=parent.App.getCurrMenuResId();
		var actions = parent.App.getActions(currentMenuResId)
		var exist = $.array.contains(actions, permissionCode, function(v1, v2){
			return v1.resCode == v2;
		});
	   return exist;
  }
})(jQuery)