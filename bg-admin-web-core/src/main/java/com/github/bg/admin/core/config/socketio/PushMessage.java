package com.github.bg.admin.core.config.socketio;

/**
 * @author linzf
 * @since 2019-06-13
 * 类描述：socket消息发送实体类
 */
public class PushMessage {

    /**
     * 登录socket的socketToken
     */
    private String socketToken;

    /**
     * 推送的内容
     */
    private String content;

    /**
     * 空的构造函数
     */
    public PushMessage() {
        super();
    }

    /**
     * 构造函数
     * @param socketToken
     * @param content
     */
    public PushMessage(String socketToken, String content) {
        this.socketToken = socketToken;
        this.content = content;
    }

    public String getSocketToken() {
        return socketToken;
    }

    public void setSocketToken(String socketToken) {
        this.socketToken = socketToken;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
