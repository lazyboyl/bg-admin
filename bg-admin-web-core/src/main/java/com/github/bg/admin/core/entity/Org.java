package com.github.bg.admin.core.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_org")
public class Org {
    /**
     * 流水ID
     */
    @Id
    @Column(name = "orgId")
    @GeneratedValue(generator = "JDBC")
    private Integer orgId;

    /**
     * 组织名字
     */
    @Column(name = "orgName")
    private String orgName;

    /**
     * 组织编码
     */
    @Column(name = "orgCode")
    private String orgCode;

    /**
     * 父流水ID
     */
    @Column(name = "parentOrgId")
    private Integer parentOrgId;

    /**
     * 父组织架构名字
     */
    @Column(name = "parentOrgName")
    private String parentOrgName;

    /**
     * 创建时间
     */
    @Column(name = "crtDate")
    private Date crtDate;

    /**
     * 菜单流水完整路径(1.2.3)
     */
    @Column(name = "fullPath")
    private String fullPath;

    /**
     * 获取流水ID
     *
     * @return orgId - 流水ID
     */
    public Integer getOrgId() {
        return orgId;
    }

    /**
     * 设置流水ID
     *
     * @param orgId 流水ID
     */
    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    /**
     * 获取组织名字
     *
     * @return orgName - 组织名字
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * 设置组织名字
     *
     * @param orgName 组织名字
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 获取组织编码
     *
     * @return orgCode - 组织编码
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置组织编码
     *
     * @param orgCode 组织编码
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * 获取父流水ID
     *
     * @return parentOrgId - 父流水ID
     */
    public Integer getParentOrgId() {
        return parentOrgId;
    }

    /**
     * 设置父流水ID
     *
     * @param parentOrgId 父流水ID
     */
    public void setParentOrgId(Integer parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    /**
     * 获取父组织架构名字
     *
     * @return parentOrgName - 父组织架构名字
     */
    public String getParentOrgName() {
        return parentOrgName;
    }

    /**
     * 设置父组织架构名字
     *
     * @param parentOrgName 父组织架构名字
     */
    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
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
     * 获取菜单流水完整路径(1.2.3)
     *
     * @return fullPath - 菜单流水完整路径(1.2.3)
     */
    public String getFullPath() {
        return fullPath;
    }

    /**
     * 设置菜单流水完整路径(1.2.3)
     *
     * @param fullPath 菜单流水完整路径(1.2.3)
     */
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
}