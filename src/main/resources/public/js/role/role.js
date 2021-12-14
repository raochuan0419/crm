layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //角色列表展示
    var  tableIns = table.render({
        elem: '#roleList',
        url : ctx+'/role/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'roleName', title: '角色名', minWidth:50, align:"center"},
            {field: 'roleRemark', title: '角色备注', minWidth:100, align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });

    // 多条件搜索
    $(".search_btn").on("click",function(){
        tableIns.reload({
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                roleName: $("input[name='roleName']").val()
            }
        })
    });


    /*头部工具栏*/
    //头工具栏事件
    table.on('toolbar(roles)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event){
            case 'add':
                openAddOrUpdateRolePage();
                break;
            case 'grant':
                openAddGrantDailog(checkStatus.data);
                break;
            case 'del':
                // 点击删除按钮，将对应选中的记录删除
                deleteRoleChance(checkStatus.data);
        };
    });

    function openAddGrantDailog(datas){
        if(datas.length==0){
            layer.msg("请选择待授权角色记录!", {icon: 5});
            return;
        }
        if(datas.length>1){
            layer.msg("暂不支持批量角色授权!", {icon: 5});
            return;
        }
        var url = ctx+"/role/toAddGrantPage?roleId="+datas[0].id;
        var title="角色管理-角色授权";
        layui.layer.open({
            title : title,
            type : 2,
            area:["600px","280px"],
            maxmin:true,
            content : url
        });
    }

    /**
     * 批量删除角色数据
     * @param data
     */
    function deleteRoleChance(data) {
        // 判断用户是否选择了要删除的记录
        if (data.length == 0) {
            layer.msg("请选择要删除的记录！");
            return;
        }
        // 询问用户是否确认删除
        layer.confirm("您确定要删除选中的记录吗？",{
            btn:["确认","取消"],
        },function (index) {
            // 关闭确认框
            layer.close(index);
            // ids=1&ids=2&ids=3
            var ids = "ids=";
            // 遍历获取对应的id
            for (var i = 0; i < data.length; i++) {
                if (i < data.length - 1) {
                    ids = ids + data[i].id + "&ids=";
                } else {
                    ids = ids + data[i].id;
                }
            }
            // 发送ajax请求，删除记录
            $.ajax({
                type:"post",
                url: ctx + "/role/delete",
                data:ids, // 参数传递的是数组
                dataType:"json",
                success:function (result) {
                    if (result.code == 200) {
                        // 加载表格
                        tableIns.reload();
                    } else {
                        layer.msg(result.msg, {icon: 5});
                    }
                }
            });
        });
    }

    /**
     * 添加和修改
     * @param roleId
     */
    function openAddOrUpdateRolePage(roleId){
        var title="<h2>角色模块--添加</h2>";
        var url=ctx+"/role/addOrUpdateRolePage";
        //判断添加或者修改
        if(roleId){
            title="<h2>角色模块--更新</h2>";
            url=url+"?roleId="+roleId;
        }
        //弹出层
        layer.open({
            title:title,
            type:2,
            content:url,
            area:["600px","280px"],
            maxmin:true
        })
    }

    /*行内工具栏*/
    //监听行工具事件
    table.on('tool(roles)', function(obj){
        var data = obj.data;
        console.log(data.id);
        if(obj.event === 'del'){
            layer.confirm('真的删除行么', function(index){

                $.post(ctx+"/role/delete",{"ids":data.id},function(result){
                    if(result.code==200){
                        layer.msg("删除成功了",{icon : 6 });
                        layer.close(index);
                        //重新加载数据
                        tableIns.reload();
                    }else{
                        layer.msg(result.msg,{icon : 5 });
                    }
                },"json");
            });
        } else if(obj.event === 'edit'){
            //修改，编辑操作
            openAddOrUpdateRolePage(data.id);
        }
    });
});