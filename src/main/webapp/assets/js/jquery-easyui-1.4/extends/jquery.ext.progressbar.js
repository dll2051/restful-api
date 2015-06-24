(function ($, undefined) {

    $.fn.progressbar.extensions = {};



    function setText(target, text) {
        var t = $(target), opts = t.progressbar("options");
        t.find(".progressbar-text").text(opts.text = text);
    };



    var defaults = $.fn.progressbar.extensions.defaults = {};

    var methods = $.fn.progressbar.extensions.methods = {

        //  扩展 easyui-progressbar 的自定义扩展方法；设置当前 easyui-progressbar 控件的 text 值；该方法的参数 text 表示将被设置的 text 值；
        //  返回值：返回表示当前 easyui-progressbar 的 jQuery 链式对象。
        setText: function (jq, text) { return jq.each(function () { setText(this, text); }); }
    };


    $.extend($.fn.progressbar.defaults, defaults);
    $.extend($.fn.progressbar.methods, methods);

})(jQuery);