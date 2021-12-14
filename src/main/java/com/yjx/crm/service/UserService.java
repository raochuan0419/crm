package com.yjx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjx.crm.base.BaseService;
import com.yjx.crm.bean.User;
import com.yjx.crm.bean.UserRole;
import com.yjx.crm.mapper.UserRoleMapper;
import com.yjx.crm.model.UserModel;
import com.yjx.crm.mapper.UserMapper;
import com.yjx.crm.query.UserQuery;
import com.yjx.crm.utils.AssertUtil;
import com.yjx.crm.utils.Md5Util;
import com.yjx.crm.utils.PhoneUtil;
import com.yjx.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName, String userPwd) {
        // 1. 验证参数
        checkLoginParams(userName, userPwd);
        // 2. 根据用户名，查询用户对象
        User user = userMapper.queryUserByUserName(userName);
        //在 UserMapper 接口类中定义对应的查询方法
        // 3. 判断用户是否存在 (用户对象为空，记录不存在，方法结束)
        AssertUtil.isTrue(null == user, "用户不存在或已注销！");
        // 4. 用户对象不为空（用户存在，校验密码。密码不正确，方法结束）
        checkLoginPwd(userPwd, user.getUserPwd());
        // 5. 密码正确（用户登录成功，返回用户的相关信息）
        return buildUserInfo(user);
    }

    /**
     * 构建返回的用户信息
     * @param user
     * @return
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        // 设置用户信息（将 userId 加密）
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 验证登录密码
     * @param userPwd 前台传递的密码
     * @param upwd 数据库中查询到的密码
     * @return
     */
    private void checkLoginPwd(String userPwd, String upwd) {
        // 数据库中的密码是经过加密的，将前台传递的密码先加密，再与数据库中的密码作比较
        userPwd = Md5Util.encode(userPwd);
        // 比较密码
        AssertUtil.isTrue(!userPwd.equals(upwd), "用户密码不正确！");
    }
    /**
     * 验证用户登录参数
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        // 判断姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户姓名不能为空！");
        // 判断密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "用户密码不能为空！");
    }

    /**
     * 修改密码
     * @param uid 用户Id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param confirmPassword 确认密码
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePasswordById(Integer uid,String oldPassword,String newPassword,String confirmPassword){
        //通过Id查找用户
        User user = userMapper.selectByPrimaryKey(uid);
        //校验数据
        checkUpdatePassword(user,oldPassword,newPassword,confirmPassword);
        //设置新密码
        user.setUserPwd(Md5Util.encode(confirmPassword));
        //修改密码
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"密码修改失败");
    }

    /**
     * 密码校验
     * @param user 用户信息
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param confirmPassword 确认密码
     */
    private void checkUpdatePassword(User user, String oldPassword, String newPassword, String confirmPassword) {
        AssertUtil.isTrue(user==null,"用户不存在或用户未登录");
        AssertUtil.isTrue(oldPassword.isBlank(),"旧密码不能为空");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"旧密码输入错误");
        AssertUtil.isTrue(newPassword.isBlank(),"新密码不能为空");
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码不能和旧密码一致");
        AssertUtil.isTrue(confirmPassword.isBlank(),"确认密码不能为空");
        AssertUtil.isTrue(!(confirmPassword.equals(newPassword)),"确认密码必须与新密码一致");
    }

    /**
     * 查询sale
     * @return
     */
    public List<Map<String,Object>> queryAllSales(){
        System.out.println(userMapper.queryAllSales());
        return userMapper.queryAllSales();
    }

    /**
     * 多条件分页查询
     * @param userQuery
     * @return
     */
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        Map<String,Object> map = new HashMap<>();
        //分页
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        PageInfo<User> pageInfo = new PageInfo<>(userMapper.selectByParams(userQuery));
        //设置键值对
        map.put("code",0);
        map.put("msg", "");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void  saveUser(User user){
        //校验数据
        checkParams(user.getUserName(),user.getEmail(),user.getPhone());
        //设置初始参数
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        //执行添加
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"用户添加失败!");
        User temp = userMapper.queryUserByUserName(user.getUserName());
        relaionUserRole(temp.getId(),user.getRoleIds());
    }

    private void relaionUserRole(int userId, String roleIds) {
        //查询当前用户的角色数量
        int count = userRoleMapper.countUserRoleByUserId(userId);
        if (count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色分配失败!");
        }
        List<UserRole> userRoles = new ArrayList<>();
        for (String s : roleIds.split(",")) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(Integer.parseInt(s));
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            userRoles.add(userRole);
        }
        AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles)<userRoles.size(),"用户角色分配失败!");
    }

    private void checkParams(String userName, String email, String phone) {
        AssertUtil.isTrue(userName.isBlank(),"用户名不能为空!");
        User user = userMapper.queryUserByUserName(userName);
        AssertUtil.isTrue(user!=null,"用户名已存在!");
        AssertUtil.isTrue(email.isBlank(),"邮箱不能为空!");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"电话号码格式错误!");
    }

    /**
     * 用户更新
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        //校验数据
        User temp = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(temp==null,"要更新的用户不存在!");
        AssertUtil.isTrue(user.getUserName().isBlank(),"用户名不能为空!");
        AssertUtil.isTrue(user.getEmail().isBlank(),"邮箱不能为空!");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()),"电话号码格式错误!");
        Integer userId = userMapper.queryUserByUserName(user.getUserName()).getId();
        relaionUserRole(userId,user.getRoleIds());
        //设置更新时间
        user.setUpdateDate(new Date());
        //执行更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户更新失败!");
    }

    /**
     * 批量删除用户
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserByIds(Integer[] ids){
        //校验数据
        AssertUtil.isTrue(ids==null||ids.length==0,"请选择待删除的用户记录!");
        //执行删除
        AssertUtil.isTrue(userMapper.deleteBatch(ids)<1,"用户记录删除失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer userId){
        //校验数据
        AssertUtil.isTrue(userId==null,"待删除用户不存在!");
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(user==null,"待删除用户不存在!");
        int count = userRoleMapper.countUserRoleByUserId(userId);
        if (count>0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)<count,"用户角色删除失败!");
        }
        //设置属性
        user.setIsValid(0);
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户记录删除失败!");
    }
}
