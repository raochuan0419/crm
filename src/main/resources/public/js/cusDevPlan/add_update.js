layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    /**
     * 添加或更新计划项
     */
    form.on("submit(addOrUpdateCusDevPlan)", function (data) {
        // 弹出loading层
        var index = top.layer.msg('数据提交中，请稍候', {
            icon: 16, time: false,
            shade: 0.8
        });
        var url = ctx + "/cus_dev_plan/save";
        if ($("input[name='id']").val()) {
            url = ctx + "/cus_dev_plan/update";
        }
        /*console.log(data.field);*/
        $.post(url, data.field, function (result) {
            if (result.code == 200) {
                setTimeout(function () {
                    // 关闭弹出层（返回值为index的弹出层）
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    // 关闭所有ifream层
                    layer.closeAll("iframe");
                    // 刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(result.msg, {icon: 5});
            }
        });
        return false;
    });
    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        // 先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        // 再执行关闭
        parent.layer.close(index);
    });
});