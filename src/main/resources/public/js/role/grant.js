var zTreeObj;
$(function () {
    loadModuleInfo();
});


function loadModuleInfo() {
    $.ajax({
        type:"post",
        url:ctx+"/module/queryAllModules",
        data:{
            roleId:$("#roleId").val()
        },
        dataType:"json",
        success:function (data) {
            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            var setting = {
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                view:{
                    showLine: false
                // showIcon: false
                },
                check: {
                    enable: true,
                    chkboxType: { "Y": "ps", "N": "ps" }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };
            var zNodes =data;
            zTreeObj=$.fn.zTree.init($("#test1"), setting, zNodes);
        }
    })
}

function zTreeOnCheck(event, treeId, treeNode) {
    //alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
    var nodes = zTreeObj.getCheckedNodes(true);
    var roleId=$("#roleId").val();
    //roleId,mid---t-permission;

    //收集数据
    var mids="mids=";
    //遍历
    for(var x in nodes){
        //判断是否到最后一个元素
        if(x<nodes.length-1){
            mids=mids+nodes[x].id+"&mids="
        }else{
            mids=mids+nodes[x].id;
        }
    }
    /*发送ajaxg添加授权*/

    $.ajax({
        type:"post",
        url:ctx+"/role/addGrant",
        data:mids+"&roleId="+roleId,
        dataType:"json",
        success:function(data){
            alert(data.msg);
        }
    });
};
