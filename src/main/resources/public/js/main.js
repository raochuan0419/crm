layui.use(['element', 'layer', 'layuimini','jquery','jquery_cookie'], function () {
    var $ = layui.jquery,
        layer = layui.layer,
        $ = layui.jquery_cookie($);

    // 菜单初始化
    $('#layuiminiHomeTabIframe').html('<iframe width="100%" height="100%" frameborder="0"  src="welcome"></iframe>')
    layuimini.initTab();
    /**
     * 用户退出
     * 删除cookie
     */
    $(".login-out").click(function () {
        // 删除 cookie
        $.removeCookie("userIdStr", {domain:"localhost",path:"/crm"});
        $.removeCookie("userName", {domain:"localhost",path:"/crm"});
        $.removeCookie("trueName", {domain:"localhost",path:"/crm"});
        // 跳转到登录页面 (父窗口跳转)
        window.parent.location.href = ctx + "/index";
    });
});