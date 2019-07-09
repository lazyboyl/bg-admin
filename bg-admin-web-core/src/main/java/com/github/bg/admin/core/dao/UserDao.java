package com.github.bg.admin.core.dao;

import com.github.bg.admin.core.entity.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author linzf
 * @since 2019-07-05
 * 类描述：用户的dao
 */
public interface UserDao extends Mapper<User> {

    /**
     * 功能描述：根据token和旧的密码来更新新的密码
     * @param token 登录的token
     * @param oldPassword 旧的密码
     * @param newPassword 新的密码
     * @return 返回更新结果
     */
    int changePassword(@Param("token")String token,@Param("oldPassword")String oldPassword,@Param("newPassword")String newPassword);

    /**
     * 功能描述：根据token来获取用户数据
     * @param token token的值
     * @return 返回获取的结果
     */
    User getUserInfo(@Param("token")String token);

    /**
     * 功能描述：实现用户的登陆
     * @param loginAccount 用户账号
     * @param loginPassword 用户密码
     * @return 返回登陆结果
     */
    User login(@Param("loginAccount")String loginAccount,@Param("loginPassword")String loginPassword);

    /**
     * 功能描述：验证这个账户是否已经创建过了
     * @param loginAccount 用户账号
     * @return 返回验证结果
     */
    int checkLoginAccount(@Param("loginAccount")String loginAccount);

    /**
     * 功能描述：查询用户的数据
     * @param search 根据账号和名称来模糊查询
     * @param fullPath 父组织架构的匹配路径
     * @return 返回查询结果
     */
    List<User> queryUserList(@Param("search") String search, @Param("fullPath") String fullPath);

}