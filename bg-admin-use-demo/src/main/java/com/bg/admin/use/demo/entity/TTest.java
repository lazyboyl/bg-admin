package com.bg.admin.use.demo.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_test")
public class TTest {
    /**
     * 流水ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private String id;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 年龄
     */
    @Column(name = "age")
    private Integer age;

    /**
     * 出生日期
     */
    @Column(name = "birthDate")
    private Date birthDate;

    /**
     * 创建时间
     */
    @Column(name = "crtDate")
    private Date crtDate;

    /**
     * 收入
     */
    @Column(name = "money")
    private Integer money;

    /**
     * 获取流水ID
     *
     * @return id - 流水ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置流水ID
     *
     * @param id 流水ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取年龄
     *
     * @return age - 年龄
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 设置年龄
     *
     * @param age 年龄
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 获取出生日期
     *
     * @return birthDate - 出生日期
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * 设置出生日期
     *
     * @param birthDate 出生日期
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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
     * 获取收入
     *
     * @return money - 收入
     */
    public Integer getMoney() {
        return money;
    }

    /**
     * 设置收入
     *
     * @param money 收入
     */
    public void setMoney(Integer money) {
        this.money = money;
    }
}