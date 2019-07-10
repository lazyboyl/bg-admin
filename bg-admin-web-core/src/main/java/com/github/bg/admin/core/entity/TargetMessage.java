package com.github.bg.admin.core.entity;

import com.github.bg.admin.core.util.UuidGenId;
import tk.mybatis.mapper.annotation.KeySql;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_target_message")
public class TargetMessage {
    /**
     * 流水ID
     */
    @Id
    @Column(name = "targetMessageId")
    @KeySql(genId = UuidGenId.class)
    private String targetMessageId;

    /**
     * 消息流水ID
     */
    @Column(name = "messageId")
    private String messageId;

    /**
     * 消息状态【1：未读；2：已读】
     */
    @Column(name = "state")
    private String state;

    /**
     * 用户ID
     */
    @Column(name = "userId")
    private String userId;

    /**
     * 发送时间
     */
    @Column(name = "sendTime")
    private Date sendTime;

    /**
     * 接收人姓名
     */
    @Column(name = "targetName")
    private String targetName;

    /**
     * 阅读时间
     */
    @Column(name = "readeTime")
    private Date readeTime;

    /**
     * 删除状态【0：已删除；1：正常】
     */
    @Column(name = "removeState")
    private String removeState;

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * 获取流水ID
     *
     * @return targetMessageId - 流水ID
     */
    public String getTargetMessageId() {
        return targetMessageId;
    }

    /**
     * 设置流水ID
     *
     * @param targetMessageId 流水ID
     */
    public void setTargetMessageId(String targetMessageId) {
        this.targetMessageId = targetMessageId;
    }

    /**
     * 获取消息流水ID
     *
     * @return messageId - 消息流水ID
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * 设置消息流水ID
     *
     * @param messageId 消息流水ID
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * 获取消息状态【1：未读；2：已读】
     *
     * @return state - 消息状态【1：未读；2：已读】
     */
    public String getState() {
        return state;
    }

    /**
     * 设置消息状态【1：未读；2：已读】
     *
     * @param state 消息状态【1：未读；2：已读】
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取用户ID
     *
     * @return userId - 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取发送时间
     *
     * @return sendTime - 发送时间
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * 设置发送时间
     *
     * @param sendTime 发送时间
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 获取阅读时间
     *
     * @return readeTime - 阅读时间
     */
    public Date getReadeTime() {
        return readeTime;
    }

    /**
     * 设置阅读时间
     *
     * @param readeTime 阅读时间
     */
    public void setReadeTime(Date readeTime) {
        this.readeTime = readeTime;
    }

    /**
     * 获取删除状态【0：已删除；1：正常】
     *
     * @return removeState - 删除状态【0：已删除；1：正常】
     */
    public String getRemoveState() {
        return removeState;
    }

    /**
     * 设置删除状态【0：已删除；1：正常】
     *
     * @param removeState 删除状态【0：已删除；1：正常】
     */
    public void setRemoveState(String removeState) {
        this.removeState = removeState;
    }
}