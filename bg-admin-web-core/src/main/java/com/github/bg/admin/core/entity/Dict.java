package com.github.bg.admin.core.entity;

import javax.persistence.*;

@Table(name = "t_dict")
public class Dict {
    /**
     * 字典流水ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private String id;

    /**
     * 字典类型
     */
    @Column(name = "dictType")
    private String dictType;

    /**
     * 字典编码
     */
    @Column(name = "dictCode")
    private String dictCode;

    /**
     * 字典文本
     */
    @Column(name = "dictText")
    private String dictText;

    /**
     * 字典值
     */
    @Column(name = "dictValue")
    private String dictValue;

    /**
     * 获取字典流水ID
     *
     * @return id - 字典流水ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置字典流水ID
     *
     * @param id 字典流水ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取字典类型
     *
     * @return dictType - 字典类型
     */
    public String getDictType() {
        return dictType;
    }

    /**
     * 设置字典类型
     *
     * @param dictType 字典类型
     */
    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    /**
     * 获取字典编码
     *
     * @return dictCode - 字典编码
     */
    public String getDictCode() {
        return dictCode;
    }

    /**
     * 设置字典编码
     *
     * @param dictCode 字典编码
     */
    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    /**
     * 获取字典文本
     *
     * @return dictText - 字典文本
     */
    public String getDictText() {
        return dictText;
    }

    /**
     * 设置字典文本
     *
     * @param dictText 字典文本
     */
    public void setDictText(String dictText) {
        this.dictText = dictText;
    }

    /**
     * 获取字典值
     *
     * @return dictValue - 字典值
     */
    public String getDictValue() {
        return dictValue;
    }

    /**
     * 设置字典值
     *
     * @param dictValue 字典值
     */
    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }
}