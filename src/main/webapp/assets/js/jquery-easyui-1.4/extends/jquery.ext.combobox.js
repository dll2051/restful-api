(function ($, undefined) {
	$.fn.combobox.extensions = {};


    function getItem(target) {
        var t = $(target), opts = t.combobox("options"),
            value = t.combobox("getValue"), data = t.combobox("getData");
        return $.array.first(data, function (val) { return val[opts.valueField] == value; });
    };
    
    function onHidePanel(){
    	var opts = $(this).combobox('options');
		if (opts.mode == 'remote') {/* 如果是自动补全模式 */
			var _data = $(this).combobox('getData');/* 下拉框所有选项 */
			var _value = $(this).combobox('getValue');/* 用户输入的值 */
			var _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */
			for (var i = 0; i < _data.length; i++) {
				if (_data[i][opts.valueField] == _value) {
					_b = true;
				}
			}
			if (!_b) {/* 如果在下拉列表中没找到用户输入的字符 */
				$(this).combobox('setValue', '');
			}
		}
    };
    
	var methods = $.fn.combobox.extensions.methods = {

		/**
		 * 获取当前选择了的项
		 * @return {Object} 返回一个 JSON-Object，该 JSON-Object 为当前 easyui-combobox 数据源中的一个子项，包含 idField 和 textField 的值；如果当前 easyui-combobox 没有选中任何值，则返回 null。
		 */
        getItem: function (jq) { return getItem(jq[0]); },
        
        /**
         * 扩展combobox在自动补全模式时，检查用户输入的字符是否存在于下拉框中，如果不存在则清空用户输入
         */
        onHidePanel: function (jq) { return onHidePanel(jq[0]); }

    };

    $.extend($.fn.combobox.methods, methods);
})(jQuery);