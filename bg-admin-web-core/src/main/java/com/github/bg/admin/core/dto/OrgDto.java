package com.github.bg.admin.core.dto;

import java.util.List;

/**
 * @author linzf
 * @since 2019/5/09
 * 类描述：前端展示组织架构的dto实体
 */
public class OrgDto {

    /**
     * 流水ID
     */
    private Integer orgId;

    /**
     * 菜单名字
     */
    private String title;

    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 父流水ID
     */
    private Integer parentOrgId;

    /**
     * 父组织架构名字
     */
    private String parentOrgName;

    /**
     * 菜单流水完整路径(1.2.3)
     */
    private String fullPath;

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
    private List<OrgDto> children;

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(Integer parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
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

    public List<OrgDto> getChildren() {
        return children;
    }

    public void setChildren(List<OrgDto> children) {
        this.children = children;
    }
}
