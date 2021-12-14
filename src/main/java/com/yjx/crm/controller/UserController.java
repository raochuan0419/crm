package com.yjx.crm.controller;

import com.yjx.crm.base.BaseController;
import com.yjx.crm.base.ResultInfo;
import com.yjx.crm.bean.User;
import com.yjx.crm.model.UserModel;
import com.yjx.crm.query.UserQuery;
import com.yjx.crm.service.UserService;
import com.yjx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    /**
     * 用户登录
     * @param userName
     * @param userPwd
    6.3.6. Starter
    修改启动类，在启动类上添加 @MapperScan 注解，设置扫描包范围。
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin (String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        // 通过 try catch 捕获 Service 层抛出的异常
        // 调用Service层的登录方法，得到返回的用户对象
        UserModel userModel = userService.userLogin(userName, userPwd);
        /**
        * 登录成功后，有两种处理：
        * 1. 将用户的登录信息存入 Session （ 问题：重启服务器，Session 失效，客户端需要重复登录 ）
        * 2. 将用户信息返回给客户端，由客户端（Cookie）保存
        */
        // 将返回的UserModel对象设置到 ResultInfo 对象中
        resultInfo.setResult(userModel);
        return resultInfo;
    }

    /**
     * 修改密码
     * @param request 请求
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param confirmPassword 确认密码
     * @return 操作信息
     */
    @PostMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,String oldPassword, String newPassword, String confirmPassword){
        ResultInfo resultInfo = new ResultInfo();
        //获取当前用户Id
        Integer uid = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updatePasswordById(uid,oldPassword,newPassword,confirmPassword);
        return resultInfo;
    }

    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    @RequestMapping("setting")
    @ResponseBody
    public ResultInfo sayUpdate(User user) {
        ResultInfo resultInfo = new ResultInfo();
        //修改信息
        userService.updateByPrimaryKeySelective(user);
        //返回目标数据对象
        return resultInfo;
    }

    @RequestMapping("toSettingPage")
    public String setting(HttpServletRequest req) {
        //获取用户的ID
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //调用方法
        User user = userService.selectByPrimaryKey(userId);
        //存储
        req.setAttribute("user", user);
        //转发
        return "user/setting";
    }
    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }

    /**
     * 多条件分页查询
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        return userService.queryUserByParams(userQuery);
    }

    /**
     * 用户模块跳转
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user){
        userService.saveUser(user);
        return success("用户添加成功!");
    }

    /**
     * 用户更新
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户更新成功!");
    }

    /**
     * 添加用户
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateUserPage")
    public String addUserPage(Integer id, Model model){
        if (id!=null) {
            model.addAttribute("user",userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUserByIds(ids);
        return success("用户记录删除成功!");
    }
}
