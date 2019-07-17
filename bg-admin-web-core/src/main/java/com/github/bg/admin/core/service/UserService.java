package com.github.bg.admin.core.service;


import com.github.bg.admin.core.auth.Auth;
import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.dao.*;
import com.github.bg.admin.core.entity.*;
import com.github.bg.admin.core.util.JsonUtils;
import com.github.bg.admin.core.util.PageUtil;
import com.github.bg.admin.core.util.RsaUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linzf
 * @since 2019/4/25
 * 类描述：用户的service类的实现
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private UserOrgDao userOrgDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private OrgDao orgDao;

    @Autowired
    private TreeDao treeDao;

    @Autowired
    private Auth authProvider;

    @Value("${auth.privateKey}")
    private String privateKey;

    /**
     * 功能描述：根据token和旧的密码来更新新的密码
     *
     * @param token       登录的token
     * @param oldPassword 旧的密码
     * @param newPassword 新的密码
     * @return 返回更新结果
     */
    public ReturnInfo changePassword(String token, String oldPassword, String newPassword) {
        if (userDao.changePassword(token, oldPassword, newPassword) > 0) {
            return new ReturnInfo(SystemStaticConst.SUCCESS, "更新用户密码成功");
        } else {
            return new ReturnInfo(SystemStaticConst.FAIL, "密码不正确，更新失败！");
        }
    }

    /**
     * 功能描述：删除用户
     *
     * @param userId 用户ID
     * @return 返回删除结果
     */
    public ReturnInfo deleteUser(String userId) {
        if (userDao.deleteByPrimaryKey(userId) > 0) {
            // 根据用户ID删除角色表和组织架构表的关联数据
            userRoleDao.deleteUserRoleByUserId(userId);
            userOrgDao.deleteUserOrgByUserId(userId);
            return new ReturnInfo(SystemStaticConst.SUCCESS, "删除用户数据成功");
        } else {
            return new ReturnInfo(SystemStaticConst.FAIL, "查无此用户数据");
        }
    }

    /**
     * 功能描述：更新用户
     *
     * @param userId       用户ID
     * @param nickName     真实姓名
     * @param headImg      头像地址
     * @param province     所在省
     * @param provinceName 所在省名称
     * @param city         所在市
     * @param cityName     所在市名称
     * @param area         所在区
     * @param areaName     所在区名称
     * @param address      地址
     * @param orgIds       组织架构ID
     * @param roles        角色集合
     * @return 返回数据新增的结果
     */
    public ReturnInfo updateUser(@RequestParam(name = "userId") String userId,
                                 @RequestParam(name = "nickName") String nickName,
                                 @RequestParam(name = "headImg") String headImg,
                                 @RequestParam(name = "province") String province,
                                 @RequestParam(name = "provinceName") String provinceName,
                                 @RequestParam(name = "city") String city,
                                 @RequestParam(name = "cityName") String cityName,
                                 @RequestParam(name = "area") String area,
                                 @RequestParam(name = "areaName") String areaName,
                                 @RequestParam(name = "address") String address,
                                 @RequestParam(name = "orgIds") String[] orgIds,
                                 @RequestParam(name = "roles") String[] roles) {
        User user = userDao.selectByPrimaryKey(userId);
        if (user == null) {
            return new ReturnInfo(SystemStaticConst.FAIL, "查无此用户数据");
        }
        user.setNickName(nickName);
        user.setHeadImg(headImg);
        user.setCrtDate(new Date());
        user.setProvince(province);
        user.setProvinceName(provinceName);
        user.setCity(city);
        user.setCityName(cityName);
        user.setArea(area);
        user.setAreaName(areaName);
        user.setAddress(address);
        userDao.updateByPrimaryKey(user);
        // 根据用户ID删除角色表和组织架构表的关联数据
        userRoleDao.deleteUserRoleByUserId(userId);
        userOrgDao.deleteUserOrgByUserId(userId);
        // 增加用户和角色的关联的数据
        UserRole userRole;
        for (String role : roles) {
            userRole = new UserRole();
            userRole.setRoleId(role);
            userRole.setUserId(user.getUserId());
            userRoleDao.insert(userRole);
        }
        String orgId = orgIds[orgIds.length - 1];
        UserOrg userOrg = new UserOrg();
        userOrg.setOrgId(Integer.parseInt(orgId));
        userOrg.setUserId(user.getUserId());
        userOrgDao.insert(userOrg);
        return new ReturnInfo(SystemStaticConst.SUCCESS, "用户更新成功");
    }


    /**
     * 功能描述：根据用户流水ID来获取用户数据
     *
     * @param userId 用户流水ID
     * @return 返回获取结果
     */
    public ReturnInfo getUserByUserId(String userId) {
        User user = userDao.selectByPrimaryKey(userId);
        if (user == null) {
            return new ReturnInfo(SystemStaticConst.SUCCESS, "查无此用户数据");
        }
        Map<String, Object> result = JsonUtils.objToMap(user);
        List<UserRole> userRoleList = userRoleDao.getUserRoleByUserId(userId);
        String[] roleIds = new String[userRoleList.size()];
        for (int i = 0; i < userRoleList.size(); i++) {
            roleIds[i] = userRoleList.get(i).getRoleId();
        }
        result.put("roles", roleIds);
        // 获取组织架构的数据数据
        result.put("orgIds", orgDao.getOrgByUserId(userId).getFullPath().split("\\."));
        return new ReturnInfo(SystemStaticConst.SUCCESS, "加载用户信息成功", result);
    }

    /**
     * 功能描述：加载所有的角色数据
     *
     * @return 返回加载结果
     */
    public ReturnInfo loadAllRole() {
        return new ReturnInfo(SystemStaticConst.SUCCESS, "加载所有的角色成功", roleDao.selectAll());
    }

    /**
     * 功能描述：创建用户
     *
     * @param loginAccount 用户账号
     * @param nickName     真实姓名
     * @param headImg      头像地址
     * @param province     所在省
     * @param provinceName 所在省名称
     * @param city         所在市
     * @param cityName     所在市名称
     * @param area         所在区
     * @param areaName     所在区名称
     * @param address      地址
     * @param orgIds       组织架构ID
     * @param roles        角色集合
     * @return 返回数据新增的结果
     */
    public ReturnInfo createUser(String loginAccount, String nickName,
                                 String headImg, String province,
                                 String provinceName, String city,
                                 String cityName, String area,
                                 String areaName, String address,
                                 String[] orgIds, String[] roles) {
        if (userDao.checkLoginAccount(loginAccount) > 0) {
            return new ReturnInfo(SystemStaticConst.SUCCESS, "账号已经存在，请修改以后再进行提交！");
        }
        User user = new User();
        user.setLoginAccount(loginAccount);
        user.setNickName(nickName);
        user.setHeadImg(headImg);
        user.setLoginPassword("123456");
        user.setCrtDate(new Date());
        user.setProvince(province);
        user.setProvinceName(provinceName);
        user.setCity(city);
        user.setCityName(cityName);
        user.setArea(area);
        user.setAreaName(areaName);
        user.setAddress(address);
        userDao.insert(user);
        // 增加用户和角色的关联的数据
        UserRole userRole;
        for (String role : roles) {
            userRole = new UserRole();
            userRole.setRoleId(role);
            userRole.setUserId(user.getUserId());
            userRoleDao.insert(userRole);
        }
        String orgId = orgIds[orgIds.length - 1];
        UserOrg userOrg = new UserOrg();
        userOrg.setOrgId(Integer.parseInt(orgId));
        userOrg.setUserId(user.getUserId());
        userOrgDao.insert(userOrg);
        return new ReturnInfo(SystemStaticConst.SUCCESS, "用户创建成功");
    }

    /**
     * 功能描述：验证这个账户是否已经创建过了
     *
     * @param loginAccount 用户账号
     * @return 返回验证结果
     */
    public ReturnInfo checkLoginAccount(String loginAccount) {
        Map<String, Object> result = new HashMap<>(1);
        if (userDao.checkLoginAccount(loginAccount) > 0) {
            result.put("success", "unPass");
        } else {
            result.put("success", "pass");
        }
        return new ReturnInfo(SystemStaticConst.SUCCESS, "验证请求发送成功", result);
    }

    /**
     * 功能描述：获取用户列表
     *
     * @param fullPath     父组织架构的匹配路径
     * @param search       根据账号和名称来模糊查询
     * @param pageSize     每页显示的记录的条数
     * @param current      当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    public ReturnInfo queryUserList(String fullPath, String search, int pageSize, int current, String orderKey, String orderByValue) {
        PageHelper.startPage(current, (pageSize > 0 && pageSize <= 500) ? pageSize : 20, (orderKey != null && !"".equals(orderKey)) ? ((orderByValue != null && !"".equals(orderByValue)) ? (orderKey + " " + orderByValue) : orderKey) : "");
        HashMap<String, Object> res = PageUtil.getResult(userDao.queryUserList(search, fullPath));
        return new ReturnInfo(SystemStaticConst.SUCCESS, "获取用户列表数据成功！", new Page(pageSize, current, (long) res.get("total"), (List) res.get("rows")));
    }

    /**
     * 功能描述：实现重新刷新token
     *
     * @param refreshToken token的值
     * @return 返回刷新结果
     */
    public ReturnInfo refreshToken(String refreshToken) {
        return authProvider.refreshToken(refreshToken);
    }


    /**
     * 功能描述：实现用户登陆
     *
     * @param loginAccount  用户账号
     * @param loginPassword 用户密码
     * @return 返回登陆结果
     */
    public ReturnInfo login(String loginAccount, String loginPassword) {
        loginPassword = RsaUtil.decrypt(loginPassword, privateKey);
        return authProvider.login(loginAccount, loginPassword);
    }

    /**
     * 功能描述：根据token来获取用户数据
     *
     * @param token token的值
     * @return 返回获取的结果
     */
    public ReturnInfo getUserInfo(String token) {
        User user = userDao.getUserInfo(token);
        if (user == null) {
            return new ReturnInfo(SystemStaticConst.FAIL, "获取账号信息错误");
        }
        user.setLoginPassword(null);
        List<Tree> treeList = treeDao.getLoginUserTree(user.getUserId());
        String[] access = new String[treeList.size()];
        for (int i = 0; i < treeList.size(); i++) {
            access[i] = treeList.get(i).getTreeCode();
        }
        Map<String, Object> result = JsonUtils.objToMap(user);
        result.put("access", access);
        return new ReturnInfo(SystemStaticConst.SUCCESS, "登陆成功", result);
    }


}
