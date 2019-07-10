package com.github.bg.admin.core.entity;

import com.github.bg.admin.core.util.UuidGenId;
import tk.mybatis.mapper.annotation.KeySql;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "t_message")
public class Message {
    /**
     * 消息流水ID
     */
    @Id
    @Column(name = "messageId")
    @KeySql(genId = UuidGenId.class)
    private String messageId;

    /**
     * 消息标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 消息内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 创建事件
     */
    @Column(name = "crtDate")
    private Date crtDate;

    /**
     * 创建用户ID
     */
    @Column(name = "crtUserId")
    private String crtUserId;

    /**
     * 创建用户名称
     */
    @Column(name = "crtUserName")
    private String crtUserName;

    /**
     * 消息类型【1：系统消息；2：其他消息】
     */
    @Column(name = "type")
    private String type;

    /**
     * 消息的接受者集合
     */
    @Transient
    private List<TargetMessage> targetMessageList;

    public List<TargetMessage> getTargetMessageList() {
        return targetMessageList;
    }

    public void setTargetMessageList(List<TargetMessage> targetMessageList) {
        this.targetMessageList = targetMessageList;
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
     * 获取消息标题
     *
     * @return title - 消息标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置消息标题
     *
     * @param title 消息标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取消息内容
     *
     * @return content - 消息内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置消息内容
     *
     * @param content 消息内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取创建事件
     *
     * @return crtDate - 创建事件
     */
    public Date getCrtDate() {
        return crtDate;
    }

    /**
     * 设置创建事件
     *
     * @param crtDate 创建事件
     */
    public void setCrtDate(Date crtDate) {
        this.crtDate = crtDate;
    }

    /**
     * 获取创建用户ID
     *
     * @return crtUserId - 创建用户ID
     */
    public String getCrtUserId() {
        return crtUserId;
    }

    /**
     * 设置创建用户ID
     *
     * @param crtUserId 创建用户ID
     */
    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    /**
     * 获取创建用户名称
     *
     * @return crtUserName - 创建用户名称
     */
    public String getCrtUserName() {
        return crtUserName;
    }

    /**
     * 设置创建用户名称
     *
     * @param crtUserName 创建用户名称
     */
    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
    }

    /**
     * 获取消息类型【1：系统消息；2：其他消息】
     *
     * @return type - 消息类型【1：系统消息；2：其他消息】
     */
    public String getType() {
        return type;
    }

    /**
     * 设置消息类型【1：系统消息；2：其他消息】
     *
     * @param type 消息类型【1：系统消息；2：其他消息】
     */
    public void setType(String type) {
        this.type = type;
    }
}