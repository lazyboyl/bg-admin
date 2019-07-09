package com.github.bg.admin.core.entity;

import com.github.bg.admin.core.util.UuidGenId;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;

@Table(name = "t_user_org")
public class UserOrg {
    /**
     * 用户组织关联流水ID
     */
    @Id
    @Column(name = "userOrgId")
    @KeySql(genId = UuidGenId.class)
    private String userOrgId;

    /**
     * 用户流水ID
     */
    @Column(name = "userId")
    private String userId;

    /**
     * 组织流水ID
     */
    @Column(name = "orgId")
    private Integer orgId;

    /**
     * 获取用户组织关联流水ID
     *
     * @return userOrgId - 用户组织关联流水ID
     */
    public String getUserOrgId() {
        return userOrgId;
    }

    /**
     * 设置用户组织关联流水ID
     *
     * @param userOrgId 用户组织关联流水ID
     */
    public void setUserOrgId(String userOrgId) {
        this.userOrgId = userOrgId;
    }

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
     * 获取组织流水ID
     *
     * @return orgId - 组织流水ID
     */
    public Integer getOrgId() {
        return orgId;
    }

    /**
     * 设置组织流水ID
     *
     * @param orgId 组织流水ID
     */
    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
}