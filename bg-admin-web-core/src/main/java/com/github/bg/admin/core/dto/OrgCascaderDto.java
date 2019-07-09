package com.github.bg.admin.core.dto;

import java.util.List;

/**
 * @author linzf
 * @since 2019/5/13
 * 类描述： 级联下拉的dto
 */
public class OrgCascaderDto {

    /**
     * 流水ID
     */
    private String value;

    /**
     * 组织架构名称
     */
    private String label;

    /**
     * 子节点数据
     */
    private List<OrgCascaderDto> children;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<OrgCascaderDto> getChildren() {
        return children;
    }

    public void setChildren(List<OrgCascaderDto> children) {
        this.children = children;
    }
}
