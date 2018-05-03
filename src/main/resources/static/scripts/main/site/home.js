(function (window, undefined) {
    var PopupAdd = Base.getClass('main.component.PopupAdd');
    var PopupMsg = Base.getClass('main.component.PopupMsg');

    Base.ready({
        initialize: fInitialize,
        binds: {
            'click #zu-top-add-question': fClickAdd,
            'click #zh-top-nav-count-wrap': fClickMsg
        }
    });

    function fInitialize() {
        var that = this;
    }

    function fClickAdd() {
        var that = this;
        //对PopupAdd(提问框)的监听, 如果ok(即添加成功), 那么刷新到首页
        PopupAdd.show({
            ok: function () {
                window.location.replace("/");
            }
        });
    }

    function fClickMsg() {
            var that = this;
            PopupMsg.show({
                ok: function () {
                    window.location.replace("/msg/list");
                }
            });
        }

})(window);