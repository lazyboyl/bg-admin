package com.github.bg.admin.core.dao;

import com.github.bg.admin.core.entity.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author linzf
 * @since 2019-07-05
 * 类描述：用户的dao
 */
public interface UserDao extends Mapper<User> {

    /**
     * 功能描述：实现用户的登陆
     * @param loginAccount 用户账号
     * @param loginPassword 用户密码
     * @return 返回登陆结果
     */
    User login(@Param("loginAccount")String loginAccount, @Param("loginPassword")String loginPassword);

}