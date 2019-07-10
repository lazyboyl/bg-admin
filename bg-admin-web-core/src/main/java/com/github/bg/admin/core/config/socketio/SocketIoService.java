package com.github.bg.admin.core.config.socketio;

public interface SocketIoService {

    /**
     * 推送的事件
     */
    String PUSH_EVENT = "push_event";

    /**
     * 启动服务
     *
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * 停止服务
     */
    void stop();

    /**
     * 功能描述：实现消息的发送
     *
     * @param loginAccount 需要发送到的账号
     * @param content      需要发送的内容
     */
    void pushMessage(String loginAccount, String content);

}
