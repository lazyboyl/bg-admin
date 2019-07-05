package com.github.bg.admin.core.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_tree")
public class Tree {
    /**
     * 菜单流水ID
     */
    @Id
    @Column(name = "treeId")
    @GeneratedValue(generator = "JDBC")
    private Integer treeId;

    /**
     * 菜单名字
     */
    @Column(name = "treeName")
    private String treeName;

    /**
     * 菜单编码
     */
    @Column(name = "treeCode")
    private String treeCode;

    /**
     * 菜单状态【1：可用；2：禁用】
     */
    @Column(name = "treeState")
    private String treeState;

    /**
     * 菜单类型【1：菜单节点；2：按钮节点】
     */
    @Column(name = "treeType")
    private String treeType;

    /**
     * 父节点编号
     */
    @Column(name = "parentTreeId")
    private Integer parentTreeId;

    /**
     * 父节点名称
     */
    @Column(name = "parentTreeName")
    private String parentTreeName;

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
     * 权限路径采用小写的冒号分隔
     */
    @Column(name = "powerPath")
    private String powerPath;

    public String getPowerPath() {
        return powerPath;
    }

    public void setPowerPath(String powerPath) {
        this.powerPath = powerPath;
    }

    /**
     * 获取菜单流水ID
     *
     * @return treeId - 菜单流水ID
     */
    public Integer getTreeId() {
        return treeId;
    }

    /**
     * 设置菜单流水ID
     *
     * @param treeId 菜单流水ID
     */
    public void setTreeId(Integer treeId) {
        this.treeId = treeId;
    }

    /**
     * 获取菜单名字
     *
     * @return treeName - 菜单名字
     */
    public String getTreeName() {
        return treeName;
    }

    /**
     * 设置菜单名字
     *
     * @param treeName 菜单名字
     */
    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    /**
     * 获取菜单编码
     *
     * @return treeCode - 菜单编码
     */
    public String getTreeCode() {
        return treeCode;
    }

    /**
     * 设置菜单编码
     *
     * @param treeCode 菜单编码
     */
    public void setTreeCode(String treeCode) {
        this.treeCode = treeCode;
    }

    /**
     * 获取菜单状态【1：可用；2：禁用】
     *
     * @return treeState - 菜单状态【1：可用；2：禁用】
     */
    public String getTreeState() {
        return treeState;
    }

    /**
     * 设置菜单状态【1：可用；2：禁用】
     *
     * @param treeState 菜单状态【1：可用；2：禁用】
     */
    public void setTreeState(String treeState) {
        this.treeState = treeState;
    }

    /**
     * 获取菜单类型【1：菜单节点；2：按钮节点】
     *
     * @return treeType - 菜单类型【1：菜单节点；2：按钮节点】
     */
    public String getTreeType() {
        return treeType;
    }

    /**
     * 设置菜单类型【1：菜单节点；2：按钮节点】
     *
     * @param treeType 菜单类型【1：菜单节点；2：按钮节点】
     */
    public void setTreeType(String treeType) {
        this.treeType = treeType;
    }

    /**
     * 获取父节点编号
     *
     * @return parentTreeId - 父节点编号
     */
    public Integer getParentTreeId() {
        return parentTreeId;
    }

    /**
     * 设置父节点编号
     *
     * @param parentTreeId 父节点编号
     */
    public void setParentTreeId(Integer parentTreeId) {
        this.parentTreeId = parentTreeId;
    }

    /**
     * 获取父节点名称
     *
     * @return parentTreeName - 父节点名称
     */
    public String getParentTreeName() {
        return parentTreeName;
    }

    /**
     * 设置父节点名称
     *
     * @param parentTreeName 父节点名称
     */
    public void setParentTreeName(String parentTreeName) {
        this.parentTreeName = parentTreeName;
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