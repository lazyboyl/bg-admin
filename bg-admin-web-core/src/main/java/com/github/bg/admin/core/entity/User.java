package com.github.bg.admin.core.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "t_user")
public class User implements Serializable {

    private static final long serialVersionUID = -5809782578282943998L;

    /**
     * 用户流水ID
     */
    @Id
    @Column(name = "userId")
    @GeneratedValue(generator = "JDBC")
    private String userId;

    /**
     * 用户登录账号
     */
    @Column(name = "loginAccount")
    private String loginAccount;

    /**
     * 用户登录密码
     */
    @Column(name = "loginPassword")
    private String loginPassword;

    /**
     * 创建时间
     */
    @Column(name = "crtDate")
    private Date crtDate;

    /**
     * 真实姓名
     */
    @Column(name = "nickName")
    private String nickName;

    /**
     * 最后登录时间
     */
    @Column(name = "lastLoginDate")
    private Date lastLoginDate;

    /**
     * 登录的token
     */
    @Column(name = "token")
    private String token;

    /**
     * 用户头像图片地址
     */
    @Column(name = "headImg")
    private String headImg;

    /**
     * 获取用户流水ID
     *
     * @return userId - 用户流水ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户流水ID
     *
     * @param userId 用户流水ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取用户登录账号
     *
     * @return loginAccount - 用户登录账号
     */
    public String getLoginAccount() {
        return loginAccount;
    }

    /**
     * 设置用户登录账号
     *
     * @param loginAccount 用户登录账号
     */
    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    /**
     * 获取用户登录密码
     *
     * @return loginPassword - 用户登录密码
     */
    public String getLoginPassword() {
        return loginPassword;
    }

    /**
     * 设置用户登录密码
     *
     * @param loginPassword 用户登录密码
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    /**
     * 获取创建时间
     *
     * @return crtDate - 创建时间
     */
    public Date getCrtDate() {
        return crtDate;
    }

    /**
     * 设置创建时间
     *
     * @param crtDate 创建时间
     */
    public void setCrtDate(Date crtDate) {
        this.crtDate = crtDate;
    }

    /**
     * 获取真实姓名
     *
     * @return nickName - 真实姓名
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置真实姓名
     *
     * @param nickName 真实姓名
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 获取最后登录时间
     *
     * @return lastLoginDate - 最后登录时间
     */
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    /**
     * 设置最后登录时间
     *
     * @param lastLoginDate 最后登录时间
     */
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    /**
     * 获取登录的token
     *
     * @return token - 登录的token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置登录的token
     *
     * @param token 登录的token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取用户头像图片地址
     *
     * @return headImg - 用户头像图片地址
     */
    public String getHeadImg() {
        return headImg;
    }

    /**
     * 设置用户头像图片地址
     *
     * @param headImg 用户头像图片地址
     */
    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}