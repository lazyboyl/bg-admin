package com.github.bg.admin.core.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_behavior_log")
public class BehaviorLog {
    /**
     * 行为日志流水ID
     */
    @Id
    @Column(name = "behaviorLogId")
    @GeneratedValue(generator = "JDBC")
    private Long behaviorLogId;

    /**
     * 响应方法
     */
    @Column(name = "actionMethod")
    private String actionMethod;

    /**
     * 请求时间
     */
    @Column(name = "actionDate")
    private Date actionDate;

    /**
     * 请求人
     */
    @Column(name = "actionUser")
    private String actionUser;

    /**
     * 请求人ID
     */
    @Column(name = "actionUserId")
    private String actionUserId;

    /**
     * 获取行为日志流水ID
     *
     * @return behaviorLogId - 行为日志流水ID
     */
    public Long getBehaviorLogId() {
        return behaviorLogId;
    }

    /**
     * 设置行为日志流水ID
     *
     * @param behaviorLogId 行为日志流水ID
     */
    public void setBehaviorLogId(Long behaviorLogId) {
        this.behaviorLogId = behaviorLogId;
    }

    /**
     * 获取响应方法
     *
     * @return actionMethod - 响应方法
     */
    public String getActionMethod() {
        return actionMethod;
    }

    /**
     * 设置响应方法
     *
     * @param actionMethod 响应方法
     */
    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    /**
     * 获取请求时间
     *
     * @return actionDate - 请求时间
     */
    public Date getActionDate() {
        return actionDate;
    }

    /**
     * 设置请求时间
     *
     * @param actionDate 请求时间
     */
    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    /**
     * 获取请求人
     *
     * @return actionUser - 请求人
     */
    public String getActionUser() {
        return actionUser;
    }

    /**
     * 设置请求人
     *
     * @param actionUser 请求人
     */
    public void setActionUser(String actionUser) {
        this.actionUser = actionUser;
    }

    /**
     * 获取请求人ID
     *
     * @return actionUserId - 请求人ID
     */
    public String getActionUserId() {
        return actionUserId;
    }

    /**
     * 设置请求人ID
     *
     * @param actionUserId 请求人ID
     */
    public void setActionUserId(String actionUserId) {
        this.actionUserId = actionUserId;
    }
}