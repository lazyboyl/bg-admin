package com.github.bg.admin.core.dto;

import java.util.List;

/**
 * @author linzf
 * @since 2019/5/5
 * 类描述：前端展示菜单的dto实体
 */
public class TreeDto {

    /**
     * 菜单流水ID
     */
    private Integer treeId;
    /**
     * 菜单名字
     */
    private String title;

    /**
     * 菜单编码
     */
    private String treeCode;
    /**
     * 菜单状态【1：可用；2：禁用】
     */
    private String treeState;
    /**
     * 菜单类型【1：菜单节点；2：按钮节点】
     */
    private String treeType;
    /**
     * 父节点唯一标识
     */
    private Integer parentTreeId;
    /**
     * 父节点名字
     */
    private String parentTreeName;
    /**
     * 默认子节点都是展开的
     */
    private boolean expand = true;
    /**
     * 默认节点是没有选中的
     */
    private boolean checked = false;
    /**
     * 子节点数据
     */
    private List<TreeDto> children;

    public Integer getTreeId() {
        return treeId;
    }

    public void setTreeId(Integer treeId) {
        this.treeId = treeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTreeCode() {
        return treeCode;
    }

    public void setTreeCode(String treeCode) {
        this.treeCode = treeCode;
    }

    public String getTreeState() {
        return treeState;
    }

    public void setTreeState(String treeState) {
        this.treeState = treeState;
    }

    public String getTreeType() {
        return treeType;
    }

    public void setTreeType(String treeType) {
        this.treeType = treeType;
    }

    public Integer getParentTreeId() {
        return parentTreeId;
    }

    public void setParentTreeId(Integer parentTreeId) {
        this.parentTreeId = parentTreeId;
    }

    public String getParentTreeName() {
        return parentTreeName;
    }

    public void setParentTreeName(String parentTreeName) {
        this.parentTreeName = parentTreeName;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<TreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<TreeDto> children) {
        this.children = children;
    }
}
