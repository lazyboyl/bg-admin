package com.github.bg.admin.core.entity;

import com.github.bg.admin.core.util.UuidGenId;
import tk.mybatis.mapper.annotation.KeySql;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_role")
public class Role {
    /**
     * 角色流水ID
     */
    @Id
    @Column(name = "roleId")
    @KeySql(genId = UuidGenId.class)
    private String roleId;

    /**
     * 角色名字
     */
    @Column(name = "roleName")
    private String roleName;

    /**
     * 角色编码
     */
    @Column(name = "roleCode")
    private String roleCode;

    /**
     * 创建时间
     */
    @Column(name = "crtDate")
    private Date crtDate;

    /**
     * 角色数据集合
     */
    @Transient
    private String roleTrees;

    /**
     * 空的构造函数
     */
    public Role(){
        super();
    }

    public String getRoleTrees() {
        return roleTrees;
    }

    public void setRoleTrees(String roleTrees) {
        this.roleTrees = roleTrees;
    }

    /**
     * 包含角色ID的构造函数
     * @param roleId 角色ID
     */
    public Role(String roleId){
        this.roleId = roleId;
    }


    /**
     * 获取角色流水ID
     *
     * @return roleId - 角色流水ID
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置角色流水ID
     *
     * @param roleId 角色流水ID
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取角色名字
     *
     * @return roleName - 角色名字
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名字
     *
     * @param roleName 角色名字
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取角色编码
     *
     * @return roleCode - 角色编码
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * 设置角色编码
     *
     * @param roleCode 角色编码
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
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
}