package com.yjx.crm.controller;

import com.yjx.crm.base.BaseController;
import com.yjx.crm.bean.User;
import com.yjx.crm.mapper.PermissionMapper;
import com.yjx.crm.service.UserService;
import com.yjx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionMapper permissionMapper;
    /**
     * 系统登录页
     * @return
     */
    @RequestMapping({"index","/"})
    public String index(){
        return "index";
    }
    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request) {
        // 通过工具类，从cookie中获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调用对应Service层的方法，通过userId主键查询用户对象
        User user = userService.selectByPrimaryKey(userId);
        // 将用户对象设置到request作用域中
        request.setAttribute("user", user);
        //查询权限列表
        List<String> permissions = permissionMapper.queryUserHasRolesHasPermissions(userId);
        //System.out.println(permissions);
        //将数据放入Session中
        request.getSession().setAttribute("permissions",permissions);
        return "main";
    }
}
